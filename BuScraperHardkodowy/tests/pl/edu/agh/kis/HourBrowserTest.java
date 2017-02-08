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

import org.junit.Test;

/**
 * Testy wyszukiwarki godzin, osobne przypadki linii nocnej, dziennej autobusowej
 * oraz tramwajowej, oraz kilka mo¿liwych przypadków niepowodzenia
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class HourBrowserTest {

	@Test
	public void testDayBusSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			toOut = new FileOutputStream(testFileBuStop2);
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
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());

		assertEquals(true,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				15, 20, 2, 0));
		
		//Sprawdzanie
		assertEquals(3,hourBrowser.getHours().size());
		assertEquals("15:39,59",hourBrowser.getHours().get(0));
		assertEquals("16:19,39,59",hourBrowser.getHours().get(1));
		assertEquals("17:19,39,59",hourBrowser.getHours().get(2));
		
		hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				10, 30, 3, 1);
		
		//Sprawdzanie
		assertEquals(4,hourBrowser.getHours().size());
		assertEquals("10:59",hourBrowser.getHours().get(0));
		assertEquals("11:39",hourBrowser.getHours().get(1));
		assertEquals("12:19,59",hourBrowser.getHours().get(2));
		assertEquals("13:39",hourBrowser.getHours().get(3));

		
		hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				20, 59, 1, 2);
		
		//Sprawdzanie
		assertEquals(2,hourBrowser.getHours().size());
		assertEquals("20:59",hourBrowser.getHours().get(0));
		assertEquals("21:39",hourBrowser.getHours().get(1));		
		
		//test nie powinno byæ warningów -- konsola pusta
		assertEquals("\n\n\n\n",new String(outContent.toByteArray()));				
		
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}

	
	@Test
	public void testNightBusSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(true,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				23, 56, 3, 0));
		
		//Sprawdzanie
		assertEquals(3,hourBrowser.getHours().size());
		assertEquals("0:03,33",hourBrowser.getHours().get(0));
		assertEquals("1:03,33",hourBrowser.getHours().get(1));
		assertEquals("2:33",hourBrowser.getHours().get(2));

		assertEquals(true,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				02, 4, 1, 1));
		
		//Sprawdzanie
		assertEquals(2,hourBrowser.getHours().size());
		assertEquals("2:33",hourBrowser.getHours().get(0));
		assertEquals("3:03,33",hourBrowser.getHours().get(1));

		assertEquals(true,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				00, 0, 2, 2));
		
		//Sprawdzanie
		assertEquals(3,hourBrowser.getHours().size());
		assertEquals("0:03,33",hourBrowser.getHours().get(0));
		assertEquals("1:03,33",hourBrowser.getHours().get(1));
		assertEquals("2:33",hourBrowser.getHours().get(2));
		
		//sprawdzanie czy konsola pusta
		//test nie powinno byæ warningów -- konsola pusta
		assertEquals("\n\n\n\n",new String(outContent.toByteArray()));				
		
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testWrongDirectionSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
			toOut = new FileOutputStream(testFileBuStop2);
			to = new PrintWriter(toOut);
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				23, 56, 3, 0));
		
		//Sprawdzanie
		assertEquals(0,hourBrowser.getHours().size());

		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				02, 4, 1, 1));
		
		//Sprawdzanie
		assertEquals(0,hourBrowser.getHours().size());

		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				00, 0, 2, 2));
		
		//Sprawdzanie
		assertEquals(0,hourBrowser.getHours().size());
		
		//sprawdzanie czy konsola pusta
		//test nie powinno byæ warningów -- konsola pusta
		assertEquals("\n\n\n\n",new String(outContent.toByteArray()));				
		
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testWrongDataFormat1SearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:g\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		//Test b³¹d na koñcu linijki
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				23, 56, 3, 0));
		assertEquals(0,hourBrowser.getHours().size());
		assertEquals(true,new String(outContent.toByteArray()).
				contains("1:19,49:19,49:19,49:g, Niepoprawny format linijki"));				
		
		
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
		
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testWrongDataFormat2SearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"D1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		//Test b³¹d na koñcu linijki
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				23, 56, 3, 0));
		assertEquals(0,hourBrowser.getHours().size());
		assertEquals(true,new String(outContent.toByteArray()).
				contains("D1:19,49:19,49:19,49:, Niepoprawny format linijki"));				
		
		
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
		
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testWrongDataFormat3SearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"243:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		//Test b³¹d na koñcu linijki
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				23, 56, 3, 0));
		assertEquals(0,hourBrowser.getHours().size());
		assertEquals(true,new String(outContent.toByteArray()).
				contains("243:49:19,49:49:, Niepoprawny format linijki"));				
		
		
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
		
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testWrongDataFormat4SearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5:::;\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		//Test b³¹d na koñcu linijki
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				23, 56, 3, 0));
		assertEquals(0,hourBrowser.getHours().size());
		assertEquals(true,new String(outContent.toByteArray()).
				contains("5:::;, Niepoprawny format linijki"));				
		
		
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
		
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testBadUserHourConditionsSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				25, 56, 3, 0));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podana godzina wykracza poza dozwolone godziny"));	
		assertEquals(0,hourBrowser.getHours().size());

		
		hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				-1, 56, 3, 0));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podana godzina wykracza poza dozwolone godziny"));	
		assertEquals(0,hourBrowser.getHours().size());
		
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testBadUserMinutesConditionsSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, 60, 3, 0));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podana minuta wykracza poza dozwolone minuty"));	
		assertEquals(0,hourBrowser.getHours().size());

		
		hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, -1, 3, 0));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podana minuta wykracza poza dozwolone minuty"));	
		assertEquals(0,hourBrowser.getHours().size());
		
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testBadUserMaxTimeConditionsSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, 50, 24, 0));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podany zakres godzin wykracza poza rozk³ad"));	
		assertEquals(0,hourBrowser.getHours().size());

		
		hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, 40, -1, 0));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podany zakres godzin wykracza poza rozk³ad"));	
		assertEquals(0,hourBrowser.getHours().size());
				
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	@Test
	public void testBadUserTypeOfDayConditionsSearchHours() {
		//Przygotowanie strumienia
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
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
			to.write("22::::\n"+
					"23:33:33:33:\n"+
					"0:03,33:03,33:03,33:\n"+
					"1:03,33:03,33:03,33:\n"+
					"2:33:03,33:33:\n"+
					"3:33:03,33:33:\n"+
					"4:33A:03,33A:33A:\n"+
					"5::::\n");
			to.flush();
			to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		try {
		toOut = new FileOutputStream(testFileBuStop2);
		to = new PrintWriter(toOut);
		to.write("22::::\n"+
				"23:49:49:49:\n"+
				"0:19,49:19,49:19,49:\n"+
				"1:19,49:19,49:19,49:\n"+
				"2:49:19,49:49:\n"+
				"3:49:19,49:49:\n"+
				"4::19::\n"+
				"5::::\n");
		to.flush();
		to.close();
		} catch (FileNotFoundException e) {
			fail("Nie zapisano do pliku");
		}
		
		//przygotowanie obiektu i testy
		HourBrowser hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, 50, 2, 3));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podano niepoprawny rodzaj dnia"));	
		assertEquals(0,hourBrowser.getHours().size());

		
		hourBrowser = new HourBrowser(new ConsoleAppender());
		
		assertEquals(false,hourBrowser.searchHours("tests/Przystanek1", "tests/Przystanek2", 
				12, 40, 2, -1));
		
		//Sprawdzanie		
		assertEquals(true,new String(outContent.toByteArray()).
				contains("Podano niepoprawny rodzaj dnia"));	
		assertEquals(0,hourBrowser.getHours().size());
				
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
		//czyszczenie wyjœcia
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
}
