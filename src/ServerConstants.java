
public class ServerConstants {
	//the default network protocol
	public static final String NW_PROTOCOL = "HTTP/1.1";
	//the 200 protocol
	public static final String NW_200 = NW_PROTOCOL + " 200 OK\r\n";
	//the 404 protocol
	public static final String NW_404 = NW_PROTOCOL + " 404 Not Found\r\n";
	//the 415 protocol
	public static final String NW_415 = NW_PROTOCOL + " 415 Unsupported Media Type\r\n";
	//the 501 protocol
	public static final String NW_501 = NW_PROTOCOL + " 501 Not Implemented\r\n";
	//the server message
	public static final String MSS = "Server: Server_170024030 written in Java7\r\n";
	//the max thread number
	public static final int SERVER_MAX_THREAD = 10;
	//the file path of 404 page
	public static final String WP_404 = "/errorpages/404.html";
	//the file path of 415 page
	public static final String WP_415 = "/errorpages/415.html";
	//the file path of 501 page
	public static final String WP_501 = "/errorpages/501.html";
	//the file path of server log
	public static final String LOGFILE_NAME = "/server_log.txt";
	
}