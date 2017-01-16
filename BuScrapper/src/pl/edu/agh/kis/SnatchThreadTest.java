package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

/**
 * Dwa proste testy dla klasy SnatchThread
 * @author Szymon Majkut
 * @version 1.1b
 *
 */
public class SnatchThreadTest {

	/**
	 * Sprawdzamy czy utworzony plik jest to¿samy w oczekiwanym
	 */
	@Test
	public void testRun() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5,new NullAppender());
		FileStoreBusInfo storer = new FileStoreBusInfo(new NullAppender());
		String[] expresions = {"//div/p[@style=' font-size: 40px;']|"
				+ "//div/p[@style='font-size: 40px;']|"
				+ "//div/p[@style='font-size: 40px; ']",
				"//p[@style=' font-size: 24px; text-align: center; white-space: "
				+ "nowrap; display: inline-flex;']|"
				+ "//p[@style='font-size: 24px; text-align: center; white-space: "
				+ "nowrap; display: inline-flex;']|"
				+ "//p[@style='font-size: 24px; text-align: center; white-space: "
				+ "nowrap; display: inline-flex; ']",
				"//tr[@style=' border-bottom: solid lightgray; border-width: 1px;']|"
				+ "//tr[@style='border-bottom: solid lightgray; border-width: 1px;']|"
				+ "//tr[@style='border-bottom: solid lightgray; border-width: 1px; ']"};
		
		SnatchThread testSnatch = new SnatchThread(999,pages,storer,expresions,new NullAppender());
		//SnatchThread testSnatch2 = new SnatchThread(998,pages,storer,expresions,
		//new NullAppender()); mo¿e istnieæ! ale po co go robiæ?

		//Przygotowanie danych testowych
		File testFile = new File("Tests/testAnaliseHTMLPage");
		testFile.createNewFile();
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile)));
		
		String line = "";
		String XMLDocument = "";
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument += line;
			}
			
			from.close();

		} catch (IOException e) {
			//e.printStackTrace();
		}
				
		pages.addPage(XMLDocument);
		
		//Odpalmy to!
		testSnatch.start();
		testSnatch.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		
		String expected = "";
		String got = "";
		
		File expectedFile = new File("Tests/testKombinatTest");
		File gotFile = new File("999/KombinatTest");
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego, sprawdz katalog Tests");
		}
		
		from = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)));
		
		try {
			while((line = from.readLine()) != null)
			{
				expected += line;
			}
			
			from.close();

		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		from = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)));
		
		try {
			while((line = from.readLine()) != null)
			{
				got += line;
			}

			from.close();

		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		assertEquals(expected,got);
		
		//Usuwamy utworzony plik
		if(gotFile.exists())
		{
			gotFile.delete();
			gotFile.getParentFile().delete();
		}
		else
		{
			fail("Nie utworzono pliku! A raczej gdzieœ siê zapodzia³!");
		}
		
		
		//jakiœ assert wykombinuj, gdy ju¿ bêdzie wypisywa³o to co ma wypisywaæ
		//albo gdy utworzy taki plik jak ma utworzyæ, ¿e numer linii itp
	}
}
