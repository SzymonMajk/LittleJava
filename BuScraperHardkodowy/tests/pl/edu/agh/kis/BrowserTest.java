package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Test;

import pl.edu.agh.kis.search.Browser;
import pl.edu.agh.kis.search.Search;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class BrowserTest {

	@Test
	public void testFoundSerch() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		//Przygotowanie plików z danymi
		File testFileBuStop1 = new File("999test/Przystanek1");
		try {
			if(!testFileBuStop1.getParentFile().mkdir())
			{
				fail("Nie utworzono pliku");
			}
			if(!testFileBuStop1.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop2 = new File("999test/Przystanek2");
		try {
			if(!testFileBuStop2.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop3 = new File("buStops/Przystanek1");
		try {
			if(!testFileBuStop3.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop4 = new File("buStops/Przystanek2");
		try {
			if(!testFileBuStop4.createNewFile())
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
			to.write("3::::\n"+
					"4::::\n"+
					"5:00,30,50:10,50:10,50:\n"+
					"6:11,31,51:31:31:\n"+
					"7:11,31,51:11,51:11,51:\n"+
					"8:11,31:31:31:\n"+
					"9:11,51:11,51:11,51:\n"+
					"10:31:31:31:\n"+
					"11:11,51:11,51:11,51:\n"+
					"12:31:31:31:\n"+
					"13:12,52:11,51:11,51:\n"+
					"14:32:31:31:\n"+
					"15:12,32,52:11,51:11,51:\n"+
					"16:12,32,52:31:31:\n"+
					"17:11,31,51:11,51:11,51:\n"+
					"18:11,31,51:31:31:\n"+
					"19:16,56:11,51:11,51:\n"+
					"20:33:31:31:\n"+
					"21:12,52:10,50:10,50:\n"+
					"22:42:30:30:\n"+
					"23::::\n"+
					"0::::\n"+
					"1::::\n"+
					"2::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
			toOut = new FileOutputStream(testFileBuStop2);
			to = new PrintWriter(toOut);
			to.write("3::::\n"+
					"4:49:59:59:\n"+
					"5:19,39,59:39:39:\n"+
					"6:19,39,59:19,59:19,59:\n"+
					"7:19,39,59:39:39:\n"+
					"8:19,59:19,59:19,59:\n"+
					"9:39:39:39:\n"+
					"10:19,59:19,59:19,59:\n"+
					"11:39:39:39:\n"+
					"12:19,59:19,59:19,59:\n"+
					"13:39:39:39:\n"+
					"14:19,59:19,59:19,59:\n"+
					"15:19,39,59:39:39:\n"+
					"16:19,39,59:19,59:19,59:\n"+
					"17:19,39,59:39:39:\n"+
					"18:19,39:19,59:19,59:\n"+
					"19:04,44:39:39:\n"+
					"20:21:19,59:19,59:\n"+
					"21:01,41:39:39:\n"+
					"22:31:19:19:\n"+
					"23:11C,51C:09C,49C:09C,49C:\n"+
					"0::::\n"+
					"1::::\n"+
					"2::::\n");
			to.flush();
			to.close();
			} catch (FileNotFoundException e) {
				fail("Nie zapisano do pliku");
			}
		
		try {
			toOut = new FileOutputStream(testFileBuStop3);
			to = new PrintWriter(toOut);
			to.write("999test");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
			toOut = new FileOutputStream(testFileBuStop4);
			to = new PrintWriter(toOut);
			to.write("999test");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//kolejka wyszukañ
		ArrayList<Search> toSearch = new ArrayList<Search>();
		
		toSearch.add(new Search("Przystanek2","Przystanek1",0,12,05,3));
		toSearch.add(new Search("Przystanek2","Przystanek1",1,13,05,2));
		toSearch.add(new Search("Przystanek2","Przystanek1",2,14,05,1));
		
		//przygotowanie obiektu i testy
		new Browser().search(toSearch);
				
		//Sprawdzanie
		assertEquals(true,new String(outContent.toByteArray())
				.contains("Mo¿esz skorzystaæ z linii: 999"));
		assertEquals(true,new String(outContent.toByteArray())
				.contains("12:19,59\n13:39\n14:19,59\n15:19,39,59"));
		assertEquals(true,new String(outContent.toByteArray())
				.contains("13:39\n14:19,59\n15:39"));
		assertEquals(true,new String(outContent.toByteArray())
				.contains("14:19,59\n15:39"));	
		
		//usuwanie plików
		if(testFileBuStop1.exists())
		{
			if(!testFileBuStop1.delete())
			{
				fail("Nie usuniêto pliku");
			}
			if(testFileBuStop1.getParentFile().delete())
			{
				fail("Usuniêto katalog zbyt wczeœnie");
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
			if(!testFileBuStop2.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
		
		if(testFileBuStop3.exists())
		{
			if(!testFileBuStop3.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
		
		if(testFileBuStop4.exists())
		{
			if(!testFileBuStop4.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}

	@Test
	public void testNotFoundSerch() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		//Przygotowanie plików z danymi
		File testFileBuStop1 = new File("999test/Przystanek1");
		try {
			if(!testFileBuStop1.getParentFile().mkdir())
			{
				fail("Nie utworzono katalogu");
			}
			if(!testFileBuStop1.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop2 = new File("999test/Przystanek2");
		try {
			if(!testFileBuStop2.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop3 = new File("buStops/Przystanek1");
		try {
			if(!testFileBuStop3.createNewFile())
			{
				fail("Nie utworzono pliku");
			}
		} catch (IOException e) {
			fail("Nie utworzono pliku");
		}
		
		File testFileBuStop4 = new File("buStops/Przystanek2");
		try {
			if(!testFileBuStop4.createNewFile())
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
			to.write("3::::\n"+
					"4::::\n"+
					"5:00,30,50:10,50:10,50:\n"+
					"6:11,31,51:31:31:\n"+
					"7:11,31,51:11,51:11,51:\n"+
					"8:11,31:31:31:\n"+
					"9:11,51:11,51:11,51:\n"+
					"10:31:31:31:\n"+
					"11:11,51:11,51:11,51:\n"+
					"12:31:31:31:\n"+
					"13:12,52:11,51:11,51:\n"+
					"14:32:31:31:\n"+
					"15:12,32,52:11,51:11,51:\n"+
					"16:12,32,52:31:31:\n"+
					"17:11,31,51:11,51:11,51:\n"+
					"18:11,31,51:31:31:\n"+
					"19:16,56:11,51:11,51:\n"+
					"20:33:31:31:\n"+
					"21:12,52:10,50:10,50:\n"+
					"22:42:30:30:\n"+
					"23::::\n"+
					"0::::\n"+
					"1::::\n"+
					"2::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
			toOut = new FileOutputStream(testFileBuStop2);
			to = new PrintWriter(toOut);
			to.write("3::::\n"+
					"4:49:59:59:\n"+
					"5:19,39,59:39:39:\n"+
					"6:19,39,59:19,59:19,59:\n"+
					"7:19,39,59:39:39:\n"+
					"8:19,59:19,59:19,59:\n"+
					"9:39:39:39:\n"+
					"10:19,59:19,59:19,59:\n"+
					"11:39:39:39:\n"+
					"12:19,59:19,59:19,59:\n"+
					"13:39:39:39:\n"+
					"14:19,59:19,59:19,59:\n"+
					"15:19,39,59:39:39:\n"+
					"16:19,39,59:19,59:19,59:\n"+
					"17:19,39,59:39:39:\n"+
					"18:19,39:19,59:19,59:\n"+
					"19:04,44:39:39:\n"+
					"20:21:19,59:19,59:\n"+
					"21:01,41:39:39:\n"+
					"22:31:19:19:\n"+
					"23:11C,51C:09C,49C:09C,49C:\n"+
					"0::::\n"+
					"1::::\n"+
					"2::::\n");
			to.flush();
			to.close();
			} catch (FileNotFoundException e) {
				fail("Nie zapisano do pliku");
			}
		
		try {
			toOut = new FileOutputStream(testFileBuStop3);
			to = new PrintWriter(toOut);
			to.write("999test");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
			toOut = new FileOutputStream(testFileBuStop4);
			to = new PrintWriter(toOut);
			to.write("999testdifferent");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//kolejka wyszukañ
		ArrayList<Search> toSearch = new ArrayList<Search>();
		
		toSearch.add(new Search("Przystanek2","Przystanek1",0,12,05,3));
		toSearch.add(new Search("Przystanek2","Przystanek1",1,13,05,2));
		toSearch.add(new Search("Przystanek2","Przystanek1",2,14,05,1));
		
		//przygotowanie obiektu i testy
		new Browser().search(toSearch);
				
		//Sprawdzanie

		assertEquals(
"Dla przystanków: Przystanek2 i Przystanek1\r\n"+
"Nie znaleziono bezpoœredniego po³¹czenia pomiêdzy przystankami\r\n"+
"Dla przystanków: Przystanek2 i Przystanek1\r\n"+
"Nie znaleziono bezpoœredniego po³¹czenia pomiêdzy przystankami\r\n"+
"Dla przystanków: Przystanek2 i Przystanek1\r\n"+
"Nie znaleziono bezpoœredniego po³¹czenia pomiêdzy przystankami\r\n"
				,new String(outContent.toByteArray()));	
		
		//usuwanie plików
		if(testFileBuStop1.exists())
		{
			if(!testFileBuStop1.delete())
			{
				fail("Nie usuniêto pliku");
			}
			if(testFileBuStop1.getParentFile().delete())
			{
				fail("Usuniêto katalog zbyt wczeœnie");
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
			if(!testFileBuStop2.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
		
		if(testFileBuStop3.exists())
		{
			if(!testFileBuStop3.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
		
		if(testFileBuStop4.exists())
		{
			if(!testFileBuStop4.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}

}
