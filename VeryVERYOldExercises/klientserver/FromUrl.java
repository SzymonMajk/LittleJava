package pl.edu.agh.kis.klientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FromUrl {

	public static void main(String[] args) {
		
		/*
		 U siebie na kompie host to localhost: / port 2010 np. */
		
		/*
		try {
			URL u = new URL("http://www.google.pl/");
			
			InputStream in = u.openStream();
			
			InputStreamReader iSR = new InputStreamReader(in);
			
			BufferedReader bR = new BufferedReader(iSR);
			
			String line = "";
			
			while((line = bR.readLine()) != null)
			{
				System.out.println(line);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		int HTTP_PORT = 80;
		
		Socket s;
		try {
			
			for(int i = 0 ; i < 2; ++i)
			{
				//s = new Socket("localhost",HTTP_PORT);
				s = new Socket("www.agh.edu.pl",HTTP_PORT);

				
				OutputStream out = s.getOutputStream();
				PrintWriter pW = new PrintWriter(out);
				
				//Najpierw musimy wysłać zapytanie
				/*
				pW.write("GET / HTTP/1.1 \r\n");
				pW.write("Host: www.agh.edu.pl \r\n");
				pW.write("\r\n");
				pW.flush();
				*/
				//Odbieramy zapytanie i zaczynamy z niego czytać
				/*
				InputStream in = s.getInputStream(); 
				InputStreamReader iSR = new InputStreamReader(in);
				BufferedReader bR = new BufferedReader(iSR);
				*/
				String line = "";
				/*
				while((line = bR.readLine()) != null)
				{
					System.out.println(line);
				}
				System.out.println("Koniec");
				*/
				//Najpierw musimy wysłać zapytanie - wersja z podszyciem się
				
				pW.write("GET / HTTP/1.1 \r\n");
				pW.write("Host: www.agh.edu.pl \r\n");
				pW.write("User-Agent: Mozilla/5.0 (X11; U; Linux i686; pl; rv:1.8.1.7) Gecko/20070914 Firefox/2.0.0.7 \r\n");
				pW.write("\r\n");
				pW.flush();
				
				InputStream in = s.getInputStream(); 
				InputStreamReader iSR = new InputStreamReader(in);
				BufferedReader bR = new BufferedReader(iSR);
				
				//Odbieramy zapytanie i zaczynamy z niego czytać
				
				line = "";
				
				while((line = bR.readLine()) != null)
				{
					System.out.println(line);
				}
				System.out.println("Koniec");
				
				s.close();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}