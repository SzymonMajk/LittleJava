package pl.edu.agh.kis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Implementacja synchronizowanego bufora dla metodyki producent-konsument, kt�rej
 * zadaniem jest przechowywanie poprawnie pobranych stron, oraz udost�pnianie
 * ich w spos�b bezpieczny w aspekcie wielow�tkowo�ci, wykorzystuj�c w tym celu
 * BlockingQueue, udost�pnia r�wnie� informacje o pusto�ci oraz zape�nieniu bufora
 * oraz pozwala poprzez konstruktor sparametryzowany ustali� jego rozmiar
 * @author Szymon Majkut
 * @version 1.2
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
	public boolean isEmpty() {
		return buffer.isEmpty();
	}

	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest zape�niona
	 * @return informacja czy kolejka jest zape�niona
	 */
	@Override
	public boolean isFull() {
		return buffer.remainingCapacity()==0;
	}

	/** 
	 * Zadaniem funkcji jest umieszczenie otrzymanej w argumencie strony w buforze
	 * w spos�b bezpieczny, maj�c ju� �wiadomo�� istnienia wolnego miejsca w buforze
	 * @param pageHTML Strona, kt�ra ma zosta� umieszczona w buforze
	 */
	@Override
	public void addPage(String pageHTML) {
		bufferLogger.info("Podali mi stron�!");
		bufferLogger.execute();
		try {
			//metoda put u�ywa wait oraz notify
			buffer.put(pageHTML);
		} catch (InterruptedException e) {
			// Trzeba co� wymy�le�...
			e.printStackTrace();
		}
	}

	/**
	 * Zadaniem funkcji jest pobranie oraz usuni�cie pierwszej strony z bufora
	 * i zwr�cenie jej, zak�adaj�c �e co� ju� znajduje si� w kolejce
	 * @return Pierwsza strona pobrana z kolejki
	 */
	@Override
	public String takePage() {
		//metoda take u�ywa wait oraz notify
		bufferLogger.info("Zabrali mi stron�!");
		bufferLogger.execute();
		String result = "";
		
		try {
			result = buffer.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest utworzenie 
	 * obiektu odpowiedzialnego za buforowanie stron, pozwalaj�cy na 
	 * ustalenie rozmiaru bufora jest w w�tku nadrz�dnym oraz okre�lenie
	 * rodzaju obiektu wysy�aj�cego logi
	 * @param size rozmiar bufora podany przez u�ytkownika
	 * @param appender obiekt s�u��cy do wysy�ania log�w
	 */
	BlockingQueuePagesBuffer(int size, Appends appender)
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(appender);
		buffer = new ArrayBlockingQueue<String>(size);
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest utworzenie 
	 * obiektu odpowiedzialnego za buforowanie stron, pozwalaj�cy na 
	 * ustalenie rozmiaru bufora jest w w�tku nadrz�dnym
	 * @param size rozmiar bufora podany przez u�ytkownika
	 */
	BlockingQueuePagesBuffer(int size)
	{
		this(size, new FileAppender(("Buffer")));
	}
	
	/**
	 * Konstruktor domy�lny, kt�rego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, posiada domy�lny rozmiar r�wny 10
	 */
	BlockingQueuePagesBuffer()
	{
		this(10, new FileAppender(("Buffer")));
	}
}
