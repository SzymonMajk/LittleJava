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
 * @version 1.0
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
		return buffer.remainingCapacity()==0;
	}

	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest zape�niona
	 * @return informacja czy kolejka jest zape�niona
	 */
	@Override
	public boolean isFull() {
		return buffer.isEmpty();
	}

	/** 
	 * Zadaniem funkcji jest umieszczenie otrzymanej w argumencie strony w buforze
	 * w spos�b bezpieczny, maj�c ju� �wiadomo�� istnienia wolnego miejsca w buforze
	 * @param pageHTML Strona, kt�ra ma zosta� umieszczona w buforze
	 */
	@Override
	public void addPage(String pageHTML) {
		try {
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
	public String pollPage() {
		return buffer.poll();
	}
	
	/**
	 * Konstruktor domy�lny, kt�rego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, posiada domy�lny rozmiar r�wny 10
	 */
	BlockingQueuePagesBuffer()
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(new FileAppender("Buffer"));
		buffer = new ArrayBlockingQueue<String>(10);
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest utworzenie 
	 * obiektu odpowiedzialnego za buforowanie stron, pozwalaj�cy na 
	 * ustalenie rozmiaru bufora jest w w�tku nadrz�dnym
	 * @param size rozmiar bufora podany przez u�ytkownika
	 */
	BlockingQueuePagesBuffer(int size)
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(new FileAppender("Buffer"));
		buffer = new ArrayBlockingQueue<String>(size);
	}

}
