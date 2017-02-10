package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
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
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(BuScrapper.class.getName());
	
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
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(100);
	
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
	static AtomicBoolean correctTaskExecute = new AtomicBoolean();
	
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
			
			//TODO taki ma³y obiekcik, który bêdzie sprawdza³ czy jest po³¹czenie internetowe
			//z URL podanym w tasku, póki nie bêdzie to przerzuca to zadanie na koniec
			//i idzie do nastêpnego zadania powtarza dla nowego URL, ale ale! Wypisuje
			//informajê o tym ¿e nie ma internetu tylko przy pierwszym, ¿eby nie zaspamiæ logów
			correctTaskExecute.set(false);
			Task newTask = tasks.getNextTask();
			Set<DownloadThread> downloadThreads = new HashSet<DownloadThread>();
			Set<SnatchThread> snatchThreads = new HashSet<SnatchThread>();
			
			//TODO nie no, musimy zrobiæ to pul¹ w¹tków...
			
			if(requestCreator.isGoodTask(newTask))
			{
				requestCreator.prepareNewRequests(newTask);
				
				for(int i = 0; i < 5; ++i)
				{
					//TODO no niech on nie dostaje startowego, tylko ten z zadania!
					downloadThreads.add(new DownloadThread(i,requestCreator.
							getRequests(),buffer, new SocketDownloader(configurator.
									getStartPageURL())));
					snatchThreads.add(new SnatchThread(i,buffer,new FileStoreBusInfo(),
				    		configurator.getXPaths()));
				}
			    
				for(DownloadThread d : downloadThreads)
				{
					d.start();
				}
				
				for(SnatchThread s : snatchThreads)
				{
					s.start();
				}
				
			    try {
			    
				    for(DownloadThread d : downloadThreads)
					{
						d.join();
					}
						
					for(SnatchThread s : snatchThreads)
					{
						s.join();
					}			    	    
			    
			    } catch (InterruptedException e) {
			    	log4j.error("Nast¹pi³o nieoczekiwane wybudzenie w¹tków)",e.getMessage());
			    }
			    
			    if(correctTaskExecute.get())
			    {
			    	//dodaj szczegó³y tasku, np. id
			    	log4j.trace("Poprawnie zakoñczy³em Task");
			    	//Zadanie zosta³o wykonane poprawnie, mo¿emy je usun¹æ
				    tasks.removeTask(newTask.getId());
			    }
			}
			//Czyszczenie przed kolejn¹ iteracj¹
		    BuScrapper.numberOfWorkingDownloadThreads.set(0);
		    requestCreator.clear();
		}
	}
	
	/**
	 * 
	 * @param args
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
