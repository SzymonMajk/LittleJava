package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

/**
 * Kilka prostych testów
 * @author Szymon Majkut
 * @version 1.3
 */
public class FileStoreBusInfoTest {

	/**
	 * Tutaj testy g³ównie polegaj¹ce na przejrzeniu kilku przypadków które nie mogê przejœæ
	 * oraz kilku które musz¹ przechodziæ, nastêpnie w tych które nie przesz³y sprawdzanie
	 * czy nie utworzono plików, a w tych które przesz³y sprawdzanie co znajduje siê w
	 * utworzonych plikach
	 */
	@Test
	public void testStoreInfo() throws IOException {
		
		FileStoreBusInfo f = new FileStoreBusInfo();
		
		HashMap<String,String> info= new HashMap<String,String>();
		
		info.put("buStopName", "PrzystanekTest");
		info.put("lineNumber", "999");
		info.put("direction", "OdTestDoKierunek");
		info.put("hours", "Godzina\n22\n1\n1\n1\n\n\n23\n1\n1\n1\n\n\n"
				+ "0\n1\n1\n1\n\n\n1\n1\n1\n1\n\n\n2\n1\n1\n1\n\n\n"
				+ "3\n1\n1\n1\n\n\n4\n1\n1\n1\n\n\n5\n1\n1\n1\n\n\n");
		
		f.storeInfo(info);
		
		File testFile;
		
		BufferedReader bufferedReader;
		String expected;
		String line;
		StringBuilder got;
				
		//Sprawdzanie zawartoœci utworzonych plików i ich usuniêcie
		testFile = new File("999OdTestDoKierunek/PrzystanekTest");

		if(testFile.exists())
		{
			expected = "23:1:1:1:0:1:1:1:1:1:1:1:2:1:1:1:3:1:1:1:4:1:1:1:";
					
			line = "";
			got = new StringBuilder();
			
			bufferedReader = new BufferedReader(new FileReader(testFile));
			
			while((line= bufferedReader.readLine()) != null)
			{
				got.append(line);
			}
			bufferedReader.close();
			assertEquals(expected,got.toString());
			if(!testFile.delete())
			{
				fail("Nie usuniêto pliku");
			}

			bufferedReader.close();
		}
		else
		{
			fail("Nie utworzono pliku przystanku Manierskiego dla linii 996");
		}
		
	}
}
//TODO co prawda w Snatchu ju¿ trochê to testujemy... Ale jakieœ inne warianty te¿ pomyœlmy!