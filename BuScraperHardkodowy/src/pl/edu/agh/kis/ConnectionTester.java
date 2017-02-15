package pl.edu.agh.kis;

import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class ConnectionTester {

	public boolean testHost(String hostName)
	{
		boolean result = true;
		Socket socket;
		
		try {
			socket = new Socket(hostName, 80);
			socket.setSoTimeout(100);
		} catch (IOException e) {
			result = false;
		}

		return result;
	}
}
