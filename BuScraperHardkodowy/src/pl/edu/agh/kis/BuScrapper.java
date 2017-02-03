package pl.edu.agh.kis;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G��wna klasa programu, jej zadaniem jest odpowiednia obs�uga 
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class BuScrapper {

	/**
	 * W�asny system log�w
	 */
	private Logger scrapperLogger;
	
	/**
	 * Kolejka zada� do wykonania przez program
	 */
	private LinkedList<Task> tasks = new LinkedList<Task>();
	
	/**
	 * Obiekt odpowiadaj�cy za generowanie zapyta� dla poszczeg�lnych zada�
	 */
	private RequestCreator requestCreator = new RequestCreator(tasks);
	
	/**
	 * Pole przechowuj�ce obiekt udost�pniaj�ce przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",tasks);
	
	/**
	 * Pole prywatne, bufor dla konsument�w-producent�w do przechowywania tymczasowego
	 * stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(10);
	
	/**
	 * Pole prywatne przechowuj�ce wyszukiwark� po��cze�
	 */
	private Browser browser = new Browser();
	
	/**
	 * Pole przechowuj�ce informacj� o ilo�ci nadal pracuj�cych w�tk�w
	 * jest static, aby ka�dy w�tek m�g� si� do niego �atwo odwo�a�
	 */
	static AtomicInteger numberOfWorkingThreads = new AtomicInteger(0);
	
	/**
	 * Funkcja zwraca informacj� o tym, czy powinna zosta� wykonana aktualizacja
	 * danych
	 * @return informacja o tym czy wykona� aktualizacj� danych
	 */
	public boolean doUpdate(boolean b)
	{
		return b;
	}
	
	/**
	 * Funkcja uruchamia potrzebne w�tki producent�w-konsument�w, pozwala im
	 * funkcjonowa� na przygotowanym wcze�niej buforze, dop�ki nie sko�cz� wszystkich
	 * powierzonych im zada�
	 */
	public void updateData()
	{
		while(!tasks.isEmpty())
		{
		
	    DownloadThread d1 = new DownloadThread(0,requestCreator.getRequests(),buffer,
	    		new SocketDownloader(configurator.getStartPageURL()));
	    /*DownloadThread d2 = new DownloadThread(1,requestCreator.getRequests(),buffer,
	    		new SocketDownloader(configurator.getStartPageURL()));
	    DownloadThread d3 = new DownloadThread(2,requestCreator.getRequests(),buffer,
	    		new SocketDownloader(configurator.getStartPageURL()));
	    DownloadThread d4 = new DownloadThread(3,requestCreator.getRequests(),buffer,
	    		new SocketDownloader(configurator.getStartPageURL()));
		*/
	    SnatchThread s1 = new SnatchThread(0,buffer,new FileStoreBusInfo(
	    		new FileAppender("File Store"+0)),configurator.getXPaths());
	    /*SnatchThread s2 = new SnatchThread(1,buffer,new FileStoreBusInfo(
	    		new FileAppender("File Store"+1)),configurator.getXPaths());
	    SnatchThread s3 = new SnatchThread(2,buffer,new FileStoreBusInfo(
	    		new FileAppender("File Store"+2)),configurator.getXPaths());
	    SnatchThread s4 = new SnatchThread(3,buffer,new FileStoreBusInfo(
	    		new FileAppender("File Store"+3)),configurator.getXPaths());
	    */
	    d1.start();
	    /*d2.start();
	    d3.start();
	    d4.start();*/
	    s1.start();
	    /*s2.start();
	    s3.start();
	    s4.start();*/
	    
	    try {
	    	
	    d1.join();
	    /*d2.join();
	    d3.join();
	    d4.join();*/
	    s1.join();
	    /*s2.join();
	    s3.join();
	    s4.join();*/
	    
	    } catch (InterruptedException e) {
		    scrapperLogger.error("Nast�pi�o nieoczekiwane wybudzenie w�tk�w)",e.getMessage());
	    }
	    
	    scrapperLogger.info("Zako�czy�em aktualizacj� danych)");
	    scrapperLogger.execute();
	    
		}
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalaj�cy na zmian� sposobu wysy�ania log�w
	 * @param appender obiekt odpowiedzialny za wysy�anie log�w
	 */
	BuScrapper(Appends appender)
	{
		scrapperLogger = new Logger();
		scrapperLogger.changeAppender(appender);
	}
	
	/**
	 * Konstruktor domy�lny, ustalaj�cy typowy spos�b wysy�ania log�w
	 */
	BuScrapper()
	{
		this(new FileAppender("BuScrapper"));
	}
	
	/**
	 * Przyk�adowy spos�b uruchomienia programu
	 * @param args nieu�ywane
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		
		//Opcjonalna aktualizacja zasob�w
		if(b.doUpdate(b.configurator.getUpdateData()))
		{
			b.updateData();
		}	
		
		//Teraz wyszukujemy zgodnie z zaleceniami u�ytkownika
		b.browser.serch(b.configurator.getToSerach());
		
	}
}
