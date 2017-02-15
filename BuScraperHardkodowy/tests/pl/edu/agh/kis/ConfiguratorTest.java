package pl.edu.agh.kis;

import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import org.junit.Test;

/**
 * Kilka testów czy wszystko dzia³a poprawnie
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class ConfiguratorTest {

	/**
	 * Sprawdzamy czy zapytania siê zgadzaj¹
	 */
	@Test
	public void testGetXPaths() {
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator conf = new Configurator("Tests/testConf",
				new TaskManager());
		
		HashMap<String,String> paths = conf.getXPaths();
		
		assertEquals(4,paths.size());
		
		assertEquals("//td/p[@style=' font-size: 24px; text-align: left; "
				+ "white-space: nowrap;']",paths.get("buStopName"));
		assertEquals("//td/div/p[@style=' font-size: 40px;']",paths.get("lineNumber"));
		assertEquals("//td[@style=' vertical-align: top; text-align: "
				+ "left;']/table/tr/td[@style=' vertical-align: "
				+ "top;']/table/tr",paths.get("hours"));
		assertEquals("//table/tr/td/table/tr/td/div[@style=' text-align: left; "
				+ "white-space: nowrap; border-left: solid black; border-radius: "
				+ "20px; padding: 10px;']",paths.get("direction"));
	}

	/**
	 * Sprawdzamy czy kolejka zapytañ tworzy siê poprawnie
	 */
	@Test
	public void testTasks() {
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		TaskManager tasks = new TaskManager();
		Configurator conf = new Configurator("Tests/testConf",tasks);
		if(!conf.getUpdateData())
		{
			fail("Niepoprawnie przeczytany plik konfiguracyjny");
		}
		
		assertEquals(true,tasks.hasNextTask());
		Task newTask = tasks.getNextTask();
		tasks.removeTask(newTask.getId());
		
		assertEquals("rozklady.mpk.krakow.pl",newTask.getHost());
		assertEquals("4",newTask.getLineNumber());
		assertEquals("5",newTask.getMaxBuStop());
		assertEquals("5",newTask.getMaxDirection());
		assertEquals("GET",newTask.getMethod());
		
		assertEquals(true,tasks.hasNextTask());
		newTask = tasks.getNextTask();
		tasks.removeTask(newTask.getId());
		
		assertEquals("rozklady.mpk.krakow.pl",newTask.getHost());
		assertEquals("5",newTask.getLineNumber());
		assertEquals("5",newTask.getMaxBuStop());
		assertEquals("5",newTask.getMaxDirection());
		assertEquals("GET",newTask.getMethod());
		
		assertEquals(true,tasks.hasNextTask());
		newTask = tasks.getNextTask();
		tasks.removeTask(newTask.getId());
		
		assertEquals("rozklady.mpk.krakow.pl",newTask.getHost());
		assertEquals("6",newTask.getLineNumber());
		assertEquals("5",newTask.getMaxBuStop());
		assertEquals("5",newTask.getMaxDirection());
		assertEquals("GET",newTask.getMethod());
		
		assertEquals(false,tasks.hasNextTask());

	}
	
	/**
	 * Sprawdzamy czy otrzymamy dobre wyszukiwania
	 */
	@Test
	public void testGetToSearch() {
		
		Configurator conf = new Configurator("Tests/testConf",
				new TaskManager());
		
		String expected = "Czarnowiejska:MiasteczkoStudenckieAGH:0:12:51:2";
		
		assertEquals(expected,conf.getToSerach().get(0));
	}
	
	/**
	 * Sprawdzamy czy otrzymamy dobry pageURL
	 */
	@Test
	public void testGetStartPageURL() {
		
		Configurator conf = new Configurator("Tests/testConf",
				new TaskManager());
		
		String expected = "http://rozklady.mpk.krakow.pl/";
		
		assertEquals(expected,conf.getStartPageURL());
	}
	
	/**
	 * Sprawdzamy czy otrzymamy odpowiedz na pytanie czy aktualizowaæ
	 */
	@Test
	public void testGetUpdateData() {
		
		Configurator conf = new Configurator("Tests/testConf",
				new TaskManager());
		
		Boolean expected = true;
		
		assertEquals(expected,conf.getUpdateData());
	}
}
//TODO i tak trzeba bêdzie nowe testy jak go pozmieniamy...
