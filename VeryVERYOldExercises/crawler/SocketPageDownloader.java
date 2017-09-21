package pl.edu.agh.kis.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

public class SocketPageDownloader implements WWWPageDownloader {

	/**
	 * Pobiera stronę i zwraca ją jako String
	 */
	public String downloadPage(String pageURL) throws DownloaderException {

		//np. możemy sprawdząć czy strona istnieje!
		//throw new DownloaderException(pageURL);
		
		StringBuilder sb = new StringBuilder();
		
		try {

			//wyciągamy z pageURL adres hosta i zasób do pobrania
			
			String hostAndResource = pageURL.split("//",2)[1];
			
			String[] slicedPageUrl = hostAndResource.split("(?=/)",2);
			
			String host = slicedPageUrl[0];
			String resource = slicedPageUrl[1];
			

			
			System.out.println("GET "+resource+" HTTP/1.1 \r\n");
			System.out.println("Host: "+host+" \r\n");

			Socket s = new Socket(host,80);
			
			OutputStream out = s.getOutputStream();
			PrintWriter pW = new PrintWriter(out,true);
			
			String line = "";
			//wysyłamy zapytanie
			pW.write("GET "+resource+" HTTP/1.1 \r\n");
			pW.write("Host: "+host+" \r\n");
			pW.write("\r\n");
			pW.flush();
			
			InputStream in = s.getInputStream(); 
			InputStreamReader iSR = new InputStreamReader(in);
			BufferedReader bR = new BufferedReader(iSR);
			
			line = "";
			
			//pozbywamy się zbędnej treści
			while((line = bR.readLine()) != null)
			{
				if(line.equals(""))
				{
					break;
				}
			}
			
			//wyciągamy treść strony
			while((line = bR.readLine()) != null)
			{
				sb.append(line+"\n");
			}
			sb.append("<a href=\"http://www.onet.pl\">aaaa</a>");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//logs.error(e.getMessage());
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

}
