package pl.edu.agh.kis;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G³ówna klasa programu, jej zadaniem jest odpowiednia obs³uga komponentów bêd¹cych jej
 * komponentami, oddelegowuj¹c im odpowiednie zakresy obowi¹zków oraz inicjalizuj¹c ich
 * stan pocz¹tkowy, udostêpnia metodê pozwalaj¹c¹ na przeprowadzenie aktualizacji danych 
 * poprzez pobranie ich z internetu, w konstruktorach okreœlany jest sposób sk³adowania
 * logów, za wyszukiwanie wiadomoœci oraz okreœlanie czy aktualizacja ma zostaæ przeprowadzona
 * odpowiadaj¹ metody klas kolejno Browser oraz Configurator.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class BuScrapper {

	/**
	 * W³asny system logów
	 */
	private Logger scrapperLogger;
	
	/**
	 * Obiekt obs³uguj¹cy zadania do wykonania okreœlone przez u¿ytkownika
	 */
	private TaskManager tasks = new TaskManager();
	
	/**
	 * Pole przechowuj¹ce obiekt udostêpniaj¹cy przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",tasks);
	
	/**
	 * Bufor dla konsumentów-producentów do przechowywania tymczasowego stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(10);
	
	/**
	 * Obiekt przechowuj¹cy wyszukiwarkê danych
	 */
	private Browser browser = new Browser();
	
	/**
	 * Pole przechowuj¹ce informacjê o iloœci nadal pracuj¹cych w¹tków pobieraj¹cych
	 */
	static AtomicInteger numberOfWorkingDownloadThreads = new AtomicInteger(0);
	
	/**
	 * Pole logiczne sprawdzaj¹ce czy aktualnie wykonywane zadanie jest wci¹¿ wykonaywane
	 * i sprawdzaj¹c wartoœæ pola w odpowiednim momencie, zosta³o wykonane bezb³êdnie
	 */
	static boolean correctTaskExecute;
	
	/**
	 * Funkcja odpowiada za sekwencjê programu przeprowadzaj¹c¹ aktualizacjê danych, na
	 * aktualizacjê sk³adaj¹ siê:
	 * <ol>
	 * <li>Przygotowanie obiektu odpowiedzialnego za tworzenie zapytañ</li>
	 * <li>Najszersza pêtla, obs³uguj¹ca zadania okreœlone w obiektach Task</li>
	 * <li>Pobranie nowego zadania oraz przygotowanie okreœlonych w nim szczegó³ów</li>
	 * <li>Przygotowanie oraz uruchomienie w¹tków pobieraj¹cych oraz wyci¹gaj¹cych dane,
	 * wyposa¿onych w odpowiednie obiekty BuScrappera oraz szczegó³y zadania</li>
	 * <li>Sprawdzenie czy zadanie zosta³o przeprowadzone prawid³owo i mo¿e zostaæ usuniête</li>
	 * </ol>
	 * Ka¿dy w¹tek tworzony w funkcji potrzebuje numeru identyfikacynego oraz okreœlenia
	 * sposobu sk³adowania logów, je¿eli ten drugi nie zostanie wybrany, logi zostaj¹ sk³adowane
	 * w sposób domyœlny. Dodatkowo ka¿dy w¹tek pobieraj¹cy musi otrzymaæ kolejkê zapytañ
	 * do servera przygotowan¹ przez RequestCreator, referencjê do implementacji PagesBuffer,
	 * w której bêdzie umieszcza³ pobrane odpowiedzi od servera oraz obiekt implementuj¹cy
	 * Downloader, który bêdzie odpowiada³ za udostêpnianie strumieni do komunikacji
	 * z serverem. Ka¿dy w¹tek wyszukuj¹cy potrzebuje natomiast referencji do implementacji
	 * PagesBuffer, z której bêdzie pobiera³ odpowiedzi servera, z których nastêpnie bêdzie
	 * wy³uskiwa³ odpowiednie dane, szczegó³y danych otrzymuje poprzez listê wyra¿eñ XPath
	 * utworzonych przez Configurator, który odnajduje je w pliku konfiguracyjnym.
	 */
	public void updateData()
	{
	
		RequestCreator requestCreator = new RequestCreator();
		
		while(tasks.hasNextTask())
		{
			correctTaskExecute = false;
			Task newTask = tasks.getNextTask();
			Set<DownloadThread> downloadThreads = new HashSet<DownloadThread>();
			Set<SnatchThread> snatchThreads = new HashSet<SnatchThread>();
			
			if(requestCreator.isGoodTask(newTask))
			{
				requestCreator.prepareNewRequests(newTask);
				
				for(int i = 0; i < 1; ++i)
				{
					downloadThreads.add(new DownloadThread(i,requestCreator.
							getRequests(),buffer, new SocketDownloader(configurator.
									getStartPageURL())));
					snatchThreads.add(new SnatchThread(i,buffer,new FileStoreBusInfo(
				    		new FileAppender("File Store"+i)),configurator.getXPaths()));
				}
				
			    /*DownloadThread d1 = new DownloadThread(0,requestCreator.getRequests(),buffer,
			    		new SocketDownloader(configurator.getStartPageURL()));
			    DownloadThread d2 = new DownloadThread(1,requestCreator.getRequests(),buffer,
			    		new SocketDownloader(configurator.getStartPageURL()));
			    DownloadThread d3 = new DownloadThread(2,requestCreator.getRequests(),buffer,
			    		new SocketDownloader(configurator.getStartPageURL()));
			    DownloadThread d4 = new DownloadThread(3,requestCreator.getRequests(),buffer,
			    		new SocketDownloader(configurator.getStartPageURL()));
				
			    SnatchThread s1 = new SnatchThread(0,buffer,new FileStoreBusInfo(
			    		new FileAppender("File Store"+0)),configurator.getXPaths());
			    SnatchThread s2 = new SnatchThread(1,buffer,new FileStoreBusInfo(
			    		new FileAppender("File Store"+1)),configurator.getXPaths());
			    SnatchThread s3 = new SnatchThread(2,buffer,new FileStoreBusInfo(
			    		new FileAppender("File Store"+2)),configurator.getXPaths());
			    SnatchThread s4 = new SnatchThread(3,buffer,new FileStoreBusInfo(
			    		new FileAppender("File Store"+3)),configurator.getXPaths());
			    
			    d1.start();
			    d2.start();
			    d3.start();
			    d4.start();
			    s1.start();
			    s2.start();
			    s3.start();
			    s4.start();*/
			    
				for(DownloadThread d : downloadThreads)
				{
					d.start();
				}
				
				for(SnatchThread s : snatchThreads)
				{
					s.start();
				}
				
			    try {
			    
			    /*d1.join();
				d2.join();
				d3.join();
				d4.join();
				s1.join();
				s2.join();
				s3.join();
				s4.join();*/
			    	
			    for(DownloadThread d : downloadThreads)
				{
					d.join();
				}
					
				for(SnatchThread s : snatchThreads)
				{
					s.join();
				}			    	    
			    
			    } catch (InterruptedException e) {
				    scrapperLogger.error("Nast¹pi³o nieoczekiwane wybudzenie w¹tków)",e.getMessage());
			    }
			    
			    if(correctTaskExecute)
			    {
			    	//dodaj task do poprawnie zakoñcoznych
				    scrapperLogger.info("Zakoñczy³em aktualizacjê danych");
			    	//Zadanie zosta³o wykonane poprawnie, mo¿emy je usun¹æ
				    tasks.removeTask(newTask.getId());
			    }
			    
			    BuScrapper.numberOfWorkingDownloadThreads.set(0);
			    requestCreator.clear();
			}
			else
		    {
			    scrapperLogger.warning("Nast¹pi³ problem podczas wykonywania zadania");
			    tasks.incorrectTask(newTask.getId());
		    }
			scrapperLogger.execute();
		}
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalaj¹cy na okreœlenie sposobu sk³adowania logów
	 * poprzez podanie odpowiedniego obiektu w argumencie funkcji.
	 * @param appender obiekt odpowiedzialny za sk³adowanie logów
	 */
	BuScrapper(Appends appender)
	{
		scrapperLogger = new Logger();
		scrapperLogger.changeAppender(appender);
	}
	
	/**
	 * Konstruktor domyœlny, który ustala domyœlny sposób sk³adowania logów.
	 */
	BuScrapper()
	{
		this(new FileAppender("BuScrapper"));
	}
	
	/**
	 * Przyk³adowy sposób uruchomienia programu.
	 * @param args nieu¿ywane
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		
		//Opcjonalna aktualizacja zasobów
		if(b.configurator.getUpdateData())
		{
			b.updateData();
		}	
		
		//Teraz wyszukujemy zgodnie z zaleceniami u¿ytkownika
		b.browser.serch(b.configurator.getToSerach());
	}
}
