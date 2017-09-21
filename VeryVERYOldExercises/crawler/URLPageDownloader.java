package pl.edu.agh.kis.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class URLPageDownloader implements WWWPageDownloader {

	/**
	 * Pobiera stronę i zwraca ją jako String
	 */
	public String downloadPage(String pageURL) throws DownloaderException {

		//np. możemy sprawdząć czy strona istnieje!
		//throw new DownloaderException(pageURL);
		
		StringBuilder sb = new StringBuilder();
		
		try {
			URL u = new URL(pageURL);
			
			InputStream in = u.openStream();
			InputStreamReader iSR = new InputStreamReader(in);
			BufferedReader bR = new BufferedReader(iSR);
			
			String line = "";
			
			while((line = bR.readLine()) != null)
			{
				sb.append(line+"\n");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//logs.error(e.getMessage());
		}
		
		return sb.toString();
	}

}
