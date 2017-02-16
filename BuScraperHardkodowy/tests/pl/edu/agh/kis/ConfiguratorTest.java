package pl.edu.agh.kis;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;

import pl.edu.agh.kis.configuration.Configurator;

/**
 * Kilka testów czy wszystko dzia³a poprawnie
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class ConfiguratorTest {

	/**
	 * Sprawdzamy czy kolejka zadañ tworzy siê poprawnie
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
		
		assertEquals("",newTask.getUrlPath());
		assertEquals("4",newTask.getLineNumber());
		assertEquals(5,newTask.getMaxBuStop());
		assertEquals(5,newTask.getMaxDirection());
		assertEquals("GET",newTask.getMethod());
		
		assertEquals(true,tasks.hasNextTask());
		newTask = tasks.getNextTask();
		tasks.removeTask(newTask.getId());
		
		assertEquals("",newTask.getUrlPath());
		assertEquals("5",newTask.getLineNumber());
		assertEquals(5,newTask.getMaxBuStop());
		assertEquals(5,newTask.getMaxDirection());
		assertEquals("GET",newTask.getMethod());
		
		assertEquals(true,tasks.hasNextTask());
		newTask = tasks.getNextTask();
		tasks.removeTask(newTask.getId());
		
		assertEquals("",newTask.getUrlPath());
		assertEquals("6",newTask.getLineNumber());
		assertEquals(5,newTask.getMaxBuStop());
		assertEquals(5,newTask.getMaxDirection());
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
