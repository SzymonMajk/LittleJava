package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * Kilka test�w czy wszystko dzia�a poprawnie
 * @author Szymon Majkut
 * @version 1.1b
 *
 */
public class ConfiguratorTest {

	/**
	 * Sprawdzamy czy zapytania si� zgadzaj�
	 */
	@Test
	public void testGetXPaths() {
		
		Configurator conf = new Configurator("Tests/testConf",new NullAppender());
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		String[] paths = conf.getXPaths();
		
		assertEquals(3,paths.length);
		
		assertEquals("//div/p[@style=' font-size: 40px;']",paths[0]);
		assertEquals("//p[@style=' font-size: 24px; text-align: center;"
				+ " white-space: nowrap; display: inline-flex;']",
				paths[1]);
		assertEquals("//tr[@style='border-bottom: solid lightgray; border-width: 1px;']",
				paths[2]);
	}

	/**
	 * Sprawdzamy czy kolejka zapyta� tworzy si� poprawnie
	 */
	@Test
	public void testGetRequests() {
		
		Configurator conf = new Configurator("Tests/testConf",new NullAppender());
		
		conf.getRequests();
		
		fail("Not yet implemented");
	}

}
