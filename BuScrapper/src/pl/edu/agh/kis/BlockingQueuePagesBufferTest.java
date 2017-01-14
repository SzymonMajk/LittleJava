package pl.edu.agh.kis;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testy bêd¹ polega³y na twworzeniu specyficznych w¹tków, dzia³aj¹cych tak
 * aby ostatecznie da³o siê sprawdziæ dzia³anie kolejki
 * @author Szymon Majkut
 * @version 1.1a
 *
 */
public class BlockingQueuePagesBufferTest {

	/**
	 * Tworzymy dwa w¹tki, jeden œci¹gaj¹cy z kolejki i drugi dodaj¹cy do niej
	 */
	@Test
	public void testIsEmpty() {

		//Najpierw tylko w¹tek œci¹gaj¹cy, który kilka razy spróbuje œci¹gn¹æ, za ka¿dym
		//razem kolejka powinna byæ pusta
		
		
		
		//Teraz w¹tki dodaj¹cy do kolejki, dodaje okreœlon¹ liczbê razy
		
		//Teraz w¹tki sprawdzaj¹cy odpalamy znowu, tym razem powinien zobaczyæ w kolejce
		
		//Teraz uruchamiamy w¹tki œci¹gaj¹cy z kolejki okreœlon¹ iloœæ razy
		
		//Znów w¹tkisprawdzaj¹ce czy kolejka zosta³a wyzerowana
	
	}

	/**
	 * Analgoicznie do powy¿szego testu, z tym ¿e teraz dzia³amy przy ustalonej objêtoœci
	 * kolejki i patrzymy niejako z drugiej strony na problem
	 */
	@Test
	public void testIsFull() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testPollPage() {
		fail("Not yet implemented");
	}

}
