package pl.edu.agh.kis;

import pl.edu.agh.kis.configuration.Configurator;
import pl.edu.agh.kis.search.Browser;
import pl.edu.agh.kis.storeinfo.FileStoreBusInfo;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G³ówna klasa programu, jej zadaniem jest odpowiednia obs³uga komponentów bêd¹cych jej
 * komponentami, oddelegowuj¹c im odpowiednie zakresy obowi¹zków oraz inicjalizuj¹c ich
 * stan pocz¹tkowy. Posiada Configurator, dziêki któremu jest w stanie przeprowadziæ
 * polecenia podane przez u¿ytkownika, wykonuje logi przy pomocy obiektu Logger, przechowuje
 * implementacjê PagesBuffer, w której przechowywane s¹ kody stron, na których aktualnie
 * operuj¹ w¹tki pobieraj¹ce oraz wy³uskuj¹ce, oraz obiekt Browser odpowiedzialny
 * za wyszukiwanie po³¹czeñ pomiêdzy liniami. Przechowuje w synchronizowanych polach
 * statycznych informacjê o iloœci w¹tków œci¹gaj¹cych oraz w¹tków wy³uskuj¹cych.
 * Wa¿niejsze kroki programu s¹ umieszczane w logach.
 * 
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
	private TaskManager taskManager = new TaskManager();
	
	/**
	 * Pole przechowuj¹ce obiekt udostêpniaj¹cy przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",taskManager);
	
	/**
	 * Bufor dla konsumentów-producentów do przechowywania tymczasowego stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(100);
	
	/**
	 * Obiekt zapewniaj¹cy metodê, która mo¿e wykonaæ przeszukiwania, otrzymuj¹c
	 * w argumencie listê obiektów Search
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
	 * <li>Przygotowanie obiektu testuj¹cego po³¹czenia z internetem oraz utworzenie
	 * puli w¹tków, które bêd¹ obs³ugiwaæ zadania pobierania oraz wy³uskiwania informacji</li>
	 * <li>Nastêpnie wykonywana jest pêtla dopóki istniej¹ jeszcze jakieœ obiekty Task
	 * do wykonania - jeden obiekt task jest rozumiany jako pobieranie danych jednej
	 * linii</li>
	 * <li>Nastêpnie nastêpuje test po³¹czenia z internetem wykorzystuj¹c adres hosta
	 * znajduj¹cego siê w aktualnie wykonywanym zadaniu, je¿eli po³¹czenie nie dojdzie
	 * do skutku, zadanie zostaje oddelegowane na koniec kolejki, jako jeszcze nie
	 * rozpoczête</li>
	 * <li>W przypadku poprawnego po³¹czenia pula w¹tków pobieraj¹cych oraz wy³uskuj¹cych
	 * otrzymuje zadanie przeprowadzenia wyszukiwania danych poprzez zapytania udostêpnione
	 * przez obiekt Task, w przypadku wyst¹pienia nieprawid³owoœci przy wykonywaniu zapytañ
	 * lub niemo¿noœci poprawnego przeprowadzenia zapytania w tym momencie, trafiaj¹ one
	 * do kolejki zapytañ do póŸniejszego wykonania przez Task</li>
	 * <li>W przypadku wyst¹pienia zerwania po³¹czenia z hostem, wszystkie niewykonane
	 * zapytania trafiaj¹ do kolejki zapytañ do póŸniejszego wykonania, a je¿eli ta
	 * oka¿e siê nie pusta Task trafia na koniec kolejki Tasków, gdyby okaza³a siê pusta
	 * Task zosta³by usuniêty w obiekcie TaskManager, a wiêc poprawnie zakoñczony</li>
	 * </ol>
	 * Ka¿dy w¹tek pobieraj¹cy podczas wykonywania okreœlonego zadania otrzymuje numer
	 * identyfikacyjny, bufor dla pobranych stron, kolejkê blokuj¹c¹ zapytañ do wykonania,
	 * z której bêdzie pobiera³ kolejne zapytania do wykonania, oraz kolejkê blokuj¹c¹
	 * zapytañ do powtórzenia, w któej bêdzie umieszcza³ zapytania wymagaj¹ce ponowienia oraz
	 * obiekt implementuj¹cy interfejs Downloader odpowiedzialny za udostêpnienie strumieni
	 * do komunikacji z danym hostem.
	 * Ka¿dy w¹tek wy³uskuj¹cy podczas wykonywania okreœlonego zadania otrzymuje numer
	 * identyfikacyjny, bufor dla pobranych stron, oraz mapê, której kluczami s¹ nazwy
	 * wyra¿eñ XPath, z których wyci¹gniemy informacje ze strony, a wartoœciami same
	 * wyra¿enia XPath dla danej nazwy. Nazwy te, s¹ okreœlone przez budowê pliku
	 * konfiguracyjnego, poprawnoœæ ich wystêpowania jest sprawdzana przez Configurator.
	 */
	public void updateData()
	{
		ConnectionTester connectionTester = new ConnectionTester();
		String lastConnectedHost = "";

		ExecutorService downloaders = Executors.newFixedThreadPool(5);
		ExecutorService snatchers = Executors.newFixedThreadPool(2);
		
		while(taskManager.hasNextTask())
		{
			Task newTask = taskManager.getNextTask();
			log4j.info("Wyci¹gam nowy Task");
				
			if(!connectionTester.testHost(newTask.getHost()))
			{
				if(!newTask.getHost().equals(lastConnectedHost))
				{
					System.out.println("Brak po³¹czenia z hostem: "+newTask.getHost());
				}
				lastConnectedHost = newTask.getHost();
				taskManager.pushBack(newTask);
				continue;
			}
			else
			{
				System.out.println("Uda³o siê nawi¹zaæ po³¹czenie z hostem: "
						+ newTask.getHost()+" rozpoczynam pobieranie danych z linii: "
						+ newTask.getLineNumber());
				newTask.prepareRequestsBeforeUsing();
			}
			
			for(int i = 0; i < 5; ++i)
			{
				downloaders.execute(new DownloadThread(i,buffer,newTask.getRequestsToDo(),
						newTask.getRequestsToRepeat(),new SocketDownloader()));
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
				
			if(newTask.isEmptyRequestsQueues())
			{
				System.out.println("Linia "+newTask.getLineNumber()+
						" zosta³a poprawnie zaktualizowana.");
				taskManager.removeTask(newTask.getId());
			}
			else
			{
				System.out.println("Linia "+newTask.getLineNumber()+
						" zostanie dokoñczona w nowej turze.");
				taskManager.pushBack(newTask);
			}
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
	 * Pierwsza funkcja po uruchomieniu programu. Tworzy nowy obiekt BuScrapper. Nastêpnie
	 * przeprowadza konfiguracjê przy pomocy obiektu sk³adowego nowego obiektu BuScrapper,
	 * w przypadku b³êdów w odczytywaniu pliku konfiguracyjnego, program zakoñczy dzia³anie
	 * informuj¹c o niepowodzeniu.
	 * Je¿eli natomiast konfiguracja przebieg³a pomyœlnie, sprawdza decyzjê u¿ytkownika
	 * odnoœnie przeprowadzenia aktualizacji, oraz przeprowadzi j¹, je¿eli u¿ytkownik
	 * wyrazi³ na to zgodê. Po przejœciu tego etapu nastêpuje wykorzystanie obiektu Browser,
	 * który przy pomocy listy obiektów Search, utworzonych i udostêpnionych przez
	 * Configurator, wykona wyszukiwania podane przez u¿ytkownika.
	 * @param args W obecniej implementacji u¿ytkownik porozumiewa siê z programem poprzez
	 * 		plik konfiguracyjny, w zwi¹zku z czym parametry wywo³ania nie s¹ u¿ywane.
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		
		if(!b.configurator.configure())
		{
			System.out.println("Program nie zosta³ poprawnie skonfigurowany");
			System.exit(1);
		}
		
		//Opcjonalna aktualizacja zasobów
		if(b.configurator.getUpdateData())
		{
			b.updateData();
		}
		
		//Teraz wyszukujemy zgodnie z zaleceniami u¿ytkownika
		b.browser.search(b.configurator.getToSerach());
	}
}
