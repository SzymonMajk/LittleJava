package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G��wna klasa programu, jej zadaniem jest odpowiednia obs�uga komponent�w b�d�cych jej
 * komponentami, oddelegowuj�c im odpowiednie zakresy obowi�zk�w oraz inicjalizuj�c ich
 * stan pocz�tkowy, udost�pnia metod� pozwalaj�c� na przeprowadzenie aktualizacji danych 
 * poprzez pobranie ich z internetu, w konstruktorach okre�lany jest spos�b sk�adowania
 * log�w, za wyszukiwanie wiadomo�ci oraz okre�lanie czy aktualizacja ma zosta� przeprowadzona
 * odpowiadaj� metody klas kolejno Browser oraz Configurator.
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
	private TaskManager tasks = new TaskManager();
	
	/**
	 * Pole przechowuj�ce obiekt udost�pniaj�cy przetworzone dane konfiguracyjne
	 */
	private Configurator configurator = new Configurator("Conf/Conf",tasks);
	
	/**
	 * Bufor dla konsument�w-producent�w do przechowywania tymczasowego stron do przerobienia
	 */
	private PagesBuffer buffer = new BlockingQueuePagesBuffer(100);
	
	/**
	 * Obiekt przechowuj�cy wyszukiwark� danych
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
		ConnectionTester connectionTester = new ConnectionTester();
		String lastConnectedHost = "";

		ExecutorService downloaders = Executors.newFixedThreadPool(5);
		ExecutorService snatchers = Executors.newFixedThreadPool(5);
		
		while(tasks.hasNextTask())
		{
			Task newTask = tasks.getNextTask();
									
			if(requestCreator.isGoodTask(newTask))
			{
				log4j.info("Wyci�gam nowy poprawny Task");
				
				if(!connectionTester.testHost(newTask.getHost()))
				{
					if(!newTask.getHost().equals(lastConnectedHost))
					{
						System.out.println("Brak po��czenia z hostem: "+newTask.getHost());
					}
					lastConnectedHost = newTask.getHost();
					tasks.pushBack(newTask);
					continue;
				}
				else
				{
					System.out.println("Uda�o si� nawi�za� po��czenie z hostem: "
							+ newTask.getHost()+" rozpoczynamy pobieranie danych z linii: "
							+ newTask.getLineNumber());
				}
				
				//TODO dlaczego to task nie mo�e mie� tych kolejek po prostu od razu?
				requestCreator.prepareRequests(newTask);
			    
				for(int i = 0; i < 5; ++i)
				{
					//Zamiast request cretora niech dostanie Task, natomiast request creator
					//no to b�dzie sk�adow� obiektu task albo tylko jego funkcje
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
				
				log4j.info("Zadanie si� zako�czy�o");
				
				//Tutaj dorzucimy do invalid requests, te kt�re nie zd��yli�my wykona�
				//Nieee, trzeba zrobi� tak, �eby taks ju� to mia�
				requestCreator.addUnifinishedRequests();
				
				//A to sprwadzenie te� w tasku!
				if(requestCreator.isEmptyInvalidRequests())
				{
					System.out.println("Linia "+newTask.getLineNumber()+
							" zosta�a poprawnie zaktualizowana.");
					tasks.removeTask(newTask.getId());
				}
				else
				{
					System.out.println("Linia "+newTask.getLineNumber()+
							" zostanie doko�czona w nowej turze.");
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
		
		//Opcjonalna aktualizacja zasob�w
		if(b.configurator.getUpdateData())
		{
			b.updateData();
		}
		
		//Teraz wyszukujemy zgodnie z zaleceniami u�ytkownika
		b.browser.serch(b.configurator.getToSerach());
	}
}
