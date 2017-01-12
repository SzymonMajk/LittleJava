package pl.edu.agh.kis;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * G³ówna klasa programu, jej zadaniem jest zaimportowanie potrzebnych plików oraz okreœlenie
 * nazwy strony, z której bêdziemy pobieraæ, nastêpnie przygotowanie odpowiedniego bufora
 * dla producentów oraz konsumentów, nastêpnie utworzenie osobnych w¹tków producentów oraz
 * konsumentów, którzy bêd¹ pobieraæ zawartoœci stron oraz je przetwarzaæ, oraz obs³ugiwaæ
 * ca³oœæ
 * @author Szymon Majkut
 * @version 1.0
 *
 */
public class BuScrapper {

	/**
	 * W³asny system logów
	 */
	private Logger scrapperLogger;
	
	/**
	 * Pole przechowuj¹ce informacjê o iloœci nadal pracuj¹cych w¹tków
	 * jest static, aby ka¿dy w¹tek móg³ siê do niego ³atwo odwo³aæ
	 */
	static AtomicInteger numberOfWorkingThreads = new AtomicInteger();
	
	/**
	 * Funkcja przygotowuje ca³oœæ aplikacji do dzia³ania, oraz informuje
	 * czy przygotowania przebieg³y pomyœlnie
	 * @param - bêdzie kilka, narazie nic...
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
	 */
	public String work()
	{
		//Utworzenie w¹tków producentów
		
		//Utworzenie w¹tków konsumentów
		
		//Dopóki wszystkie w¹tki pracuj¹ czekaj na nie
		
		//Rozpisz wnioski i zakoñcz dzia³anie
		
		return "BuScrapper nie zosta³ jeszcze dokoñczony!";
	}
	
	/**
	 * Przyk³adowy sposób uruchomienia programu
	 * @param args podajemy pliki z konfiguracj¹ XPath oraz adres strony
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		if(b.isPrepared())
		{
			System.out.println(b.work());
		}
		else
		{
			System.out.println("Wyst¹pi³ problem podczas przygotowywania klasy!");
		}	
	}
}
