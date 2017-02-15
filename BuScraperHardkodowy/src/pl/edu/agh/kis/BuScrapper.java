package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G³ówna klasa programu, jej zadaniem jest odpowiednia obs³uga komponentów bêd¹cych jej
 * komponentami, oddelegowuj¹c im odpowiednie zakresy obowi¹zków oraz inicjalizuj¹c ich
 * stan pocz¹tkowy, udostêpnia metodê pozwalaj¹c¹ na przeprowadzenie aktualizacji danych 
 * poprzez pobranie ich z internetu, w konstruktorach okreœlany jest sposób sk³adowania
 * logów, za wyszukiwanie wiadomoœci oraz okreœlanie czy aktualizacja ma zostaæ przeprowadzona
 * odpowiadaj¹ metody klas kolejno Browser oraz Configurator.
 * @author Szymon Majkut
 * @version %I%, %G%
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
	 * Pole przechowuj¹ce informacjê o iloœci nadal pracuj¹cych w¹tków wy³uskuj¹cych
	 */
	static AtomicInteger numberOfWorkingSnatchThreads = new AtomicInteger(0);
		
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
		ConnectionTester connectionTester = new ConnectionTester();
		String lastConnectedHost = "";

		ExecutorService downloaders = Executors.newFixedThreadPool(5);
		ExecutorService snatchers = Executors.newFixedThreadPool(5);
		
		while(tasks.hasNextTask())
		{
			Task newTask = tasks.getNextTask();
									
			if(requestCreator.isGoodTask(newTask))
			{
				log4j.info("Wyci¹gam nowy poprawny Task");
				
				if(!connectionTester.testHost(newTask.getHost()))
				{
					if(!newTask.getHost().equals(lastConnectedHost))
					{
						System.out.println("Brak po³¹czenia z hostem: "+newTask.getHost());
					}
					lastConnectedHost = newTask.getHost();
					tasks.pushBack(newTask);
					continue;
				}
				else
				{
					System.out.println("Uda³o siê nawi¹zaæ po³¹czenie z hostem: "
							+ newTask.getHost()+" rozpoczynamy pobieranie danych z linii: "
							+ newTask.getLineNumber());
				}
				
				//TODO dlaczego to task nie mo¿e mieæ tych kolejek po prostu od razu?
				requestCreator.prepareRequests(newTask);
			    
				for(int i = 0; i < 5; ++i)
				{
					//Zamiast request cretora niech dostanie Task, natomiast request creator
					//no to bêdzie sk³adow¹ obiektu task albo tylko jego funkcje
					downloaders.execute(new DownloadThread(i,buffer,requestCreator,
							new SocketDownloader()));
				}
				
				for(int i = 0; i < 2; ++i)
				{
					snatchers.execute(new SnatchThread(i,buffer,new FileStoreBusInfo(),
				    		configurator.getXPaths()));
				}
				
				while(BuScrapper.numberOfWorkingDownloadThreads.get() 
						+ BuScrapper.numberOfWorkingSnatchThreads.get() > 0)
				{
					Thread.yield();
				}
				
				log4j.info("Zadanie siê zakoñczy³o");
				
				//Tutaj dorzucimy do invalid requests, te które nie zd¹¿yliœmy wykonaæ
				//Nieee, trzeba zrobiæ tak, ¿eby taks ju¿ to mia³
				requestCreator.addUnifinishedRequests();
				
				//A to sprwadzenie te¿ w tasku!
				if(requestCreator.isEmptyInvalidRequests())
				{
					System.out.println("Linia "+newTask.getLineNumber()+
							" zosta³a poprawnie zaktualizowana.");
					tasks.removeTask(newTask.getId());
				}
				else
				{
					System.out.println("Linia "+newTask.getLineNumber()+
							" zostanie dokoñczona w nowej turze.");
					newTask.setRequestsToRepeat(requestCreator.getInvalidRequests());
					newTask.setStatus(1);
					tasks.pushBack(newTask);
				}
				
				

			}
			
		    requestCreator.clear();
		}

		downloaders.shutdown();
		snatchers.shutdown();

		while(BuScrapper.numberOfWorkingDownloadThreads.get() 
				+ BuScrapper.numberOfWorkingSnatchThreads.get() > 0)
		{
			Thread.yield();
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
