import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * The server class containing the main body of server.
 * @author 170024030
 *
 */
public class Server implements Runnable {
	
	private ServerSocket socket; 
	private String serverRoot;
	private int serverPort;
	private boolean serverActive;
	private ExecutorService threadPool;	
	
	/**
	 * The server constructor.
	 * @param path
	 * @param port
	 */
	public Server(String path, int port){
		this.serverPort = port;
		this.serverRoot = path;
		this.threadPool = Executors.newFixedThreadPool(ServerConstants.SERVER_MAX_THREAD);
		this.serverActive = true;
	}
	
	/**
	 * close thread method.
	 * @throws IOException
	 */
	public void close() throws IOException{
		socket.close();
	}

	/**
	 * Overridden run method of server.
	 */
	@Override
	public void run() {
		//open new socket.
		try {
			openServerSocket();
		} catch (RuntimeException re) {
			System.out.println("Cannot open server socket.\r\n" + "Message: " + re.getMessage());
			System.exit(-1);
		}
		
		//listen for clients and deal with requests when the boolean is true.
		while (serverActive) {
			try {
				System.out.println("Server started listening. \t The Port is " + serverPort);
				Socket conn = socket.accept(); 	//listen and look for incoming clients.
				System.out.println("Server is connected with a client from " + conn.getInetAddress());
				ConnectionHandler ch = new ConnectionHandler(conn, serverRoot);	//construct handler for this connection.
				ch.handleClientRequest();	//invoke the handle method of the handler.
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
		//start shutting server when the flag is false.
		System.out.println("Shutting server");
		threadPool.shutdown();
		System.out.println("Waiting for threads to finish...");
		while(!threadPool.isTerminated()) {
			//waiting for all threads in thread pool to be terminated.
		}
		System.out.println("Server stopped");
	}
	
	/**
	 * method to stop server.
	 */
	public synchronized void stopServer() {
		serverActive = false;
		cleanServerSocket();
	}
	
	/**
	 * close sockets.
	 */
	private void cleanServerSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error whilst closing server socket.\r\n" + "Message: " + ioe.getMessage());
        }
    }
	
	/**
	 * open new sockets.
	 */
	private void openServerSocket() {
		try {
			socket = new ServerSocket(serverPort);
			serverActive = true;
			System.out.println("Server socket bound to port " + Integer.toString(serverPort) + ".");
		} catch (IOException ioe) {
            throw new RuntimeException("Port " + Integer.toString(serverPort) + " is unavailable.", ioe);
        }
	}
}
