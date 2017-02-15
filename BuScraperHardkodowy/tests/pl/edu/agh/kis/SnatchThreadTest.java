package pl.edu.agh.kis;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import pl.edu.agh.kis.storeinfo.FileStoreBusInfo;

/**
 * Test klasy SnatchThread
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class SnatchThreadTest {

	/**
	 * Sprawadzamy przypadek przystanku tylko z podzia³em na zwyk³y dzieñ tygodnia i soboty
	 */
	@Test
	public void testRunRuczaj() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage23Ruczaj");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/RuczajResults");
		File gotFile = new File(testRoot+"999OdTestDoKierunek/Ruczaj");
		File buStops = new File(testRoot+"buStops/Ruczaj");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku z podzia³em na Czw/Pt, Pt/Sob-Sob/Nd oraz Nd/Pon
	 */
	@Test
	public void testRunCentralna() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage62Centralna");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/CentralnaResults");
		File gotFile = new File(testRoot+"998OdTestDoKierunek/Centralna");
		File buStops = new File(testRoot+"buStops/Centralna");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku tylko z rozk³adem na dzieñ powszedni
	 */
	@Test
	public void testRunConrada() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage118Conrada");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/ConradaResults");
		File gotFile = new File(testRoot+"997OdTestDoKierunek/Conrada");
		File buStops = new File(testRoot+"buStops/Conrada");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku z rozk³adem na dzieñ powszedni, soboty i œwiêta
	 */
	@Test
	public void testRunStruga() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage139Struga");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/StrugaResults");
		File gotFile = new File(testRoot+"996OdTestDoKierunek/Struga");
		File buStops = new File(testRoot+"buStops/Struga");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku z rozk³adem na dzieñ powszedni, soboty i œwiêta
	 */
	@Test
	public void testRunBibice() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage257Bibice");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/BibiceResults");
		File gotFile = new File(testRoot+"995OdTestDoKierunek/Bibice");
		File buStops = new File(testRoot+"buStops/Bibice");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku z rozk³adem na wszystkie dni tygodnia w jednym polu
	 */
	@Test
	public void testRunArka() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage601Arka");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/ArkaResults");
		File gotFile = new File(testRoot+"994OdTestDoKierunek/Arka");
		File buStops = new File(testRoot+"buStops/Arka");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku z rozk³adem na Pt/Sob-Sob/Nd w jednym polu
	 */
	@Test
	public void testRunProsta() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage643Prosta");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/ProstaResults");
		File gotFile = new File(testRoot+"992OdTestDoKierunek/Prosta");
		File buStops = new File(testRoot+"buStops/Prosta");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
	
	/**
	 * Sprawadzamy przypadek przystanku z rozk³adem na Pon/Wt, Wt/Œr, Œr/Czw w jednym polu
	 */
	@Test
	public void testRunCzyzyny() throws IOException, InterruptedException {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Configurator configurator = new Configurator("Tests/testConf",
				new TaskManager());
		
		SnatchThread s1 = new SnatchThread(999,pages,new FileStoreBusInfo(testRoot),
				configurator.getXPaths());

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage664Czy¿yny");
		if(!testFile.exists())
		{
			fail("Brak ¿adanego pliku testowego");
		}
				
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"));
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		s1.start();
		s1.join();
		
		//Wczytujemy plik porównawczy oraz utwrzony i porównujemy
		StringBuilder expected = new StringBuilder();
		StringBuilder got = new StringBuilder();
		
		File expectedFile = new File("Tests/Czy¿ynyResults");
		File gotFile = new File(testRoot+"991OdTestDoKierunek/Czy¿yny");
		File buStops = new File(testRoot+"buStops/Czy¿yny");
		File testDataFolder = new File(testRoot+"buStops").getParentFile();
		
		if(!expectedFile.exists())
		{
			fail("Brak pliku testowego");
		}
		
		if(!gotFile.exists())
		{
			fail("Brak pliku wynikowego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi³ siê wyj¹tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuniêto pliku");
			}

			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuniêto katalogu");
			}
		}
		else
		{
			fail("Plik zosta³ niew³¹œciwie usuniêty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g³ówny testów nie zosta³ usuniêty poprawnie");
			}
		}
		else
		{
			fail("Katalog g³ówny testów zosta³ wczeœniej usuniêty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa³o siê w plikach
		assertEquals(expected.toString(),got.toString());
	}
}
