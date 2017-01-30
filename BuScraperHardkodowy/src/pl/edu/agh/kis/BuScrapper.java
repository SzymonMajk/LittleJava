package pl.edu.agh.kis;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G��wna klasa programu, jej zadaniem jest zaimportowanie potrzebnych plik�w oraz okre�lenie
 * nazwy strony, z kt�rej b�dziemy pobiera�, nast�pnie przygotowanie odpowiedniego bufora
 * dla producent�w oraz konsument�w, nast�pnie utworzenie osobnych w�tk�w producent�w oraz
 * konsument�w, kt�rzy b�d� pobiera� zawarto�ci stron oraz je przetwarza�, oraz obs�ugiwa�
 * ca�o��
 * @author Szymon Majkut
 * @version 1.1a
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
	 * Pole prywatne przechowuj�ce obiekt odpowiedzialny za zapisywanie gotowych wynik�w
	 */
	private StoreBusInfo infoSaver = new FileStoreBusInfo();
	
	/**
	 * Pole przechowuj�ce informacj� o ilo�ci nadal pracuj�cych w�tk�w
	 * jest static, aby ka�dy w�tek m�g� si� do niego �atwo odwo�a�
	 */
	static AtomicInteger numberOfWorkingThreads = new AtomicInteger(0);
	
	/**
	 * Funkcja przygotowuje ca�o�� aplikacji do dzia�ania, oraz informuje
	 * czy przygotowania przebieg�y pomy�lnie
	 * @return informacja o tym, czy obiekt zosta� w�a�ciwie przygotowany
	 */
	public boolean isPrepared()
	{
		scrapperLogger = new Logger();
		scrapperLogger.changeAppender(new FileAppender("BuScrapper"));
		
		return true;
	}
	
	/**
	 * Funkcja uruchamia potrzebne w�tki producent�w-konsument�w, pozwala im
	 * funkcjonowa� na przygotowanym wcze�niej buforze, dop�ki nie sko�cz� wszystkich
	 * zada� oraz informuje o zako�czeniu pracy
	 * @return tekst ko�cz�cy prac�, informuje przy tym o zaistnia�ych mo�liwo�ciach
	 * @throws IOException B��d zwi�zany z korzystaniem z URL oraz Socketu
	 * @throws UnknownHostException Gdy podamy z�ego hosta
	 * @throws InterruptedException W przypadku gdyby nie uda�o si� zrobi� join()
	 */
	public String work() throws UnknownHostException, IOException, InterruptedException
	{
		//Utworzenie w�tk�w producent�w na tym etapie musimy zna� nazw� hosta i numer portu
		//no i na tym w�a�nie etapie ju� utworzymy kilka odpowiednich socket�w dla w�tk�w i
		//wy�lemy do nich te sockety, ka�dy w�tek musi posiada� unikalne sockety!
		
		String pageURL = "http://rozklady.mpk.krakow.pl/";

		while(!tasks.isEmpty())
		{
		
	    DownloadThread d1 = new DownloadThread(0,requestCreator.getRequests(),buffer,
	    		new SocketDownloader(pageURL));
		
		//Utworzenie w�tk�w konsument�w
		
	    SnatchThread s1 = new SnatchThread(0,buffer,infoSaver,configurator.getXPaths());
		     
		//Dop�ki wszystkie w�tki pracuj� czekaj na nie
	    d1.start();
	    s1.start();
	    
	    d1.join();
	    s1.join();
		//Rozpisz wnioski i zako�cz dzia�anie
	    
		}
	    
		return "Wnioski... Zerknij do katalogu i sprawdz katalogi!";
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
	 * @param args podajemy pliki z konfiguracj� XPath oraz adres strony
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		if(b.isPrepared())
		{
			try {
				System.out.println(b.work());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Wyst�pi� problem podczas przygotowywania klasy!");
		}	
	}
}
