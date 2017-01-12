package pl.edu.agh.kis;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest umieszczenie odpowiedniej zawarto�ci w bufofrze
 * metod tej klasy nie interesuje co dalej stanie si� z otrzyman� przez ni� tre�ci�,
 * a problemami kt�re musi pokona� jest wykonywanie odpowiednich zapyta� w celu pozyskania
 * tej tre�ci
 * @author Szymon Majkut
 * @version 1.0
 */
public class DownloadThread extends Thread {

	/**
	 * W�asny system log�w
	 */
	private Logger downloadLogger;
	
	/**
	 * Unikatowa nazwa przyporz�dkowana danemu w�tkowi
	 */
	private String threadName;
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wej�cia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wyj�cia do hosta
	 */
	private OutputStream output;
	
	/**
	 * Referencja do bufora przechowuj�cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	/**
	 * G��wna p�tla w�tku - producenta, jej zadaniem jest oddawanie czasu procesora w przypadku
	 * pe�nego bufora, w przeciwnym wypadku dodawanie do niego strony, kt�ra zostaje pobrana
	 * poprzez odpowiednio nastawiony strumie� wej�cia, je�eli to konieczne, wysy�aj�c strumieniem
	 * wyj�cia odpowiednie zapytanie o zas�b
	 */
	public void run()
	{		
		//Najszersza p�tla do while z warunkiem czy jakiekolwiek w�tki jeszcze pracuj�
		
			//zaznaczamy, �e w�tek pracuje ( np. je�li pozosta� jeszcze jakie� wolne zapytania)
			//BuScrapper.numberOfWorkingThreads.incrementAndGet();
				
			//Najog�lniej dzia�amy z zapytaniem, aby uzyska� zas�b, np. pod��czamy nowe strumienie
		
			//Otrzymujemy stron� i zapisujemy j� w pami�ci lokalnej w�tku
		
			//Wci�� sprawdzamy sprawdzamy czy kolejka pe�na, je�li tak to yield
		
			//Gdy kolejka pozwoli na w�o�enie, to wk�adamy i powtarzamy ca�� akcj�
				
			//po zako�czonej pracy informujemy �e w�tek sko�czy� prac�
			//BuScrapper.numberOfWorkingThreads.decrementAndGet();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku nadrz�dnym
	 */
	DownloadThread(int id)
	{
		threadName = "DownloadThread number " + id;
		downloadLogger = new Logger();
		downloadLogger.changeAppender(new FileAppender(threadName));
	}
}

//Do zrobienia jest pod��czenie tutaj czego�, co b�dzie mia�o przechodzi� po kolejnych
//elementach strony, czy tam zapytania wysy�a� kolejne... nie wiem