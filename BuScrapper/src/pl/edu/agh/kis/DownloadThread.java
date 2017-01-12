package pl.edu.agh.kis;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa w¹tków, których zadaniem jest umieszczenie odpowiedniej zawartoœci w bufofrze
 * metod tej klasy nie interesuje co dalej stanie siê z otrzyman¹ przez ni¹ treœci¹,
 * a problemami które musi pokonaæ jest wykonywanie odpowiednich zapytañ w celu pozyskania
 * tej treœci
 * @author Szymon Majkut
 * @version 1.0
 */
public class DownloadThread extends Thread {

	/**
	 * W³asny system logów
	 */
	private Logger downloadLogger;
	
	/**
	 * Unikatowa nazwa przyporz¹dkowana danemu w¹tkowi
	 */
	private String threadName;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wejœcia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wyjœcia do hosta
	 */
	private OutputStream output;
	
	/**
	 * Referencja do bufora przechowuj¹cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	/**
	 * G³ówna pêtla w¹tku - producenta, jej zadaniem jest oddawanie czasu procesora w przypadku
	 * pe³nego bufora, w przeciwnym wypadku dodawanie do niego strony, która zostaje pobrana
	 * poprzez odpowiednio nastawiony strumieñ wejœcia, je¿eli to konieczne, wysy³aj¹c strumieniem
	 * wyjœcia odpowiednie zapytanie o zasób
	 */
	public void run()
	{		
		//Najszersza pêtla do while z warunkiem czy jakiekolwiek w¹tki jeszcze pracuj¹
		
			//zaznaczamy, ¿e w¹tek pracuje ( np. jeœli pozosta³ jeszcze jakieœ wolne zapytania)
			//BuScrapper.numberOfWorkingThreads.incrementAndGet();
				
			//Najogólniej dzia³amy z zapytaniem, aby uzyskaæ zasób, np. pod³¹czamy nowe strumienie
		
			//Otrzymujemy stronê i zapisujemy j¹ w pamiêci lokalnej w¹tku
		
			//Wci¹¿ sprawdzamy sprawdzamy czy kolejka pe³na, jeœli tak to yield
		
			//Gdy kolejka pozwoli na w³o¿enie, to wk³adamy i powtarzamy ca³¹ akcjê
				
			//po zakoñczonej pracy informujemy ¿e w¹tek skoñczy³ pracê
			//BuScrapper.numberOfWorkingThreads.decrementAndGet();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku nadrzêdnym
	 */
	DownloadThread(int id)
	{
		threadName = "DownloadThread number " + id;
		downloadLogger = new Logger();
		downloadLogger.changeAppender(new FileAppender(threadName));
	}
}

//Do zrobienia jest pod³¹czenie tutaj czegoœ, co bêdzie mia³o przechodziæ po kolejnych
//elementach strony, czy tam zapytania wysy³aæ kolejne... nie wiem