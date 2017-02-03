package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Test;

/**
 * Test wybierania godzin
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class HourBrowserTest {


	@Test
	public void testSearchHours() {
		//Przygotowanie plików z danymi
		File testFileBuStop1 = new File("tests/Przystanek1");
		try {
			testFileBuStop1.createNewFile();
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop2 = new File("tests/Przystanek2");
		try {
			testFileBuStop2.createNewFile();
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		OutputStream toOut;
		PrintWriter to;
		
		try {
			toOut = new FileOutputStream(testFileBuStop1);
			to = new PrintWriter(toOut);
			to.write("12:5,46:5:5:\n13:5:5:5:\n14:5:5:5:\n15:5:5:5:");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("12:7,54:7:7:\n13:7:7:7:\n14:7:7:7:\n15:7:7:7:");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser();
		
		hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, 12, 2, 0);
		
		//Sprawdzanie
		assertEquals(3,hourBrowser.getHours().size());
		assertEquals("12:46",hourBrowser.getHours().get(0));
		assertEquals("13:5",hourBrowser.getHours().get(1));
		assertEquals("14:5",hourBrowser.getHours().get(2));
		
		//usuwanie plików
		if(testFileBuStop1.exists())
		{
			testFileBuStop1.delete();
			testFileBuStop1.getParentFile().delete();
		}
		else
		{
			fail("Plik testowy testFileIn nie istnia³!");
		}
				
		if(testFileBuStop2.exists())
		{
			testFileBuStop2.delete();
			testFileBuStop2.getParentFile().delete();
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
	}

}
