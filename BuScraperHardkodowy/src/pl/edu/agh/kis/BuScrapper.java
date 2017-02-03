package pl.edu.agh.kis;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G³ówna klasa programu, jej zadaniem jest odpowiednia obs³uga 
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class BuScrapper {

	/**
	 * W³asny system logów
	 */
	private Logger scrapperLogger;
	
	/**
	 * Kolejka zadañ do wykonania przez program
	 */
	private LinkedList<Task> tasks = new LinkedList<Task>();
	
	/**
	 * Obiekt odpowiadaj¹cy za generowanie zapytañ dla poszczególnych zadañ
	 */
	private RequestCreator requestCreator = new RequestCreator(tasks);
	
	/**
	 * Pole przechowuj¹ce obiekt udostêpniaj¹ce przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",tasks);
	
	/**
	 * Pole prywatne, bufor dla konsumentów-producentów do przechowywania tymczasowego
	 * stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(10);
	
	/**
	 * Pole prywatne przechowuj¹ce wyszukiwarkê po³¹czeñ
	 */
	private Browser browser = new Browser();
	
	/**
	 * Pole przechowuj¹ce informacjê o iloœci nadal pracuj¹cych w¹tków
	 * jest static, aby ka¿dy w¹tek móg³ siê do niego ³atwo odwo³aæ
	 */
	static AtomicInteger numberOfWorkingThreads = new AtomicInteger(0);
	
	/**
	 * Funkcja zwraca informacjê o tym, czy powinna zostaæ wykonana aktualizacja
	 * danych
	 * @return informacja o tym czy wykonaæ aktualizacjê danych
	 */
	public boolean doUpdate(boolean b)
	{
		return b;
	}
	
	/**
	 * Funkcja uruchamia potrzebne w¹tki producentów-konsumentów, pozwala im
	 * funkcjonowaæ na przygotowanym wczeœniej buforze, dopóki nie skoñcz¹ wszystkich
	 * powierzonych im zadañ
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
		    scrapperLogger.error("Nast¹pi³o nieoczekiwane wybudzenie w¹tków)",e.getMessage());
	    }
	    
	    scrapperLogger.info("Zakoñczy³em aktualizacjê danych)");
	    scrapperLogger.execute();
	    
		}
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalaj¹cy na zmianê sposobu wysy³ania logów
	 * @param appender obiekt odpowiedzialny za wysy³anie logów
	 */
	BuScrapper(Appends appender)
	{
		scrapperLogger = new Logger();
		scrapperLogger.changeAppender(appender);
	}
	
	/**
	 * Konstruktor domyœlny, ustalaj¹cy typowy sposób wysy³ania logów
	 */
	BuScrapper()
	{
		this(new FileAppender("BuScrapper"));
	}
	
	/**
	 * Przyk³adowy sposób uruchomienia programu
	 * @param args nieu¿ywane
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		
		//Opcjonalna aktualizacja zasobów
		if(b.doUpdate(b.configurator.getUpdateData()))
		{
			b.updateData();
		}	
		
		//Teraz wyszukujemy zgodnie z zaleceniami u¿ytkownika
		b.browser.serch(b.configurator.getToSerach());
		
	}
}
