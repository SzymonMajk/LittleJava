package pl.edu.agh.kis;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Robot - wersja z URL, wielowątkowa z blokiem synchronized na loggerze i
 * specjalnym rodzajem PageCasha z synchronizowanymi metodami oraz z małą blokadą
 * żeby nie było zbyt wiele naraz wątków
 * @author Szymon Majkut
 *
 */
public class ThreadRobot {
	
	/**
	 * Logger, który będziemy synchronizować, aby wątki czekały czy mogą zapisać logi
	 */
		static Logger logs = new Logger();
	
		/**
		 * Tworzę jeden obiekt do kolejki i mapy linków, w celu ułatwienia sobie
		 * synchronizowania dostępu do tych treści - pomocne np. przy implementacji
		 * z bazą danych
		 */
		private PageCash pageCash;
		
		/**
		 * Zmienna przechowująca aktualną ilość pracujących wątków
		 */
		static AtomicInteger numberOfWorkingThreads = new AtomicInteger();

		/**
		 * Stała zawierająca maksymalną ilość pracujących wątków
		 */
		final int MAX_T_NUMBER = 5;

		
		/**
		 * Funkcja odpowiedzialna za rozpoczęcie pozyskiwania linków, podając jako
		 * pierwszy link do przeszukania swój argument. W nieskończonej pętli tworzy
		 * nowe wątki, które jeżeli kolejka nie jest pusta, pobierają linki ze stron
		 * oraz zapisują do logów wiadomość z jakiego linku akurat pobierały
		 * @param site początkowy adres do wyszukania linków
		 */
		public void start(String site)
		{
			/*Przeszukujemy pierwszą ze stron, żeby pozyskać kolejne*/
			pageCash.addPage(site);
			
		    for(int i = 0; i < MAX_T_NUMBER; ++i) {
		    	
		    	try {
					new ThreadDownloader(pageCash,i).start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
			
			while (ThreadRobot.numberOfWorkingThreads.intValue() > 0) {
				//Działanie programu...
			}
		
		}
		
		/**
		 * Konstruktor ustalający rodzaj składowania logów
		 * @param c implementacja składowania logów
		 */
		ThreadRobot(PageCash c)
		{
			pageCash = c;
			//do systemu logów
			logs.changeAppender(new FileAppender("WebCrawlerLogs"));
		}
		
		/**
		 * Punkt startowy i test aplikacji
		 * @param args nieużywane
		 */
		public static void main(String[] args) throws MalformedURLException, DownloaderException, ClassNotFoundException, SQLException, InterruptedException {
			
			ThreadRobot r = new ThreadRobot(new SimplePageCash());
			r.start("http://kis.agh.edu.pl/");
			System.out.println("Koniec!");
			
		}
}