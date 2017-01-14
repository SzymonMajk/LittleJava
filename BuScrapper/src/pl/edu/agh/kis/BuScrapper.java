package pl.edu.agh.kis;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * G��wna klasa programu, jej zadaniem jest zaimportowanie potrzebnych plik�w oraz okre�lenie
 * nazwy strony, z kt�rej b�dziemy pobiera�, nast�pnie przygotowanie odpowiedniego bufora
 * dla producent�w oraz konsument�w, nast�pnie utworzenie osobnych w�tk�w producent�w oraz
 * konsument�w, kt�rzy b�d� pobiera� zawarto�ci stron oraz je przetwarza�, oraz obs�ugiwa�
 * ca�o��
 * @author Szymon Majkut
 * @version 1.0
 *
 */
public class BuScrapper {

	/**
	 * W�asny system log�w
	 */
	private Logger scrapperLogger;
	
	/**
	 * Pole przechowuj�ce obiekt udost�pniaj�ce przetworzone dane konfiguracyjne
	 */
	private static Configurator configurator = new Configurator();
	
	/**
	 * Pole przechowuj�ce informacj� o ilo�ci nadal pracuj�cych w�tk�w
	 * jest static, aby ka�dy w�tek m�g� si� do niego �atwo odwo�a�
	 */
	static AtomicInteger numberOfWorkingThreads = new AtomicInteger();
	
	/**
	 * Funkcja przygotowuje ca�o�� aplikacji do dzia�ania, oraz informuje
	 * czy przygotowania przebieg�y pomy�lnie
	 * @param - b�dzie kilka, narazie nic...
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
	 */
	public String work()
	{
		//Utworzenie w�tk�w producent�w na tym etapie musimy zna� nazw� hosta i numer portu
		//no i na tym w�a�nie etapie ju� utworzymy kilka odpowiednich socket�w dla w�tk�w i
		//wy�lemy do nich te sockety, ka�dy w�tek musi posiada� unikalne sockety!
		
		//Utworzenie w�tk�w konsument�w
		
		//Dop�ki wszystkie w�tki pracuj� czekaj na nie
		
		//Rozpisz wnioski i zako�cz dzia�anie
		
		return "BuScrapper nie zosta� jeszcze doko�czony!";
	}
	
	/**
	 * Przyk�adowy spos�b uruchomienia programu
	 * @param args podajemy pliki z konfiguracj� XPath oraz adres strony
	 */
	public static void main(String[] args) {

		BuScrapper b = new BuScrapper();
		if(b.isPrepared())
		{
			System.out.println(b.work());
		}
		else
		{
			System.out.println("Wyst�pi� problem podczas przygotowywania klasy!");
		}	
	}
}
