package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.agh.kis.*;
	
/**
 * Robot - wersja z URL, jednowątkowa
 * @author Szymon Majkut
 *
 */
public class SimpleRobot {
	
	//Własny program do składowania logów
		private Logger logs = new Logger();
	
	//Wyrażenie regularne do wyciągania linku
		private String linkRegex = "<[aA]\\s+[^>]*[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+)\" ";
		
		/**
		 * Tworzę jeden obiekt do kolejki i mapy linków, w celu ułatwienia sobie
		 * synchronizowania dostępu do tych treści - pomocne np. przy implementacji
		 * z bazą danych
		 */
		private PageCash pageCash;

		/**
		 * Tutaj zmienię koncepcję i będę tworzył obiekt w nowym wątku, który
		 * będzie sobie pobierał zawartość strony
		 */
		private WWWPageDownloader pageDownloader;
		
		private String currentDomain;
		
		private void takeDomain(String d)
		{
			String[] l = d.split("/");
			
			if(l.length>=3)
			{
				currentDomain = l[2] + "/";
			}

		}
		
		/**
		 * Jeśli link nie posiada http lub nazwy domeny, są one doklejane, stąd mamy
		 * pewność, że do kontenerów trafią dobre linki
		 * @param s - link bezpośrednio z robota
		 * @return - link w odpowiednim formacie
		 */
		private String createGoodLink(String s)
		{
			if(s.startsWith("/"))
			{
				s = s.substring(1, s.length());
			}
			
			if(s.contains("http"))
			{
				return s;
			}
			else
			{
				return "http://" + currentDomain + s;
			}
		}
		
			
		/**
		 * Funkcja przeszukuję stronę zapisaną w Stringu w poszukiwaniu nowych linków
		 * @param page - String z wcześniej pobraną stroną
		 */
		public void searchPage(String page)
		{
			
			//Wyszukiwanie kolejnych linków
			Pattern p = Pattern.compile(linkRegex);		
			Matcher m = p.matcher(page);
			
			boolean found = false;
            while (m.find()) {
            	
            	//Dodajemy do linków do odwiedzenia pod warunkiem że jeszcze nieodwiedzone
            	
            	String wellString = createGoodLink(m.group(1));
            	
            	if(!pageCash.pageAlreadyVisited(wellString))
            	{            		
            		logs.info("Znalazłem nowy link!: " +
            				wellString);
            		pageCash.addPage(wellString);
                    found = true;
            	}
            	
            }
            if(!found){
            	//to później do logów info!
            	logs.info("Nie znaleziono nowych linków!");
            }
            
            //Troszeczkę logów do tego nooo, do tego nooo, do tego nooo
            //Do wylistowania kolejki i seta!
            Logger t = new Logger();
            t.changeAppender(new FileAppender("AlreadyVisiter"));
            t.info("Oto linki odwiedzone: " +
    				pageCash.listVisited());
            t.execute();
            t.changeAppender(new FileAppender("QueueVisiter"));
            t.info("Oto linki odwiedzone: " +
    				pageCash.listQueue());
            t.execute();
		}
		
		public void start(String site)
		{
			/*Przeszukujemy pierwszą ze stron, żeby pozyskać kolejne*/
			pageCash.addPage(site);
			
			while(!pageCash.isEmpty())
			{
				String next = pageCash.getNextPage();
				takeDomain(next);
				logs.info("Teraz przeszukuję link: " + next.toString());
				try {
					pageCash.addVisitedPage(next);
					searchPage(pageDownloader.downloadPage(next));
				} catch (DownloaderException e) {
					logs.error(e.getLink());
				}
				logs.execute();
            }
		
		}
		
		SimpleRobot(SimplePageCash c,WWWPageDownloader p)
		{
			pageCash = c;
			pageDownloader = p;
			//do systemu logów
			logs.changeAppender(new FileAppender("WebCrawlerLogs"));
		}
		
		public static void main(String[] args) throws MalformedURLException, DownloaderException, ClassNotFoundException, SQLException {
			//Wersja podstawowa 
			/*
			SimpleRobotV3 r = new SimpleRobotV3(new SimpleDownloadQueue(), 
					new SimpleVisitedPages(), new URLPageDownloader());
			r.start("http://kis.agh.edu.pl/");
			System.out.println("Koniec!");
			*/
			
			// Wersja z bazami danych
			
			/*
			Class.forName("org.h2.Driver");
            String DB_URL = "jdbc:h2:tcp://localhost/~/test";
            String DB_USER = "sa";
            String DB_PASSWD = "";
        	Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD); 

			SimpleRobotV3 r = new SimpleRobotV3(new DatabaseDownloadQueue(conn), 
					new DatabaseVisitedPages(conn), new URLPageDownloader());
			r.start("http://kis.agh.edu.pl/");
			
			conn.close();

			System.out.println("Koniec!");
			*/
			//Wersja podstawowa
			/*
			SimpleRobot r = new SimpleRobot(new SimplePageCash(), 
					new UrlPageDownloader());
			r.start("http://kis.agh.edu.pl/");
			System.out.println("Koniec!");
			*/
			//wersja z socketem
			/*
			SimpleRobot r = new SimpleRobot(new SimplePageCash(), 
					new SocketPageDownloader());
			r.start("http://kis.agh.edu.pl/");
			System.out.println("Koniec!");
			*/
			//wersja url z bazą danych
			Class.forName("org.h2.Driver");
            String DB_URL = "jdbc:h2:tcp://localhost/~/test";
            String DB_USER = "sa";
            String DB_PASSWD = "";
        	Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD); 

			
			SimpleRobot r = new SimpleRobot(new SimplePageCash(), 
					new UrlPageDownloader());
			r.start("http://kis.agh.edu.pl/");
			
			conn.close();
			System.out.println("Koniec!");
			
		}
}