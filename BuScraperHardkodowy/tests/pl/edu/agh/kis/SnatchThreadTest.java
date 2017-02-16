package pl.edu.agh.kis;

import pl.edu.agh.kis.storeinfo.FileStoreBusInfo;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Test klasy SnatchThread
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class SnatchThreadTest {

	@Test
	public void testRunRuczaj()  {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage23Ruczaj");
		if(!testFile.exists())
		{
			fail("Brak ¿¹danego pliku testowego");
		}
		
		String line = "";
		StringBuilder XMLDocument = new StringBuilder();
		
		try (BufferedReader buffReader1 = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"))) {	
		
			while((line = buffReader1.readLine()) != null)
			{
				XMLDocument.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		
		try {
			pages.addPage(XMLDocument.toString());
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader buffReader2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"))) {	
		
			while((line = buffReader2.readLine()) != null)
			{
				expected.append(line);
			}
			
			buffReader2.close();

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader buffReader3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFile),"UTF-8"))) {	
		
			while((line = buffReader3.readLine()) != null)
			{
				got.append(line);
			}

			buffReader3.close();

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
	
	@Test
	public void testRunCentralna() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage62Centralna");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {
		
			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
	
	@Test
	public void testRunConrada() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage118Conrada");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzone");

		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			

		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {
		
			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
	
	@Test
	public void testRunStruga() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage139Struga");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {
		
			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
	
	@Test
	public void testRunBibice() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage257Bibice");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {

			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
	
	@Test
	public void testRunArka() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage601Arka");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {
		
			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
	
	@Test
	public void testRunProsta() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage643Prosta");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {
		
			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
	
	@Test
	public void testRunCzyzyny() {

		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		String testRoot = "tests/testData/";
		String snatchTestRoot = "tests/testData";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		Map<String,String> xPaths = new HashMap<String,String>();
		xPaths.put("XPathBusStopName", 
				"//td/p[@style=' font-size: 24px; text-align: left; white-space: nowrap;']");
		xPaths.put("XPathLineNumber", 
				"//td/div/p[@style=' font-size: 40px;']");
		xPaths.put("XPathLineDirection", 
				"//table/tr/td/table/tr/td/div[@style=' text-align: left; white-space: "
				+ "nowrap; border-left: solid black; border-radius: 20px; padding: 10px;']");
		xPaths.put("XPathHours", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][1]");
		xPaths.put("XPathMinutesOrdinary", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][2]");
		xPaths.put("XPathMinutesSaturday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][3]");
		xPaths.put("XPathMinutesSunday", 
				"//td[text()=\"Godzina\"]/parent::tr/following-sibling::tr"
				+ "/td[not(@colspan)][4]");
		
		SnatchThread s1 = new SnatchThread(999,pages,
				new FileStoreBusInfo(snatchTestRoot),xPaths);

		//Przygotowanie danych testowych
		File testFile = new File("Tests/AnaliseHTMLPage664Czy¿yny");
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
		} catch (InterruptedException e1) {
			fail("B³¹d przy próbie dodania strony");
		}
		
		//Odpalmy to!
		s1.start();
		try {
			s1.join();
		} catch (InterruptedException e1) {
			fail("Niepoprawnie wybudzony");
		}
		
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
		
		try (BufferedReader from2 = new BufferedReader(new InputStreamReader
				(new FileInputStream(expectedFile)))) {
		
			while((line = from2.readLine()) != null)
			{
				expected.append(line);
			}
			
		} catch (IOException e) {
			fail("Pojawi³ siê wyj¹tek przy czytaniu z pliku oczekiwanego");
		}
		
		try (BufferedReader from3 = new BufferedReader(new InputStreamReader
				(new FileInputStream(gotFile)))) {

			while((line = from3.readLine()) != null)
			{
				got.append(line);
			}

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
