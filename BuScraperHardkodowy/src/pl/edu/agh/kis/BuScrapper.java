package pl.edu.agh.kis;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * G³ówna klasa programu, jej zadaniem jest zaimportowanie potrzebnych plików oraz okreœlenie
 * nazwy strony, z której bêdziemy pobieraæ, nastêpnie przygotowanie odpowiedniego bufora
 * dla producentów oraz konsumentów, nastêpnie utworzenie osobnych w¹tków producentów oraz
 * konsumentów, którzy bêd¹ pobieraæ zawartoœci stron oraz je przetwarzaæ, oraz obs³ugiwaæ
 * ca³oœæ
 * @author Szymon Majkut
 * @version 1.1a
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
	 * Pole prywatne przechowuj¹ce obiekt odpowiedzialny za zapisywanie gotowych wyników
	 */
	private StoreBusInfo infoSaver = new FileStoreBusInfo();
	
	/**
	 * Pole przechowuj¹ce informacjê o iloœci nadal pracuj¹cych w¹tków
	 * jest static, aby ka¿dy w¹tek móg³ siê do niego ³atwo odwo³aæ
	 */
	static AtomicInteger numberOfWorkingThreads = new AtomicInteger(0);
	
	/**
	 * Funkcja przygotowuje ca³oœæ aplikacji do dzia³ania, oraz informuje
	 * czy przygotowania przebieg³y pomyœlnie
	 * @return informacja o tym, czy obiekt zosta³ w³aœciwie przygotowany
	 */
	public boolean isPrepared()
	{
		scrapperLogger = new Logger();
		scrapperLogger.changeAppender(new FileAppender("BuScrapper"));
		
		return true;
	}
	
	/**
	 * Funkcja uruchamia potrzebne w¹tki producentów-konsumentów, pozwala im
	 * funkcjonowaæ na przygotowanym wczeœniej buforze, dopóki nie skoñcz¹ wszystkich
	 * zadañ oraz informuje o zakoñczeniu pracy
	 * @return tekst koñcz¹cy pracê, informuje przy tym o zaistnia³ych mo¿liwoœciach
	 * @throws IOException B³¹d zwi¹zany z korzystaniem z URL oraz Socketu
	 * @throws UnknownHostException Gdy podamy z³ego hosta
	 * @throws InterruptedException W przypadku gdyby nie uda³o siê zrobiæ join()
	 */
	public String work() throws UnknownHostException, IOException, InterruptedException
	{
		//Utworzenie w¹tków producentów na tym etapie musimy znaæ nazwê hosta i numer portu
		//no i na tym w³aœnie etapie ju¿ utworzymy kilka odpowiednich socketów dla w¹tków i
		//wyœlemy do nich te sockety, ka¿dy w¹tek musi posiadaæ unikalne sockety!
		
		String pageURL = "http://rozklady.mpk.krakow.pl/";

		while(!tasks.isEmpty())
		{
		
	    DownloadThread d1 = new DownloadThread(0,requestCreator.getRequests(),buffer,
	    		new SocketDownloader(pageURL));
		
		//Utworzenie w¹tków konsumentów
		
	    SnatchThread s1 = new SnatchThread(0,buffer,infoSaver,configurator.getXPaths());
		     
		//Dopóki wszystkie w¹tki pracuj¹ czekaj na nie
	    d1.start();
	    s1.start();
	    
	    d1.join();
	    s1.join();
		//Rozpisz wnioski i zakoñcz dzia³anie
	    
		}
	    
		return "Wnioski... Zerknij do katalogu i sprawdz katalogi!";
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
	 * @param args podajemy pliki z konfiguracj¹ XPath oraz adres strony
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
			System.out.println("Wyst¹pi³ problem podczas przygotowywania klasy!");
		}	
	}
}
