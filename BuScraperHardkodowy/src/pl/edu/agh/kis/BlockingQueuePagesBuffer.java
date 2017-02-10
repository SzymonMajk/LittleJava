package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Implementacja synchronizowanego bufora w¹tków pobieraj¹cych oraz wy³uskuj¹cych, której
 * zadaniem jest przechowywanie stron pobranyhc przez w¹tki pobieraj¹ce, oraz udostêpnianie
 * ich w sposób bezpieczny w aspekcie wielow¹tkowoœci w¹tkom wy³uskuj¹cym, wykorzystuj¹c
 * w tym celu implementacjê interfejsku BlockingQueue. Udostêpnia równie¿ informacje 
 * stanie bufora oraz pozwala poprzez konstruktor sparametryzowany ustaliæ jego rozmiar.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class BlockingQueuePagesBuffer implements PagesBuffer {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = 
			LogManager.getLogger(BlockingQueuePagesBuffer.class.getName());
	
	/**
	 * Obiekt przechowuj¹cy synchronizowany bufor, jest on podawany w konstruktorze
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
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest zape³niona
	 * @return informacja czy kolejka jest zape³niona
	 */
	@Override
	synchronized public boolean isFull() {
		return buffer.remainingCapacity()==0;
	}

	/** 
	 * Zadaniem funkcji jest umieszczenie otrzymanej w argumencie strony w buforze
	 * w sposób bezpieczny, maj¹c ju¿ œwiadomoœæ istnienia wolnego miejsca w buforze,
	 * dlatego u¿ytkownik jest zobligowany do umieszczenia wywo³ania funkcji bezpoœrednio
	 * po wywo³aniu funkcji isFull() oraz zwrócenie przez ni¹ wartoœci logicznego fa³szu.
	 * @param pageHTML Kod Ÿród³owy strony, która ma zostaæ umieszczona w buforze
	 * @throws InterruptedException wyj¹tek pojawia siê przy próbie nieprawid³owego wybudzenia
	 */
	@Override
	public void addPage(String pageHTML) throws InterruptedException {
		log4j.info("Dodano stronê! Stan bufora przed:"+buffer.size());
		buffer.put(pageHTML);
	}

	/**
	 * Zadaniem funkcji jest pobranie oraz zwrócenie kodu Ÿród³owego pierwszej
	 * strony z bufora, a nastêpnie usuniêcie jej, bêd¹c œwiadomym, i¿ takowa znajduje
	 * siê w ju¿ w buforze, dlatego u¿ytkownik jest zobligowany do umieszczenia wywo³ania 
	 * funkcji bezpoœrednio po wywo³aniu funkcji isEmpty() oraz zwrócenie przez ni¹ wartoœci
	 * logicznego fa³szu.
	 * @return Pierwszy kod Ÿród³owy strony, pobierany z bufora
	 * @throws InterruptedException wyj¹tek pojawia siê przy próbie nieprawid³owego wybudzenia
	 */
	@Override
	public String takePage() throws InterruptedException {
		//metoda take u¿ywa wait oraz notify
		log4j.info("Pobrano stronê!  Stan bufora przed:"+buffer.size());
		return buffer.take();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest utworzenie  obiektu 
	 * odpowiedzialnego za buforowanie stron o rozmiarze ustalonym przez argument
	 * @param size rozmiar bufora podany przez u¿ytkownika
	 */
	BlockingQueuePagesBuffer(int size)
	{
		buffer = new ArrayBlockingQueue<String>(size);
	}
	
	/**
	 * Konstruktor domyœlny, którego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, ustala domyœlny rozmiar bufora równy 50
	 */
	BlockingQueuePagesBuffer()
	{
		this(50);
	}
}
