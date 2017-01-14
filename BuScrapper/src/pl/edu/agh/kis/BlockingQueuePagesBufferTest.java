package pl.edu.agh.kis;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testy b�d� polega�y na twworzeniu specyficznych w�tk�w, dzia�aj�cych tak
 * aby ostatecznie da�o si� sprawdzi� dzia�anie kolejki
 * @author Szymon Majkut
 * @version 1.1a
 *
 */
public class BlockingQueuePagesBufferTest {

	/**
	 * Tworzymy dwa w�tki, jeden �ci�gaj�cy z kolejki i drugi dodaj�cy do niej
	 */
	@Test
	public void testIsEmpty() {

		//Najpierw tylko w�tek �ci�gaj�cy, kt�ry kilka razy spr�buje �ci�gn��, za ka�dym
		//razem kolejka powinna by� pusta
		
		
		
		//Teraz w�tki dodaj�cy do kolejki, dodaje okre�lon� liczb� razy
		
		//Teraz w�tki sprawdzaj�cy odpalamy znowu, tym razem powinien zobaczy� w kolejce
		
		//Teraz uruchamiamy w�tki �ci�gaj�cy z kolejki okre�lon� ilo�� razy
		
		//Zn�w w�tkisprawdzaj�ce czy kolejka zosta�a wyzerowana
	
	}

	/**
	 * Analgoicznie do powy�szego testu, z tym �e teraz dzia�amy przy ustalonej obj�to�ci
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
