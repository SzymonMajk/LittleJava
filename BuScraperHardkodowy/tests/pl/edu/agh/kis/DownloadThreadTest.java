package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

/**
 * Dwa proste testy dla klasy DownloadThread
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class DownloadThreadTest {

	/**
	 * Sprwadzamy przypadek, gdy otrzymujemy od servera odpowiedz 200 OK
	 */
	@Test
	public void testRunRespons200() {
			
		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		BlockingQueue<Request> requestsToDo = new ArrayBlockingQueue<Request>(3);
		BlockingQueue<Request> requestsToRepeat = new ArrayBlockingQueue<Request>(3);

		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		String nameOfTestFile = "Tests/ServerRespons200";
		String nameOfGotFile = testRoot+"Result";
		File testFile = new File(nameOfTestFile);
		File gotFile = new File(nameOfGotFile);
	
		if(!gotFile.getParentFile().mkdir())
		{
			fail("Nie utworzono katalogów");
		}
		
		DownloadThread d1 = new DownloadThread(999,pages,requestsToDo,requestsToRepeat,
				new FileDownloader(nameOfGotFile,nameOfTestFile));
		
		
		//Przygotowanie danych testowych
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
			
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try (BufferedReader from1 = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"))) {

			while((line = from1.readLine()) != null)
			{
				XMLDocument.append(line);
			}
				
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		try {
			pages.addPage(XMLDocument.toString());
		} catch (InterruptedException e) {
			fail("W¹tek przerwany"+e.getMessage());
		}
		
		d1.start();
		try {
			d1.join();
		} catch (InterruptedException e) {
			fail("W¹tek przerwany"+e.getMessage());
		}
		
		//Usuwamy utworzone pliki
		if(!gotFile.getParentFile().delete())
		{
			fail("Nie usuniêto katalogu");
		}
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(false,pages.isEmpty());
		try {
			assertEquals(true,pages.pollPage().contains("Odpowiedz z pliku 200 OK"));
		} catch (InterruptedException e) {
			fail("Niepoprawnie wybudzony:"+e.getMessage());
		}
	}
}