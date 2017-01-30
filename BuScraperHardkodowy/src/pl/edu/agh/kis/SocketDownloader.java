package pl.edu.agh.kis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class SocketDownloader implements Downloader {

	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wejœcia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wyjœcia do hosta
	 */
	private OutputStream output;

	private String hostName;
	
	private int portNumber = 80;
	
	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return output;
	}

	@Override
	public InputStream getInputSteam() {
		// TODO Auto-generated method stub
		return input;
	}
	
	@Override
	public boolean createSocket() {
		boolean result = true;
		Socket socket;
		try {
			socket = new Socket(hostName, portNumber);
			output = socket.getOutputStream();
			input = socket.getInputStream();
		} catch (IOException e) {
			result = false;
			//logi 
			//e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 */
	SocketDownloader(String pageURL) {
		
		URL url;
		try {
			url = new URL(pageURL);
			hostName = url.getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}
