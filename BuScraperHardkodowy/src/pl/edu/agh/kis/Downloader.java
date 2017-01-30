package pl.edu.agh.kis;

import java.io.InputStream;
import java.io.OutputStream;

public interface Downloader {

	boolean createSocket();
	
	OutputStream getOutputStream();
	
	InputStream getInputSteam();
}
