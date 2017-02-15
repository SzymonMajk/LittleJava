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
 * @version %I%, %G%
 *
 */
public class LineBrowserTest {

	@Test
	public void testSharedSearchConnectingLines() {
		
		//Przygotowanie plików z danymi
		File testFileBuStop1 = new File("tests/Przystanek1");
		try {
			if(!testFileBuStop1.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop2 = new File("tests/Przystanek2");
		try {
			if(!testFileBuStop2.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		OutputStream toOut;
		PrintWriter to;
		
		try {
			toOut = new FileOutputStream(testFileBuStop1);
			to = new PrintWriter(toOut);
			to.write("218kierunek1\n219kierunek2\n219kierunek3");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("217kierunek1\n219kierunek1\n219kierunek2");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		LineBrowser lineBrowser = new LineBrowser();
		
		lineBrowser.searchConnectingLines("tests/Przystanek1", "tests/Przystanek2");
		
		//Sprawdzanie
		assertEquals(1,lineBrowser.getLines().size());
		assertEquals("219kierunek2",lineBrowser.getLines().get(0));
		
		//usuwanie plików
		if(testFileBuStop1.exists())
		{
			if(!testFileBuStop1.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn nie istnia³!");
		}
		
		if(testFileBuStop2.exists())
		{
			if(!testFileBuStop2.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
	}
	
	@Test
	public void testNotSharedSearchConnectingLines() {
		
		//Przygotowanie plików z danymi
		File testFileBuStop1 = new File("tests/Przystanek1");
		try {
			if(!testFileBuStop1.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop2 = new File("tests/Przystanek2");
		try {
			if(!testFileBuStop2.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		OutputStream toOut;
		PrintWriter to;
		
		try {
			toOut = new FileOutputStream(testFileBuStop1);
			to = new PrintWriter(toOut);
			to.write("218kierunek1\n220kierunek2\n219kierunek3");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("217kierunek1\n219kierunek1\n219kierunek2");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		LineBrowser lineBrowser = new LineBrowser();
		
		lineBrowser.searchConnectingLines("tests/Przystanek1", "tests/Przystanek2");
		
		//Sprawdzanie
		assertEquals(0,lineBrowser.getLines().size());
		
		//usuwanie plików
		if(testFileBuStop1.exists())
		{
			if(!testFileBuStop1.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn nie istnia³!");
		}
		
		if(testFileBuStop2.exists())
		{
			if(!testFileBuStop2.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
	}

}
