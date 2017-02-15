package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

/**
 * Test przystanku standardowego, test czy znaki niedozwolone s� odpowiednio usuwane oraz
 * testy przypadk�w w kt�rych nie powinny by� sk�adowane dane.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class FileStoreBusInfoTest {

	/**
	 * Test dla standardowego przypadku
	 */
	@Test
	public void testStoreInfoOrdinary1() {
		
		//Przygotowanie obiekt�w oraz danych
		String testRoot = "tests";
		FileStoreBusInfo f = new FileStoreBusInfo(testRoot);

		HashMap<String,String> info= new HashMap<String,String>();
		info.put("buStopName", "PrzystanekTest");
		info.put("lineNumber", "999");
		info.put("direction", "OdTestDoKierunek");
		info.put("hours", "Godzina\n22\n1\n1\n1\n\n\n23\n1\n1\n1\n\n\n"
				+ "0\n1\n1\n1\n\n\n1\n1\n1\n1\n\n\n2\n1\n1\n1\n\n\n"
				+ "3\n1\n1\n1\n\n\n4\n1\n1\n1\n\n\n5\n1\n1\n1\n\n\n");
		
		String line;
		StringBuilder got = new StringBuilder();
		StringBuilder gotHelp = new StringBuilder();
		String expected = "PrzystanekTest23:1:1:1:0:1:1:1:1:1:1:1:2:1:1:1:3:1:1:1:4:1:1:1:";
		String expectedHelp = "999OdTestDoKierunek";

		//Uruchomienie testowanej funkcji
		f.storeInfo(info);
		
		//Sprawdzanie zawarto�ci utworzonych plik�w i ich usuni�cie
		File testFile = new File("tests/999OdTestDoKierunek/PrzystanekTest");
		File testHelpBrowserFile = new File("tests/buStops/PrzystanekTest");
		
		if(testFile.exists() && testHelpBrowserFile.exists())
		{					
			line = "";
			
			try ( BufferedReader bufferedReader = 
					new BufferedReader(new FileReader(testFile))) {
			
				while((line = bufferedReader.readLine()) != null)
				{
					got.append(line);
				}

			} catch (IOException e) {
				fail("Wyj�tek podczas czytania pliku z danymi");
			}
			
			try ( BufferedReader bufferedReader = 
					new BufferedReader(new FileReader(testHelpBrowserFile))) {
			
				while((line = bufferedReader.readLine()) != null)
				{
					gotHelp.append(line);
				}

			} catch (IOException e) {
				fail("Wyj�tek podczas czytania pliku z danymi");
			}
			
			if(!testFile.delete())
			{
				fail("Nie usuni�to pliku z danymi");
			}
			
			if(!testFile.getParentFile().delete())
			{
				fail("Nie katalogu pliku z danymi");
			}
			
			if(!testHelpBrowserFile.delete())
			{
				fail("Nie usuni�to pliku z danymi pomocniczymi dla przegl�darki");
			}
			
			if(!testHelpBrowserFile.getParentFile().delete())
			{
				fail("Nie katalogu pliku z danymi pomocniczymi dla przegl�darki");
			}
		}
		else
		{
			fail("Nie utwrzono plik�w z danymi");
		}
		
		//Test tego co oczekiwane i co znalaz�o si� w pliku
		assertEquals(expected,got.toString());
		assertEquals(expectedHelp,gotHelp.toString());
	}
	
	/**
	 * Test dla przypadku standardowego, gdzie wyst�puj� znaki niedozwolone przy nazwach
	 * plik�w
	 */
	@Test
	public void testStoreInfoOrdinary2() {
		
		//Przygotowanie obiekt�w oraz danych
		String testRoot = "tests";
		FileStoreBusInfo f = new FileStoreBusInfo(testRoot);

		HashMap<String,String> info= new HashMap<String,String>();
		info.put("buStopName", "Przystanek.\"Test\"");
		info.put("lineNumber", "999");
		info.put("direction", "Od<TestDo>Kierun*ek");
		info.put("hours", "Godzina\n22\n1\n1\n1\n\n\n23\n1\n1\n1\n\n\n"
				+ "0\n1\n1\n1\n\n\n1\n1\n1\n1\n\n\n2\n1\n1\n1\n\n\n"
				+ "3\n1\n1\n1\n\n\n4\n1\n1\n1\n\n\n5\n1\n1\n1\n\n\n");
		
		String line;
		StringBuilder got = new StringBuilder();
		StringBuilder gotHelp = new StringBuilder();
		String expected = 
				"Przystanek.\"Test\"23:1:1:1:0:1:1:1:1:1:1:1:2:1:1:1:3:1:1:1:4:1:1:1:";
		String expectedHelp = 
				"999Od<TestDo>Kierun*ek";

		//Uruchomienie testowanej funkcji
		f.storeInfo(info);
		
		//Sprawdzanie zawarto�ci utworzonych plik�w i ich usuni�cie
		File testFile = new File("tests/999OdTestDoKierunek/PrzystanekTest");
		File testHelpBrowserFile = new File("tests/buStops/PrzystanekTest");
		
		if(testFile.exists() && testHelpBrowserFile.exists())
		{					
			line = "";
			
			try ( BufferedReader bufferedReader = 
					new BufferedReader(new FileReader(testFile))) {
			
				while((line = bufferedReader.readLine()) != null)
				{
					got.append(line);
				}

			} catch (IOException e) {
				fail("Wyj�tek podczas czytania pliku z danymi");
			}
			
			try ( BufferedReader bufferedReader = 
					new BufferedReader(new FileReader(testHelpBrowserFile))) {
			
				while((line = bufferedReader.readLine()) != null)
				{
					gotHelp.append(line);
				}

			} catch (IOException e) {
				fail("Wyj�tek podczas czytania pliku z danymi");
			}
			
			if(!testFile.delete())
			{
				fail("Nie usuni�to pliku z danymi");
			}
			
			if(!testFile.getParentFile().delete())
			{
				fail("Nie katalogu pliku z danymi");
			}
			
			if(!testHelpBrowserFile.delete())
			{
				fail("Nie usuni�to pliku z danymi pomocniczymi dla przegl�darki");
			}
			
			if(!testHelpBrowserFile.getParentFile().delete())
			{
				fail("Nie katalogu pliku z danymi pomocniczymi dla przegl�darki");
			}
		}
		else
		{
			fail("Nie utwrzono plik�w z danymi");
		}
		
		//Test tego co oczekiwane i co znalaz�o si� w pliku
		assertEquals(expected,got.toString());
		assertEquals(expectedHelp,gotHelp.toString());
	}
	
	/**
	 * Test dla przypadku z b��dnym numerem linii -- pliki nie powinny zosta� utworzone
	 * plik�w
	 */
	@Test
	public void testStoreInfoExtrardinary1() {
		
		//Przygotowanie obiekt�w oraz danych
		String testRoot = "tests";
		FileStoreBusInfo f = new FileStoreBusInfo(testRoot);

		HashMap<String,String> info= new HashMap<String,String>();
		info.put("buStopName", "PrzystanekTest");
		info.put("lineNumber", "99*9");
		info.put("direction", "Od:TestDo:Kierunek");
		info.put("hours", "Godzina\n22\n1\n1\n1\n\n\n23\n1\n1\n1\n\n\n"
				+ "0\n1\n1\n1\n\n\n1\n1\n1\n1\n\n\n2\n1\n1\n1\n\n\n"
				+ "3\n1\n1\n1\n\n\n4\n1\n1\n1\n\n\n5\n1\n1\n1\n\n\n");

		//Uruchomienie testowanej funkcji
		f.storeInfo(info);
		
		//Sprawdzanie zawarto�ci utworzonych plik�w i ich usuni�cie
		File testFile = new File("tests/999OdTestDoKierunek/PrzystanekTest");
		File testHelpBrowserFile = new File("tests/buStops/PrzystanekTest");
		
		//Je�eli okaza�o by si�, �e istniej�, musimy je pousuwa�, ale sprawdzamy
		//kt�ry istnia�, wi�c odnotujemy od razu sprawdzenie
		if(testFile.exists() && testHelpBrowserFile.exists())
		{			
			if(!testFile.delete())
			{
				assertFalse(testFile.exists());
			}
			
			if(!testFile.getParentFile().delete())
			{
				assertFalse(testFile.getParentFile().exists());
			}
			
			if(!testHelpBrowserFile.delete())
			{
				assertFalse(testHelpBrowserFile.exists());
			}
			
			if(!testHelpBrowserFile.getParentFile().delete())
			{
				assertFalse(testHelpBrowserFile.getParentFile().exists());
			}
		}
	}
	
	/**
	 * Test dla przypadku z pustych String�w -- pliki nie powinny zosta� utworzone
	 * plik�w
	 */
	@Test
	public void testStoreInfoExtrardinary2() {
		
		//Przygotowanie obiekt�w oraz danych
		String testRoot = "tests";
		FileStoreBusInfo f = new FileStoreBusInfo(testRoot);

		HashMap<String,String> info= new HashMap<String,String>();
		info.put("buStopName", "");
		info.put("lineNumber", "");
		info.put("direction", "");
		info.put("hours", "");

		//Uruchomienie testowanej funkcji
		f.storeInfo(info);
		
		//Sprawdzanie zawarto�ci utworzonych plik�w i ich usuni�cie
		File testFile = new File("tests/999OdTestDoKierunek/PrzystanekTest");
		File testHelpBrowserFile = new File("tests/buStops/PrzystanekTest");
		
		//Je�eli okaza�o by si�, �e istniej�, musimy je pousuwa�, ale sprawdzamy
		//kt�ry istnia�, wi�c odnotujemy od razu sprawdzenie
		if(testFile.exists() && testHelpBrowserFile.exists())
		{			
			if(!testFile.delete())
			{
				assertFalse(testFile.exists());
			}
			
			if(!testFile.getParentFile().delete())
			{
				assertFalse(testFile.getParentFile().exists());
			}
			
			if(!testHelpBrowserFile.delete())
			{
				assertFalse(testHelpBrowserFile.exists());
			}
			
			if(!testHelpBrowserFile.getParentFile().delete())
			{
				assertFalse(testHelpBrowserFile.getParentFile().exists());
			}
		}
	}
	
	/**
	 * Test dla przypadku otrzymania nulli -- pliki nie powinny zosta� utworzone
	 * plik�w
	 */
	@Test
	public void testStoreInfoExtrardinary3() {
		
		//Przygotowanie obiekt�w oraz danych
		String testRoot = "tests";
		FileStoreBusInfo f = new FileStoreBusInfo(testRoot);

		HashMap<String,String> info= new HashMap<String,String>();
		info.put("buStopName", null);
		info.put("lineNumber", null);
		info.put("direction", null);
		info.put("hours", null);

		//Uruchomienie testowanej funkcji
		f.storeInfo(info);
		
		//Sprawdzanie zawarto�ci utworzonych plik�w i ich usuni�cie
		File testFile = new File("tests/999OdTestDoKierunek/PrzystanekTest");
		File testHelpBrowserFile = new File("tests/buStops/PrzystanekTest");
		
		//Je�eli okaza�o by si�, �e istniej�, musimy je pousuwa�, ale sprawdzamy
		//kt�ry istnia�, wi�c odnotujemy od razu sprawdzenie
		if(testFile.exists() && testHelpBrowserFile.exists())
		{			
			if(!testFile.delete())
			{
				assertFalse(testFile.exists());
			}
			
			if(!testFile.getParentFile().delete())
			{
				assertFalse(testFile.getParentFile().exists());
			}
			
			if(!testHelpBrowserFile.delete())
			{
				assertFalse(testHelpBrowserFile.exists());
			}
			
			if(!testHelpBrowserFile.getParentFile().delete())
			{
				assertFalse(testHelpBrowserFile.getParentFile().exists());
			}
		}
	}
}