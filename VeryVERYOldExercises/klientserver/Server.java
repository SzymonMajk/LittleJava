package pl.edu.agh.kis.klientserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/*   //wersja na jednym wątku
public class Server {
	
	
	public static void main(String[] args) {

			int myPort = 2014;
			//int myPort = Integer.parseInt(args[0]);
			
			File respond = new File("public_html/index.html");
			
			try {
				
			ServerSocket serv = new ServerSocket(myPort);
			Socket comm = null;
			
			InputStream in = null;
			InputStreamReader iSR = null;
			BufferedReader bR = null;

			OutputStream out = null;
			PrintWriter pW = null;
			
			while(true)
			{
				comm = serv.accept();
			
				//czytamy co do nas przychodzi
				
				in = comm.getInputStream();
				iSR = new InputStreamReader(in);
				bR = new BufferedReader(iSR);
		
				String line = "";
	
				line = bR.readLine();
				System.out.println(line);
				//odpowiadamy
				
				bR = new BufferedReader(new FileReader(respond));
				out = comm.getOutputStream();
				pW = new PrintWriter(out);
				
				line = "";
				
				pW.write("HTTP/1.1 200 OK \r\n\r\n");
				
				while((line = bR.readLine()) != null)
				{
					pW.write(line+"\n");
				}
				
				pW.flush();
				in.close();
			
			}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
}
*/
//wersja z wątkami
public class Server {
	
	
	public static void main(String[] args) {


			if(args.length<1)
			{
				System.out.println("Podano złą ilość argumentów! Podaj adres localhost!");
				return;
			}
			
			int myPort = Integer.parseInt(args[0]);
			
			ServerSocket serv;
			try {
				serv = new ServerSocket(myPort);

				Socket comm = null;
					
				while(true)
				{
					//tworzymy nowy wątek, który ma za zadanie wysłać stronę
						
					comm = serv.accept();
					new PageSender(comm).start();
				}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}

class PageSender extends Thread{
	
	Socket s;
	
	public void run()
	{
		
		BufferedReader bR = null;
		
		InputStream in;
		try {
			in = s.getInputStream();

			InputStreamReader iSR = new InputStreamReader(in);
			bR = new BufferedReader(iSR);
	
			String line = "";
	
			//pobieramy tylko pierwszą linijkę, aby wiedzieć jaki mamy zasób
			line = bR.readLine();
			String resource = "/index.html";
			if(line != null)
			{
				resource = line.split(" ")[1];
			}

			System.out.println(resource);
			//odpowiadamy
			
			//modyfikacja dodana, aby podanie samej strony odczytywało z automau index.html
			if(resource.equals("/"))
			{
				resource = "/index.html";
			}
			
			File respond = new File("public_html"+resource);
			OutputStream out = s.getOutputStream();
			PrintWriter pW = new PrintWriter(out);
			
			
			//zabezpieczenie przed nie znalezieniem zasobu lub podaniem katalogu jako poszukiwany zasób
			if(!respond.exists() || respond.isDirectory())
			{
				pW.write("HTTP/1.1 404 Not Found \r\n\r\n");
				pW.write("<html><head></head><body><h1>Nie znaleziono!" +
						"</h1><a href=/index.html>Back to Main Page!</a></body><html>");
				pW.flush();
			}
			else
			{
				bR = new BufferedReader(new FileReader(respond));

				
				line = "";
				
				pW.write("HTTP/1.1 200 OK \r\n\r\n");
				
				while((line = bR.readLine()) != null)
				{
					pW.write(line+"\n");
				}
				
				pW.flush();
			}
			
			in.close();
			System.out.println("Skończyłem klienta!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	PageSender(Socket s)
	{
		this.s = s;
	}
}