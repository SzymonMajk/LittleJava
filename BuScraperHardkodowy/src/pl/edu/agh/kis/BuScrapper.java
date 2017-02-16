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
 * G��wna klasa programu, jej zadaniem jest odpowiednia obs�uga komponent�w b�d�cych jej
 * komponentami, oddelegowuj�c im odpowiednie zakresy obowi�zk�w oraz inicjalizuj�c ich
 * stan pocz�tkowy. Posiada Configurator, dzi�ki kt�remu jest w stanie przeprowadzi�
 * polecenia podane przez u�ytkownika, wykonuje logi przy pomocy obiektu Logger, przechowuje
 * implementacj� PagesBuffer, w kt�rej przechowywane s� kody stron, na kt�rych aktualnie
 * operuj� w�tki pobieraj�ce oraz wy�uskuj�ce, oraz obiekt Browser odpowiedzialny
 * za wyszukiwanie po��cze� pomi�dzy liniami. Przechowuje w synchronizowanych polach
 * statycznych informacj� o ilo�ci w�tk�w �ci�gaj�cych oraz w�tk�w wy�uskuj�cych.
 * Wa�niejsze kroki programu s� umieszczane w logach.
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
	 * Obiekt obs�uguj�cy zadania do wykonania okre�lone przez u�ytkownika
	 */
	private TaskManager taskManager = new TaskManager();
	
	/**
	 * Pole przechowuj�ce obiekt udost�pniaj�cy przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",taskManager);
	
	/**
	 * Bufor dla konsument�w-producent�w do przechowywania tymczasowego stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(100);
	
	/**
	 * Obiekt zapewniaj�cy metod�, kt�ra mo�e wykona� przeszukiwania, otrzymuj�c
	 * w argumencie list� obiekt�w Search
	 */
	private Browser browser = new Browser();
	
	/**
	 * Pole przechowuj�ce informacj� o ilo�ci nadal pracuj�cych w�tk�w pobieraj�cych
	 */
	static AtomicInteger numberOfWorkingDownloadThreads = new AtomicInteger(0);
	
	/**
	 * Pole przechowuj�ce informacj� o ilo�ci nadal pracuj�cych w�tk�w wy�uskuj�cych
	 */
	static AtomicInteger numberOfWorkingSnatchThreads = new AtomicInteger(0);
		
	/**
	 * Funkcja odpowiada za sekwencj� programu przeprowadzaj�c� aktualizacj� danych, na
	 * aktualizacj� sk�adaj� si�:
	 * <ol>
	 * <li>Przygotowanie obiektu testuj�cego po��czenia z internetem oraz utworzenie
	 * puli w�tk�w, kt�re b�d� obs�ugiwa� zadania pobierania oraz wy�uskiwania informacji</li>
	 * <li>Nast�pnie wykonywana jest p�tla dop�ki istniej� jeszcze jakie� obiekty Task
	 * do wykonania - jeden obiekt task jest rozumiany jako pobieranie danych jednej
	 * linii</li>
	 * <li>Nast�pnie nast�puje test po��czenia z internetem wykorzystuj�c adres hosta
	 * znajduj�cego si� w aktualnie wykonywanym zadaniu, je�eli po��czenie nie dojdzie
	 * do skutku, zadanie zostaje oddelegowane na koniec kolejki, jako jeszcze nie
	 * rozpocz�te</li>
	 * <li>W przypadku poprawnego po��czenia pula w�tk�w pobieraj�cych oraz wy�uskuj�cych
	 * otrzymuje zadanie przeprowadzenia wyszukiwania danych poprzez zapytania udost�pnione
	 * przez obiekt Task, w przypadku wyst�pienia nieprawid�owo�ci przy wykonywaniu zapyta�
	 * lub niemo�no�ci poprawnego przeprowadzenia zapytania w tym momencie, trafiaj� one
	 * do kolejki zapyta� do p�niejszego wykonania przez Task</li>
	 * <li>W przypadku wyst�pienia zerwania po��czenia z hostem, wszystkie niewykonane
	 * zapytania trafiaj� do kolejki zapyta� do p�niejszego wykonania, a je�eli ta
	 * oka�e si� nie pusta Task trafia na koniec kolejki Task�w, gdyby okaza�a si� pusta
	 * Task zosta�by usuni�ty w obiekcie TaskManager, a wi�c poprawnie zako�czony</li>
	 * </ol>
	 * Ka�dy w�tek pobieraj�cy podczas wykonywania okre�lonego zadania otrzymuje numer
	 * identyfikacyjny, bufor dla pobranych stron, kolejk� blokuj�c� zapyta� do wykonania,
	 * z kt�rej b�dzie pobiera� kolejne zapytania do wykonania, oraz kolejk� blokuj�c�
	 * zapyta� do powt�rzenia, w kt�ej b�dzie umieszcza� zapytania wymagaj�ce ponowienia oraz
	 * obiekt implementuj�cy interfejs Downloader odpowiedzialny za udost�pnienie strumieni
	 * do komunikacji z danym hostem.
	 * Ka�dy w�tek wy�uskuj�cy podczas wykonywania okre�lonego zadania otrzymuje numer
	 * identyfikacyjny, bufor dla pobranych stron, oraz map�, kt�rej kluczami s� nazwy
	 * wyra�e� XPath, z kt�rych wyci�gniemy informacje ze strony, a warto�ciami same
	 * wyra�enia XPath dla danej nazwy. Nazwy te, s� okre�lone przez budow� pliku
	 * konfiguracyjnego, poprawno�� ich wyst�powania jest sprawdzana przez Configurator.
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
			log4j.info("Wyci�gam nowy Task");
				
			if(!connectionTester.testHost(newTask.getHost()))
			{
				if(!newTask.getHost().equals(lastConnectedHost))
				{
					System.out.println("Brak po��czenia z hostem: "+newTask.getHost());
				}
				lastConnectedHost = newTask.getHost();
				taskManager.pushBack(newTask);
				continue;
			}
			else
			{
				System.out.println("Uda�o si� nawi�za� po��czenie z hostem: "
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
						" zosta�a poprawnie zaktualizowana.");
				taskManager.removeTask(newTask.getId());
			}
			else
			{
				System.out.println("Linia "+newTask.getLineNumber()+
						" zostanie doko�czona w nowej turze.");
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
	 * Pierwsza funkcja po uruchomieniu programu. Tworzy nowy obiekt BuScrapper. Nast�pnie
	 * przeprowadza konfiguracj� przy pomocy obiektu sk�adowego nowego obiektu BuScrapper,
	 * w przypadku b��d�w w odczytywaniu pliku konfiguracyjnego, program zako�czy dzia�anie
	 * informuj�c o niepowodzeniu.
	 * Je�eli natomiast konfiguracja przebieg�a pomy�lnie, sprawdza decyzj� u�ytkownika
	 * odno�nie przeprowadzenia aktualizacji, oraz przeprowadzi j�, je�eli u�ytkownik
	 * wyrazi� na to zgod�. Po przej�ciu tego etapu nast�puje wykorzystanie obiektu Browser,
	 * kt�ry przy pomocy listy obiekt�w Search, utworzonych i udost�pnionych przez
	 * Configurator, wykona wyszukiwania podane przez u�ytkownika.
	 * @param args W obecniej implementacji u�ytkownik porozumiewa si� z programem poprzez
	 * 		plik konfiguracyjny, w zwi�zku z czym parametry wywo�ania nie s� u�ywane.
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		
		if(!b.configurator.configure())
		{
			System.out.println("Program nie zosta� poprawnie skonfigurowany");
			System.exit(1);
		}
		
		//Opcjonalna aktualizacja zasob�w
		if(b.configurator.getUpdateData())
		{
			b.updateData();
		}
		
		//Teraz wyszukujemy zgodnie z zaleceniami u�ytkownika
		b.browser.search(b.configurator.getToSerach());
	}
}
