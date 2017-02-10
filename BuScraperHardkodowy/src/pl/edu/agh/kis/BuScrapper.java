package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
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
	 * Pole logiczne sprawdzaj�ce czy aktualnie wykonywane zadanie jest wci�� wykonaywane
	 * i sprawdzaj�c warto�� pola w odpowiednim momencie, zosta�o wykonane bezb��dnie
	 */
	static AtomicBoolean correctTaskExecute = new AtomicBoolean();
	
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
			
			//TODO taki ma�y obiekcik, kt�ry b�dzie sprawdza� czy jest po��czenie internetowe
			//z URL podanym w tasku, p�ki nie b�dzie to przerzuca to zadanie na koniec
			//i idzie do nast�pnego zadania powtarza dla nowego URL, ale ale! Wypisuje
			//informaj� o tym �e nie ma internetu tylko przy pierwszym, �eby nie zaspami� log�w
			correctTaskExecute.set(false);
			Task newTask = tasks.getNextTask();
			Set<DownloadThread> downloadThreads = new HashSet<DownloadThread>();
			Set<SnatchThread> snatchThreads = new HashSet<SnatchThread>();
			
			//TODO nie no, musimy zrobi� to pul� w�tk�w...
			
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
			    	log4j.error("Nast�pi�o nieoczekiwane wybudzenie w�tk�w)",e.getMessage());
			    }
			    
			    if(correctTaskExecute.get())
			    {
			    	//dodaj szczeg�y tasku, np. id
			    	log4j.trace("Poprawnie zako�czy�em Task");
			    	//Zadanie zosta�o wykonane poprawnie, mo�emy je usun��
				    tasks.removeTask(newTask.getId());
			    }
			}
			//Czyszczenie przed kolejn� iteracj�
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
		
		//Opcjonalna aktualizacja zasob�w
		if(b.configurator.getUpdateData())
		{
			b.updateData();
		}	
		
		//Teraz wyszukujemy zgodnie z zaleceniami u�ytkownika
		b.browser.serch(b.configurator.getToSerach());
	}
}
