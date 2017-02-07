package pl.edu.agh.kis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Implementacja synchronizowanego bufora w�tk�w pobieraj�cych oraz wy�uskuj�cych, kt�rej
 * zadaniem jest przechowywanie stron pobranyhc przez w�tki pobieraj�ce, oraz udost�pnianie
 * ich w spos�b bezpieczny w aspekcie wielow�tkowo�ci w�tkom wy�uskuj�cym, wykorzystuj�c
 * w tym celu implementacj� interfejsku BlockingQueue. Udost�pnia r�wnie� informacje 
 * stanie bufora oraz pozwala poprzez konstruktor sparametryzowany ustali� jego rozmiar.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class BlockingQueuePagesBuffer implements PagesBuffer {

	/**
	 * W�asny system log�w
	 */
	private Logger bufferLogger;
	
	/**
	 * Obiekt przechowuj�cy synchronizowany bufor, jest on podawany w konstruktorze
	 */
	private BlockingQueue<String> buffer;
	
	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest pusta
	 * @return informacja czy kolejka jest pusta
	 */
	@Override
	synchronized public boolean isEmpty() {
		return buffer.isEmpty();
	}

	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest zape�niona
	 * @return informacja czy kolejka jest zape�niona
	 */
	@Override
	synchronized public boolean isFull() {
		return buffer.remainingCapacity()==0;
	}

	/** 
	 * Zadaniem funkcji jest umieszczenie otrzymanej w argumencie strony w buforze
	 * w spos�b bezpieczny, maj�c ju� �wiadomo�� istnienia wolnego miejsca w buforze,
	 * dlatego u�ytkownik jest zobligowany do umieszczenia wywo�ania funkcji bezpo�rednio
	 * po wywo�aniu funkcji isFull() oraz zwr�cenie przez ni� warto�ci logicznego fa�szu.
	 * @param pageHTML Kod �r�d�owy strony, kt�ra ma zosta� umieszczona w buforze
	 * @throws InterruptedException wyj�tek pojawia si� przy pr�bie nieprawid�owego wybudzenia
	 */
	@Override
	public void addPage(String pageHTML) throws InterruptedException {
		bufferLogger.info("Podali mi stron�!");
		bufferLogger.execute();
		buffer.put(pageHTML);
	}

	/**
	 * Zadaniem funkcji jest pobranie oraz zwr�cenie kodu �r�d�owego pierwszej
	 * strony z bufora, a nast�pnie usuni�cie jej, b�d�c �wiadomym, i� takowa znajduje
	 * si� w ju� w buforze, dlatego u�ytkownik jest zobligowany do umieszczenia wywo�ania 
	 * funkcji bezpo�rednio po wywo�aniu funkcji isEmpty() oraz zwr�cenie przez ni� warto�ci
	 * logicznego fa�szu.
	 * @return Pierwszy Kod �r�d�owy strony, pobierany z bufora
	 * @throws InterruptedException wyj�tek pojawia si� przy pr�bie nieprawid�owego wybudzenia
	 */
	@Override
	public String takePage() throws InterruptedException {
		//metoda take u�ywa wait oraz notify
		bufferLogger.info("Zabrali mi stron�!");
		bufferLogger.execute();

		return buffer.take();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest utworzenie  obiektu 
	 * odpowiedzialnego za buforowanie stron, pozwalaj�cy na ustalenie rozmiaru bufora 
	 * oraz przypisanie obiektu sk�aduj�cego logi.
	 * @param size rozmiar bufora podany przez u�ytkownika
	 * @param appender obiekt s�u��cy do sk�adowania log�w.
	 */
	BlockingQueuePagesBuffer(int size, Appends appender)
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(appender);
		buffer = new ArrayBlockingQueue<String>(size);
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest utworzenie obiektu
	 * odpowiedzialnego za buforowanie stron, pozwalaj�cy na ustalenie rozmiaru 
	 * bufora oraz ustalaj�cy domy�lny spos�b sk�adowania log�w.
	 * @param size rozmiar bufora podany przez u�ytkownika.
	 */
	BlockingQueuePagesBuffer(int size)
	{
		this(size, new FileAppender(("Buffer")));
	}
	
	/**
	 * Konstruktor domy�lny, kt�rego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, ustala zar�wno domy�lny rozmiar bufora, jak i domy�lny
	 * spos�b sk�adowania log�w.
	 */
	BlockingQueuePagesBuffer()
	{
		this(10, new FileAppender(("Buffer")));
	}
}
