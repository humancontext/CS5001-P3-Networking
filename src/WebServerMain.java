import java.io.*;
import java.util.Scanner;
/**
 * Main class to execute the server and deal with error.
 * @author 170024030
 *
 */
public class WebServerMain {
	/**
	 * Use try-catch to ensure the given parameters are valid.
	 * Another thread is implemented to allow the server to quit when
	 * "Q" is inputed by keyboard.
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[1]);	//transform String to Integer.
			Server server = new Server(args[0], port);	//construct server
			new Thread(server).start();
			
			//allow the server to quit manually from the server side.
			System.out.println("Input \"Q\" to shut down server.");
			Scanner keyboardScanner = new Scanner(System.in);
			
			//capture keyboard input. shut down server thread if "Q"
			while (true) {
				String str = keyboardScanner.nextLine();
				if (str.equals("Q")) {
					break;
				}
			}
			server.stopServer();	//stop server.
		} catch(ArrayIndexOutOfBoundsException ae) {
			System.out.println("Usage: java WebServerMain <document_root> <port>");
		} 
	}
	
	
}

