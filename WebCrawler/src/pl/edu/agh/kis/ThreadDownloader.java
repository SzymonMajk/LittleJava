package pl.edu.agh.kis;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Obiekt pojedynczego wątku, działający tak długo, dopóki wszystkie inne wątki
 * jego rodzaju w programie wciąż działają, jest w stanie pobrać strone oraz wyciągnąć
 * z niej linki, synchronizując wpsółpracę z PageCasem oraz Loggerem
 * @author szymek
 *
 */
public class ThreadDownloader extends Thread {

	private int threadId;
	
	//Wyrażenie regularne do wyciągania linku
	private String linkRegex = "<[aA]\\s+[^>]*[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+)\" ";
			
	private PageCash pageCash;
	
	private String currentDomain;
	
	/**
	 * Funkcja ustala aktualną domenę linku, z którego pobieramy
	 * @param d link, z którego wyciągamy domenę
	 */
	public void setCurrentDomain(String d)
	{
		String[] l = d.split("/");
		
		if(l.length>=3)
		{
			currentDomain = l[2] + "/";
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
		
        while (m.find()) {
        	
        	//Dodajemy do linków do odwiedzenia pod warunkiem że jeszcze nieodwiedzone
        	
        	String wellString = createGoodLink(m.group(1));
				

        	if(!pageCash.pageAlreadyVisited(wellString))
        	{            		
        		pageCash.addPage(wellString);
        	}
        	
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
	 * Funkcja działania wątku, wątek działa dopóki którykolwiek
	 * z pozostałych wątków jeszcze pracuje, pobiera stronę i wyciąga
	 * z niej linki, umieszczając w odpowiednich miejscach
	 */
    public void run(){
   	 
		do {
			
	   	 	String next = null;
	   	 
	   	 	 while(!pageCash.isEmpty())
			 {
	   		 //Wątek zaznacza, że jest pracujący
	   		 ThreadRobot.numberOfWorkingThreads.incrementAndGet();
	   		 next = pageCash.getNextPage();
	    			setCurrentDomain(next);
	    			//logs.info("Teraz przeszukuję link: " + next.toString());
	    			try {
	    				
	    				pageCash.addVisitedPage(next);
	        	        
	    				searchPage(new UrlPageDownloader().downloadPage(next));
	    			} catch (DownloaderException e) {
	    				//logs.error(e.getLink());
	    			}
	    		    finally {
	
	    		    	synchronized (ThreadRobot.logs) {
	    		    		ThreadRobot.logs.info("Thread: " + threadId, " Przeszukał link: " + next);
	    		    		ThreadRobot.logs.execute();
	    		    }
	
	    	    }
	    	  	//Wątek zaznacza, że wątek przestał pracować
	   	   		 ThreadRobot.numberOfWorkingThreads.decrementAndGet();
		    }
		} while (ThreadRobot.numberOfWorkingThreads.intValue() > 0);
   	 
		synchronized(System.out) {
   	 
			System.out.println("No to ja kończe swoje zadanie!" +
		         		"~Podpisano wątek: " + threadId);
   	}
   	 
       }
	
	/**
	 * Sparametryzowany konstruktor ustalający sposób składowania linków
	 * oraz numer wątku
	 * @param p
	 * @throws InterruptedException 
	 */
	ThreadDownloader(PageCash p, int id) throws InterruptedException
	{
		pageCash = p;
		threadId = id;
	}

}
