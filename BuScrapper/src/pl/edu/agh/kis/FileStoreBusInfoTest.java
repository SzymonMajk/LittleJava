package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

/**
 * Kilka prostych testów
 * @author Szymon Majkut
 * @version 1.1b
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
		
		FileStoreBusInfo f = new FileStoreBusInfo(new NullAppender());
		
		String s0 = "name=Manierskiego\nhour=220,05,75\nnumber=996\n"
				+ "name=Manierskiego\r\nhour=114,04,75,63\nnumber=996\n"
				+ "name=Manierskiego\r\nhour=113,03,23\nnumber=996\n"
				+ "name=Manierskiego\r\nhour=120,02,75\nnumber=996\n"
				+ "name=Manierskiego\r\nhour=001,01,34\nnumber=996\n";

		f.storeInfo(s0);
		
		String s1 = "name=Przystanek1\nhour=220,06,75\nnumber=999\n"
				+ "name=Przystanek1\r\nhour=114,04,75,63\nnumber=999\n"
				+ "name=Przystanek1\r\nhour=113,03,23\nnumber=999\n"
				+ "name=Przystanek1\r\nhour=120,02,75\nnumber=999\n"
				+ "name=Przystanek1\r\nhour=001,01,34\nnumber=999\n";
		
		f.storeInfo(s1);
		
		String s2 = "name=Przystanek2\nhour=220,05,75\nnumber=999\n"
				+ "name=Przystanek2\r\nhour=114,04,75,63\nnumber=999\n"
				+ "name=Przystanek2\r\nhour=113,03,23\nnumber=999\n"
				+ "name=Przystanek2\r\nhour=120,02,75\nnumber=999\n"
				+ "name=Przystanek2\r\nhour=002,01,34\nnumber=999\n";
		
		f.storeInfo(s2);
		
		//Przypadek - niepoprawna nazwa przystanku
		String s3 = "name=Przystanek1;\nhour=220,05,75\nnumber=998\n"
				+ "name=Przystanek1;\r\nhour=114,04,75,63\nnumber=998\n"
				+ "name=Przystanek1;\r\nhour=113,03,23\nnumber=998\n"
				+ "name=Przystanek1;\r\nhour=120,02,75\nnumber=998\n"
				+ "name=Przystanek1;\r\nhour=001,01,34\nnumber=998\n";
		
		f.storeInfo(s3);
		
		//Przypadek - niepoprawny format godziny
		String s4 = "name=Przystanek2\nhour=220,0t5,75\nnumber=998\n"
				+ "name=Przystanek2\r\nhour=114t,04,75,63\nnumber=998\n"
				+ "name=Przystanek2\r\nhour=113,03t,23\nnumber=998\n"
				+ "name=Przystanek2\r\nhour=120,02,75t\nnumber=998\n"
				+ "name=Przystanek2\r\nhour=0051, 01,34\nnumber=998\n";
		
		f.storeInfo(s4);
		
		//Przypadek - niepoprawny numer przystanku
		String s5 = "name=Przystanek1\nhour=220,05,75\nnumber=9978\n"
				+ "name=Przystanek1\r\nhour=114,04,75,63\nnumber=9978\n"
				+ "name=Przystanek1\r\nhour=113,03,23\nnumber=9978\n"
				+ "name=Przystanek1\r\nhour=120,02,75\nnumber=9978\n"
				+ "name=Przystanek1\r\nhour=001,01,34\nnumber=9978\n";
		
		f.storeInfo(s5);
		
		File testFile;
		File testFileParent;
		
		BufferedReader bufferedReader;
		String expected;
		String line;
		String got;
		
		//Sprawdzenie czy przypadkiem nie utworzono niew³aœciwym plików, je¿eli utworzono ich sprz¹tniêcie
		
		testFile = new File("998/Przystanek1");

		if(testFile.exists())
		{
			fail("Utworzono plik z niepoprawnymi danymi Przystanek1 dla linii 998");
			testFile.delete();
		}
		
		testFileParent = testFile.getParentFile();
		
		if(testFileParent.exists())
		{
			fail("Utworzono katalog dla linii 998");
			testFileParent.delete();
		}
		
		testFile = new File("998/Przystanek2");
		
		if(testFile.exists())
		{
			fail("Utworzono plik z niepoprawnymi danymi Przystanek2 dla linii 998");
			testFile.delete();
		}
		
		testFileParent = testFile.getParentFile();
		
		if(testFileParent.exists())
		{
			fail("Utworzono katalog dla linii 998");
			testFileParent.delete();
		}
		
		testFile = new File("997/Przystanek1");
		
		if(testFile.exists())
		{
			fail("Utworzono plik z niepoprawnymi danymi Przystanek1 dla linii 997");
			testFile.delete();
		}
		
		testFileParent = testFile.getParentFile();
		
		if(testFileParent.exists())
		{
			fail("Utworzono katalog dla linii 998");
			testFileParent.delete();
		}
		
		//Sprawdzanie zawartoœci utworzonych plików i ich usuniêcie
		
		
		
		testFile = new File("996/Manierskiego");

		if(testFile.exists())
		{
			expected = "Dni powszednie:01: 01,34Soboty:13: 03,2314: 04,75,6320:"
					+ " 02,75Niedziele:20: 05,75";
					
			line = "";
			got = "";
			
			bufferedReader = new BufferedReader(new FileReader(testFile));
			
			while((line= bufferedReader.readLine()) != null)
			{
				got += line;
			}
			bufferedReader.close();
			assertEquals(expected,got);
			testFile.delete();
		}
		else
		{
			fail("Nie utworzono pliku przystanku Manierskiego dla linii 996");
		}
		
		testFileParent = testFile.getParentFile();
		
		if(testFileParent.exists())
		{
			testFileParent.delete();
		}
		else
		{
			fail("Nie utworzono katalogu dla linii 996");
		}
		
		testFile = new File("999/Przystanek1");

		if(testFile.exists())
		{
			expected = "Dni powszednie:01: 01,34Soboty:13: 03,2314: 04,75,6320:"
					+ " 02,75Niedziele:20: 06,75";
					
			line = "";
			got = "";
			
			bufferedReader = new BufferedReader(new FileReader(testFile));
			
			while((line= bufferedReader.readLine()) != null)
			{
				got += line;
			}
					
			bufferedReader.close();
			assertEquals(expected,got);
			testFile.delete();
		}
		else
		{
			fail("Nie utworzono pliku przystanku Przystanek1 dla linii 999");
		}
		
		testFileParent = testFile.getParentFile();
		
		if(!testFileParent.exists())
		{
			fail("Nie utworzono katalogu dla linii 999");
		}

		testFile = new File("999/Przystanek2");

		if(testFile.exists())
		{
			expected = "Dni powszednie:02: 01,34Soboty:13: 03,2314: 04,75,6320:"
					+ " 02,75Niedziele:20: 05,75";
					
			line = "";
			got = "";
			
			bufferedReader = new BufferedReader(new FileReader(testFile));
			
			while((line= bufferedReader.readLine()) != null)
			{
				got += line;
			}
					
			bufferedReader.close();
			assertEquals(expected,got);
			testFile.delete();
		}
		else
		{
			fail("Nie utworzono pliku przystanku Przystanek2 dla linii 999");
		}
		
		testFileParent = testFile.getParentFile();
		
		if(testFileParent.exists())
		{
			testFileParent.delete();
		}
		else
		{
			fail("Nie utworzono katalogu dla linii 999");
		}
	}

}
