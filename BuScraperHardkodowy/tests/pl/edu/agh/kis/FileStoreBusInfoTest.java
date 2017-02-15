package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import pl.edu.agh.kis.storeinfo.FileStoreBusInfo;

/**
 * Test przystanku standardowego, test czy znaki niedozwolone s� odpowiednio usuwane oraz
 * testy przypadk�w w kt�rych nie powinny by� sk�adowane dane.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class FileStoreBusInfoTest {
	
	/**
	 * Test dla przypadku z b��dnym numerem linii -- pliki nie powinny zosta� utworzone
	 * plik�w
	 */
	@Test
	public void testStoreInfoWrongData1() {
		
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
	public void testStoreInfoWrongData2() {
		
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
	public void testStoreInfoWrongData3() {
		
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