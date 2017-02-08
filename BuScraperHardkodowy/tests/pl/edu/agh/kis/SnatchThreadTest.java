package pl.edu.agh.kis;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

/**
 * Test klasy SnatchThread
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class SnatchThreadTest {

	/**
	 * Sprawdzamy czy utworzony plik jest to�samy w oczekiwanym
	 */
	@Test
	public void testRun() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5,new NullAppender());
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager(),new NullAppender());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(
	    		new NullAppender()),configurator.getXPaths(), new NullAppender());
		/*SnatchThread s2 = new SnatchThread(998,pages,new FileStoreBusInfo(
	    		new NullAppender()),configurator.getXPaths(), new NullAppender());*/

		//Przygotowanie danych testowych
		File testFile = new File("Tests/testAnaliseHTMLPage");
		if(!testFile.exists())
		{
			fail("Brak �adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile)));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi� si� wyj�tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik por�wnawczy oraz utwrzony i por�wnujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/testStrugaTest");
		File gotFile = new File("999OdTestDoKierunek/Struga");
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego, sprawdz katalog Tests");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego, sprawdz katalog 999OdTestDoKierunek/Struga");
		}
		
		from = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)));
		
		try {
			while((line = from.readLine()) != null)
			{
				expected.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi� si� wyj�tek przy czytaniu z pliku testStrugaTest");
		}
		
		from = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)));
		
		try {
			while((line = from.readLine()) != null)
			{
				got.append(line);
			}

			from.close();

		} catch (IOException e) {
			fail("Pojawi� si� wyj�tek przy czytaniu z pliku StrugaTest");
		}
		
		assertEquals(expected.toString(),got.toString());
		
		//Usuwamy utworzony plik
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuni�to pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuni�to katalogu");
			}
		}
		else
		{
			fail("Nie utworzono pliku! A raczej gdzie� si� zapodzia�!");
		}
	}
}
