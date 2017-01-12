package pl.edu.agh.kis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Implementacja synchronizowanego bufora dla metodyki producent-konsument, której
 * zadaniem jest przechowywanie poprawnie pobranych stron, oraz udostêpnianie
 * ich w sposób bezpieczny w aspekcie wielow¹tkowoœci, wykorzystuj¹c w tym celu
 * BlockingQueue, udostêpnia równie¿ informacje o pustoœci oraz zape³nieniu bufora
 * oraz pozwala poprzez konstruktor sparametryzowany ustaliæ jego rozmiar
 * @author Szymon Majkut
 * @version 1.0
 *
 */
public class BlockingQueuePagesBuffer implements PagesBuffer {

	/**
	 * W³asny system logów
	 */
	private Logger bufferLogger;
	
	/**
	 * Obiekt przechowuj¹cy synchronizowany bufor, jest on podawany w konstruktorze
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
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest zape³niona
	 * @return informacja czy kolejka jest zape³niona
	 */
	@Override
	public boolean isFull() {
		return buffer.isEmpty();
	}

	/** 
	 * Zadaniem funkcji jest umieszczenie otrzymanej w argumencie strony w buforze
	 * w sposób bezpieczny, maj¹c ju¿ œwiadomoœæ istnienia wolnego miejsca w buforze
	 * @param pageHTML Strona, która ma zostaæ umieszczona w buforze
	 */
	@Override
	public void addPage(String pageHTML) {
		try {
			buffer.put(pageHTML);
		} catch (InterruptedException e) {
			// Trzeba coœ wymyœleæ...
			e.printStackTrace();
		}
	}

	/**
	 * Zadaniem funkcji jest pobranie oraz usuniêcie pierwszej strony z bufora
	 * i zwrócenie jej, zak³adaj¹c ¿e coœ ju¿ znajduje siê w kolejce
	 * @return Pierwsza strona pobrana z kolejki
	 */
	@Override
	public String pollPage() {
		return buffer.poll();
	}
	
	/**
	 * Konstruktor domyœlny, którego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, posiada domyœlny rozmiar równy 10
	 */
	BlockingQueuePagesBuffer()
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(new FileAppender("Buffer"));
		buffer = new ArrayBlockingQueue<String>(10);
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest utworzenie 
	 * obiektu odpowiedzialnego za buforowanie stron, pozwalaj¹cy na 
	 * ustalenie rozmiaru bufora jest w w¹tku nadrzêdnym
	 * @param size rozmiar bufora podany przez u¿ytkownika
	 */
	BlockingQueuePagesBuffer(int size)
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(new FileAppender("Buffer"));
		buffer = new ArrayBlockingQueue<String>(size);
	}

}
