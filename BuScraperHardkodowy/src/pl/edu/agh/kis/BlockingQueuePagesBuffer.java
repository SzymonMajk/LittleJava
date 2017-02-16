package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Implementacja synchronizowanego bufora w�tk�w pobieraj�cych oraz wy�uskuj�cych, kt�rej
 * zadaniem jest przechowywanie stron pobranyhc przez w�tki pobieraj�ce, oraz udost�pnianie
 * ich w spos�b bezpieczny w aspekcie wielow�tkowo�ci w�tkom wy�uskuj�cym, wykorzystuj�c
 * w tym celu implementacj� interfejsku BlockingQueue. Udost�pnia r�wnie� informacje 
 * stanie bufora oraz pozwala poprzez konstruktor sparametryzowany ustali� jego rozmiar.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class BlockingQueuePagesBuffer implements PagesBuffer {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = 
			LogManager.getLogger(BlockingQueuePagesBuffer.class.getName());
	
	/**
	 * Obiekt przechowuj�cy synchronizowany bufor, jest on podawany w konstruktorze
	 */
	private BlockingQueue<String> buffer;
	
	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest pusta. Zwraca informacj� o stanie
	 * zape�nienia bufora blokuj�cego.
	 * @return zwraca prawd� je�li bufor jest pusty oraz fa�sz je�li nie jest pusty.
	 */
	@Override
	synchronized public boolean isEmpty() {
		return buffer.isEmpty();
	}

	/**
	 * Zadaniem funkcji jest sprawdzenie czy kolejka jest pe�na. Zwraca informacj� o stanie
	 * zape�nienia bufora blokuj�cego.
	 * @return zwraca prawd� je�li bufor jest pe�ny oraz fa�sz je�li nie jest pe�ny.
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
		log4j.info("Dodano stron�! Stan bufora przed:"+buffer.size());
		buffer.put(pageHTML);
	}

	/**
	 * Zadaniem funkcji jest pobranie oraz zwr�cenie kodu �r�d�owego pierwszej
	 * strony z bufora, a nast�pnie usuni�cie jej, b�d�c �wiadomym, i� takowa znajduje
	 * si� w ju� w buforze, dlatego u�ytkownik jest zobligowany do umieszczenia wywo�ania 
	 * funkcji bezpo�rednio po wywo�aniu funkcji isEmpty() oraz zwr�cenie przez ni� warto�ci
	 * logicznego fa�szu. Je�eli kolejka oka�e si� pusta, w�tek b�dzie czeka� na pojawienie
	 * si� w niej nowego elementu. Ustalony jest jednak 
	 * @return Pierwszy kod �r�d�owy strony, pobierany z bufora
	 * @throws InterruptedException wyj�tek pojawia si� przy pr�bie nieprawid�owego wybudzenia
	 */
	@Override
	public String pollPage() throws InterruptedException {
		//metoda take u�ywa wait oraz notify
		log4j.info("Pobrano stron�!  Stan bufora przed:"+buffer.size());
		return buffer.poll(2, TimeUnit.SECONDS);
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest utworzenie  obiektu 
	 * odpowiedzialnego za przetrzymywanie stron w buforze o rozmiarze ustalonym
	 * poprzez argument.
	 * @param size rozmiar bufora podany przez u�ytkownika.
	 */
	BlockingQueuePagesBuffer(int size)
	{
		buffer = new ArrayBlockingQueue<String>(size);
	}
	
	/**
	 * Konstruktor domy�lny, kt�rego zadaniem jest utworzenie obiektu odpowiedzialnego
	 * za buforowanie stron, ustala domy�lny rozmiar bufora r�wny 50.
	 */
	BlockingQueuePagesBuffer()
	{
		this(50);
	}
}
