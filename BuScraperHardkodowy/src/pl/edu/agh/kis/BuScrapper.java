package pl.edu.agh.kis;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G��wna klasa programu, jej zadaniem jest odpowiednia obs�uga komponent�w b�d�cych jej
 * komponentami, oddelegowuj�c im odpowiednie zakresy obowi�zk�w oraz inicjalizuj�c ich
 * stan pocz�tkowy, udost�pnia metod� pozwalaj�c� na przeprowadzenie aktualizacji danych 
 * poprzez pobranie ich z internetu, w konstruktorach okre�lany jest spos�b sk�adowania
 * log�w, za wyszukiwanie wiadomo�ci oraz okre�lanie czy aktualizacja ma zosta� przeprowadzona
 * odpowiadaj� metody klas kolejno Browser oraz Configurator.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class BuScrapper {

	/**
	 * W�asny system log�w
	 */
	private Logger scrapperLogger;
	
	/**
	 * Obiekt obs�uguj�cy zadania do wykonania okre�lone przez u�ytkownika
	 */
	private TaskManager tasks = new TaskManager();
	
	/**
	 * Pole przechowuj�ce obiekt udost�pniaj�cy przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",tasks);
	
	/**
	 * Bufor dla konsument�w-producent�w do przechowywania tymczasowego stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(10);
	
	/**
	 * Obiekt przechowuj�cy wyszukiwark� danych
	 */
	private Browser browser = new Browser();
	
	/**
	 * Pole przechowuj�ce informacj� o ilo�ci nadal pracuj�cych w�tk�w pobieraj�cych
	 */
	static AtomicInteger numberOfWorkingDownloadThreads = new AtomicInteger(0);
	
	/**
	 * Pole logiczne sprawdzaj�ce czy aktualnie wykonywane zadanie jest wci�� wykonaywane
	 * i sprawdzaj�c warto�� pola w odpowiednim momencie, zosta�o wykonane bezb��dnie
	 */
	static boolean correctTaskExecute;
	
	/**
	 * Funkcja odpowiada za sekwencj� programu przeprowadzaj�c� aktualizacj� danych, na
	 * aktualizacj� sk�adaj� si�:
	 * <ol>
	 * <li>Przygotowanie obiektu odpowiedzialnego za tworzenie zapyta�</li>
	 * <li>Najszersza p�tla, obs�uguj�ca zadania okre�lone w obiektach Task</li>
	 * <li>Pobranie nowego zadania oraz przygotowanie okre�lonych w nim szczeg��w</li>
	 * <li>Przygotowanie oraz uruchomienie w�tk�w pobieraj�cych oraz wyci�gaj�cych dane,
	 * wyposa�onych w odpowiednie obiekty BuScrappera oraz szczeg�y zadania</li>
	 * <li>Sprawdzenie czy zadanie zosta�o przeprowadzone prawid�owo i mo�e zosta� usuni�te</li>
	 * </ol>
	 * Ka�dy w�tek tworzony w funkcji potrzebuje numeru identyfikacynego oraz okre�lenia
	 * sposobu sk�adowania log�w, je�eli ten drugi nie zostanie wybrany, logi zostaj� sk�adowane
	 * w spos�b domy�lny. Dodatkowo ka�dy w�tek pobieraj�cy musi otrzyma� kolejk� zapyta�
	 * do servera przygotowan� przez RequestCreator, referencj� do implementacji PagesBuffer,
	 * w kt�rej b�dzie umieszcza� pobrane odpowiedzi od servera oraz obiekt implementuj�cy
	 * Downloader, kt�ry b�dzie odpowiada� za udost�pnianie strumieni do komunikacji
	 * z serverem. Ka�dy w�tek wyszukuj�cy potrzebuje natomiast referencji do implementacji
	 * PagesBuffer, z kt�rej b�dzie pobiera� odpowiedzi servera, z kt�rych nast�pnie b�dzie
	 * wy�uskiwa� odpowiednie dane, szczeg�y danych otrzymuje poprzez list� wyra�e� XPath
	 * utworzonych przez Configurator, kt�ry odnajduje je w pliku konfiguracyjnym.
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
				    scrapperLogger.error("Nast�pi�o nieoczekiwane wybudzenie w�tk�w)",e.getMessage());
			    }
			    
			    if(correctTaskExecute)
			    {
			    	//dodaj task do poprawnie zako�coznych
				    scrapperLogger.info("Zako�czy�em aktualizacj� danych");
			    	//Zadanie zosta�o wykonane poprawnie, mo�emy je usun��
				    tasks.removeTask(newTask.getId());
			    }
			    
			    BuScrapper.numberOfWorkingDownloadThreads.set(0);
			    requestCreator.clear();
			}
			else
		    {
			    scrapperLogger.warning("Nast�pi� problem podczas wykonywania zadania");
			    tasks.incorrectTask(newTask.getId());
		    }
			scrapperLogger.execute();
		}
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalaj�cy na okre�lenie sposobu sk�adowania log�w
	 * poprzez podanie odpowiedniego obiektu w argumencie funkcji.
	 * @param appender obiekt odpowiedzialny za sk�adowanie log�w
	 */
	BuScrapper(Appends appender)
	{
		scrapperLogger = new Logger();
		scrapperLogger.changeAppender(appender);
	}
	
	/**
	 * Konstruktor domy�lny, kt�ry ustala domy�lny spos�b sk�adowania log�w.
	 */
	BuScrapper()
	{
		this(new FileAppender("BuScrapper"));
	}
	
	/**
	 * Przyk�adowy spos�b uruchomienia programu.
	 * @param args nieu�ywane
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		
		//Opcjonalna aktualizacja zasob�w
		if(b.configurator.getUpdateData())
		{
			b.updateData();
		}	
		
		//Teraz wyszukujemy zgodnie z zaleceniami u�ytkownika
		b.browser.serch(b.configurator.getToSerach());
	}
}
