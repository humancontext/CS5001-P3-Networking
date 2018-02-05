import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author 170024030
 *
 */
public class RequestLog {
	
	/**
	 * the constructor used to update server log.
	 * log contains time, request type, requested file and response. 
	 * @param path
	 * @param requestType
	 * @param requestFile
	 * @param responseCode
	 */
	public RequestLog(String path, String requestType, String requestFile, String responseCode) {
		try{
			//if the log file does not exist, create one.
			File f = new File (path + ServerConstants.LOGFILE_NAME);
			if (!f.exists()) {
				f.createNewFile();
			}
			//updating server log.
			String str = "";
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
			str += df.format(date);
			str += "\t" + requestType + "\t\t" + requestFile + "\t\t" + responseCode;
			FileOutputStream fos = new FileOutputStream(f.getPath(), true);
			fos.write(str.getBytes());
		}catch (IOException ioe){
			System.out.println(ioe.getMessage());
		}
	}
	
}
