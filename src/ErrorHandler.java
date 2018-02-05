import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Error handler object
 * @author 170024030
 *
 */
public class ErrorHandler {
	private String serverRoot;	//the server root path
	private String error;	//the error message
	private PrintWriter pw;	//used to print in client's terminal
	private String[] request;	//used to store client's request
	private OutputStream os;	//used to send data to client
	
	/**
	 * Constructor method
	 * @param serverRoot
	 * @param request
	 * @param error
	 * @param pw
	 * @param os
	 */
	public ErrorHandler(String serverRoot, String[] request, String error, PrintWriter pw, OutputStream os) {
		this.serverRoot = serverRoot;
		this.request = request;
		this.error = error;
		this.pw = pw;
		this.os = os;
	}
	
	/**
	 * Method implemented to handle errors.
	 */
	public void handleError(){
		//cread server log
		new RequestLog(serverRoot, request[0], request[1], error);
		//get the name of corresponding error page.
		String name = "";
		switch (error) {
		case ServerConstants.NW_404:
			name = ServerConstants.WP_404;
			break;
		case ServerConstants.NW_415:
			name = ServerConstants.WP_415;
			break;
		case ServerConstants.NW_501:
			name = ServerConstants.WP_501;
			break;
		}
		//if the error page exist, show the error page to client
		try {
			Path p = Paths.get(serverRoot + name);
			File file = new File(serverRoot + name);
			InputStream in = new FileInputStream(file);
			int len = in.available();
			String header = getErrorHeader(len);
			byte[] body = Files.readAllBytes(p);
			pw.print(header);
			pw.flush();
			os.write(body);
			pw.close();
		//if the error page do not exist, return only the header for auto-checker
		} catch (IOException ioe) {
			String header = getErrorHeader(128);
			pw.print(header);
			pw.flush();
			pw.close();
		}
	}
	
	/**
	 * Get the header of error page.
	 * @param len
	 * @return
	 */
	private String getErrorHeader(int len) {
		String header = "";
		header += error;
		header += ServerConstants.MSS;
		header += "Content-Type: text/html\r\n";
		header += "Content-Length: " + len +"\r\n";
		header += "\r\n";
		return header;
	}
}
