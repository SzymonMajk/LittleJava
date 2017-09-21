package pl.edu.agh.kis.crawler;

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

import pl.edu.agh.kis.logger.FileAppender;
import pl.edu.agh.kis.logger.Logger;
	
/**
 * Robot - wersja z URL, jednowątkowa
 * @author Szymon Majkut
 *
 */
public class SimpleRobotV3 {
	
	//Własny program do składowania logów
		private Logger logs = new Logger();
	
	//można np. rozszerzyć o ustalanie ich w konstruktorze z parametrów
		private String linkRegex = "<[aA]\\s+[^>]*[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+)\" ";
		
	//rozwinięcie na przyszłość - możliwość wyciągania treści ze strony
		private String elementRegex = "(AGH)";
	
		/*Kontenery stron do odwiedzenia oraz stron odwiedzonych */
		
		private VisitedPages visitedPages;
		
		private DownloadQueue downloadQueue;
		
		private WWWPageDownloader pageDownloader;
		
		private String currentDomain;
		
		private void takeDomain(String d)
		{
			String[] l = d.split("/");
			
			if(l.length>=2)
			{
				currentDomain = l[2];
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
            	if(!visitedPages.pageAlreadyVisited(m.group(1)))
            	{
            		//to później do logów info!
            		String wellString = createGoodLink(m.group(1));
            		
            		logs.info("Znalazłem nowy link!: " +
            				wellString);
            		downloadQueue.addPage(wellString);
                    found = true;
            	}
            	
            }
            if(!found){
            	//to później do logów info!
            	logs.info("Nie znaleziono nowych linków!");
            }
			/*
            //Teraz wyszukiwanie ciekawy treści ze strony:
            p = Pattern.compile(elementRegex);		
			m = p.matcher(result);
			
			found = false;
            while (m.find()) {
            	System.out.println("Znalazłem treść");
            	//Dodajemy do kolejki wywołania lub robimy tak jak poniżej od razu
            	useFindedElement(m.group(1));
            	
                found = true;
            }
            if(!found){
                System.out.println("Nie znaleziono elementów!");
            }*/
		}
		
		public void start(String site)
		{
			/*Przeszukujemy pierwszą ze stron, żeby pozyskać kolejne*/
			downloadQueue.addPage(site);
			
			while(!downloadQueue.isEmpty())
			{
				String next = downloadQueue.getNextPage();
				takeDomain(next);
				logs.info("Teraz przeszukuję link: " + next.toString());
				try {
					visitedPages.addVisitedPage(next);
					searchPage(pageDownloader.downloadPage(next.toString()));
				} catch (DownloaderException e) {
					logs.error(e.getLink());
				}
				logs.execute();
            }
		
		}
		
		SimpleRobotV3(DownloadQueue d, VisitedPages v,WWWPageDownloader p)
		{
			downloadQueue = d;
			visitedPages = v;
			pageDownloader = p;
			//do systemu logów
			logs.changeAppender(new FileAppender());
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
			//Wersja z socketem
			
			SimpleRobotV3 r = new SimpleRobotV3(new SimpleDownloadQueue(), 
					new SimpleVisitedPages(), new SocketPageDownloader());
			r.start("http://kis.agh.edu.pl/");
			System.out.println("Koniec!");
			
		}
}