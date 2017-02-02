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
 * @version 1.2
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
		return buffer.isEmpty();
	}

	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest zape³niona
	 * @return informacja czy kolejka jest zape³niona
	 */
	@Override
	public boolean isFull() {
		return buffer.remainingCapacity()==0;
	}

	/** 
	 * Zadaniem funkcji jest umieszczenie otrzymanej w argumencie strony w buforze
	 * w sposób bezpieczny, maj¹c ju¿ œwiadomoœæ istnienia wolnego miejsca w buforze
	 * @param pageHTML Strona, która ma zostaæ umieszczona w buforze
	 */
	@Override
	public void addPage(String pageHTML) {
		bufferLogger.info("Podali mi stronê!");
		bufferLogger.execute();
		try {
			//metoda put u¿ywa wait oraz notify
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
	public String takePage() {
		//metoda take u¿ywa wait oraz notify
		bufferLogger.info("Zabrali mi stronê!");
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
	 * Konstruktor sparametryzowany, którego zadaniem jest utworzenie 
	 * obiektu odpowiedzialnego za buforowanie stron, pozwalaj¹cy na 
	 * ustalenie rozmiaru bufora jest w w¹tku nadrzêdnym oraz okreœlenie
	 * rodzaju obiektu wysy³aj¹cego logi
	 * @param size rozmiar bufora podany przez u¿ytkownika
	 * @param appender obiekt s³u¿¹cy do wysy³ania logów
	 */
	BlockingQueuePagesBuffer(int size, Appends appender)
	{
		bufferLogger = new Logger();
		bufferLogger.changeAppender(appender);
		buffer = new ArrayBlockingQueue<String>(size);
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest utworzenie 
	 * obiektu odpowiedzialnego za buforowanie stron, pozwalaj¹cy na 
	 * ustalenie rozmiaru bufora jest w w¹tku nadrzêdnym
	 * @param size rozmiar bufora podany przez u¿ytkownika
	 */
	BlockingQueuePagesBuffer(int size)
	{
		this(size, new FileAppender(("Buffer")));
	}
	
	/**
	 * Konstruktor domyœlny, którego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, posiada domyœlny rozmiar równy 10
	 */
	BlockingQueuePagesBuffer()
	{
		this(10, new FileAppender(("Buffer")));
	}
}
