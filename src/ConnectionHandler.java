
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
/**
 * Connection handler object.
 * @author 170034030
 *
 */
public class ConnectionHandler {
	private Socket socket;	//this is the TCP/IP connection to client
	private InputStream is;	//used to get data from client
	private OutputStream os;//used to send data to client
	private PrintWriter pw;	//used to print in the client's terminal
	String serverRoot;	//the server root path
	BufferedReader br;  //used to read request from client data
	private String fileType[] = new String[2];	//used to store requested file type as "general/extension"
	private String request[];	//used to store processed requests
	
	/**
	 * the constructor.
	 * @param socket
	 * @param serverRoot
	 */
	public ConnectionHandler(Socket socket, String serverRoot) {
		this.socket = socket;
		this.serverRoot = serverRoot;
		try{
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(socket.getOutputStream(), true);
			String firstLine = br.readLine();	//read client request
			try{
				this.request = firstLine.split(" ");	//process request
			} catch (NullPointerException npe) {
				System.out.println("Error: Client connection lost.");
				System.exit(-1);
			}
			
			fileType[1] = getSucc(request[1], '.');	//find the general type from extension.
			/**
			 * text contains:
			 * 		txt
			 * 		html
			 * image contains:
			 * 		jpg
			 * 		png
			 * 		gif
			 * others are characterized as unknown for 415 errors
			 */
			switch (fileType[1]) {
			case "html":
			case "txt":
				fileType[0] = "text";
				break;
			case "jpg":
			case "png":
			case "gif":
			case "ico":
				fileType[0] = "image";
				break;
			default:
				fileType[0] = "unknown";
				break;
			}
		} catch (IOException ioe){
			System.out.println("Errors occured during construct ConnectionHandler: " + ioe.getMessage());
		}
	}
	
	/**
	 * method implemented to handle request from client.
	 * @throws IOException
	 */
	public void handleClientRequest() throws IOException {
		/**
		 * the request is handled with respect to the general type.
		 */
		switch (fileType[0]) {
		//invoke handle methods for unknown type.
		case "unknown":
			handleUnknown();
			break;
		//handle known file type.
		default:
			handleKnown();
			break;
		}
		
	}
	
	/**
	 * method that handles unknown file type.
	 * gives 415 errors.
	 * @throws IOException
	 */
	public void handleUnknown() throws IOException {
		ErrorHandler eh = new ErrorHandler(serverRoot, request, ServerConstants.NW_415, pw, os);
		eh.handleError();
	}
	
	/**
	 * method that handles known file type.
	 */
	public void handleKnown() { 
		try{
			String header;	//generate header
			File file = new File(serverRoot + request[1]);
			InputStream in = new FileInputStream(file);
			int len = in.available();	//calculate the length of content
			Path path = Paths.get(file.getPath());	//located requested file
			byte[] data = Files.readAllBytes(path);	//read bytes from requested file
			String type = fileType[0] + "/" + fileType[1]; //generate content type
			switch (request[0]) {
			//handle GET request
			case "GET":
				header = getHeader(len, type);
				//return header and body
				pw.print(header);
				pw.flush();
				os.write(data);
				os.flush();
				os.close();
				pw.close();
				new RequestLog(serverRoot, request[0], request[1], ServerConstants.NW_200);
				break;
			//handle HEAD request
			case "HEAD":
				header = getHeader(len, type);
				//return header
				pw.println(header);
				pw.flush();
				pw.close();
				new RequestLog(serverRoot, request[0], request[1], ServerConstants.NW_200);
				break;
			//handle DELETE request
			case "DELETE":
				//delete requested file
				if(file.delete()) {
					pw.println("Deletion Successful.");
					pw.flush();
					pw.close();
				}
				else {
					pw.println("Deletion Unsuccessful.");
					pw.flush();
					pw.close();
				}
				new RequestLog(serverRoot, request[0], request[1], ServerConstants.NW_200);
				break;
			//handle unimplemented request
			default:
				//construct an error handler to handle error
				ErrorHandler eh = new ErrorHandler(serverRoot, request, ServerConstants.NW_501, pw, os);
				eh.handleError();
			}
		// ioe is catched when the requested file does't exist.
		} catch (IOException ioe) {
			//construct an error handler to handle error
			ErrorHandler eh = new ErrorHandler(serverRoot, request, ServerConstants.NW_404, pw, os);
			eh.handleError();
		}
		
	}
	
	/**
	 * method implemented to get the substring of a given string after a assigned char
	 * @param str
	 * @param sign
	 * @return
	 */
	static String getSucc(String str, char sign) {
		int index = str.lastIndexOf(sign) + 1;
		String s = str.substring(index);
		return s;
	}
	
	/**
	 * generate header if the request is feasible
	 * @param len
	 * @param ft
	 * @return
	 */
	static String getHeader(int len, String ft) {
		String header = "";
		header += ServerConstants.NW_200;
		header += ServerConstants.MSS;
		header += "Content-Type: " + ft + "\r\n";
		header += "Content-Length: " + len +"\r\n";
		header += "\r\n";
		return header;
	}
}
