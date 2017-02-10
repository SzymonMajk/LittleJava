package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

/**
 * Dwa proste testy dla klasy DownloadThread
 * @author Szymon Majkut
 * @varsion 1.3
 *
 */
public class DownloadThreadTest {

	/**
	 * Test bêdzie polega³ na przygotowaniu dwóch zestawów testowych, jednego z headerem
	 * posiadaj¹cym informacjê o pozytywnym odbiorze, drugiego który stwierdzi, ¿e nie istnieje
	 * taki zasób, a nastêpnie sprawdzenie czy w kolejce znajduje siê tylko ten pierwszy
	 * @throws IOException wyrzucany w przypadku b³êdów wejœcia wyjœcia
	 * @throws InterruptedException wyrzuany w przypadku b³êdnego wybudzenia w¹tków
	 */
	@Test
	public void testRun() throws IOException, InterruptedException {
		
		//Utworzenie plików oraz strumieni plikowych do testu, bufora,
		//requests oraz dwóch w¹tków
		File testFileOut1 = new File("tests/RequestTestFile1");
		if(!testFileOut1.createNewFile())
		{
			fail("Nie utworzono pliku");
		}
				
		File testFileIn1 = new File("tests/RespondTestFile1");
		if(!testFileIn1.createNewFile())
		{
			fail("Nie utworzono pliku");
		}
		
		File testFileOut2 = new File("tests/RequestTestFile2");
		if(!testFileOut2.createNewFile())
		{
			fail("Nie utworzono pliku");
		}
				
		File testFileIn2 = new File("tests/RespondTestFile2");
		if(!testFileIn2.createNewFile())
		{
			fail("Nie utworzono pliku");
		}
				
		FileDownloader downloader1 = new FileDownloader("tests/RespondTestFile1",
				"tests/RequestTestFile1");
		FileDownloader downloader2 = new FileDownloader("tests/RespondTestFile2",
				"tests/RequestTestFile2");
				
		BlockingQueuePagesBuffer buffer = new BlockingQueuePagesBuffer(5);
		BlockingQueue<String> request = new ArrayBlockingQueue<String>(3);
		request.add("Zapytanie1");
		request.add("Zapytanie2");

		//Przygotowanie pliku z odpowiedzi¹
		OutputStream toOut = new FileOutputStream(testFileIn1);
		PrintWriter to = new PrintWriter(toOut);
		to.write("HTTP/1.1 200 OK\n\nBodyOdpowiedz1");
		to.flush();
		to.close();
		
		toOut = new FileOutputStream(testFileIn2);
		to = new PrintWriter(toOut);
		to.write("HTTP/1.1 404 NOT FOUND\n\nBodyOdpowiedz2");
		to.flush();
		to.close();
		
		//Tworzenie dwóch obiektów i wykonywanie zapytañ 
		DownloadThread testThread1 = new DownloadThread(999,request,buffer,
				downloader1);
		DownloadThread testThread2 = new DownloadThread(998,request,buffer,
				downloader2);

		testThread1.start();
		testThread2.start();

		testThread1.join();
		testThread2.join();
		
		//Sprawdzanie zawartoœci kolejki powinna trafiæ tylko odpowiedz poprawnego zapytania
		assertEquals("BodyOdpowiedz1",buffer.takePage());
		assertEquals(true,buffer.isEmpty());

		//Usuwanie plików
		if(testFileIn1.exists())
		{
			if(!testFileIn1.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn1 nie istnia³!");
		}
				
		if(testFileOut1.exists())
		{
			if(!testFileOut1.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut1 nie istnia³!");
		}

		if(testFileIn2.exists())
		{
			if(!testFileIn2.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn2 nie istnia³!");
		}
				
		if(testFileOut2.exists())
		{
			if(!testFileOut2.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut2 nie istnia³!");
		}
	}
	
	/**
	 * Test metody wewn¹trz DownloadThread, która jest dosyæ wa¿na
	 * i warto by³oby jej zrobiæ osobny test
	 * @throws IOException
	 */
	@Test
	public void testRequest() throws IOException {
		
		//Utworzenie plików oraz strumieni plikowych do testu oraz bufora
		File testFileOut = new File("tests/RequestTestFile");
		if(!testFileOut.createNewFile())
		{
			fail("Nie utworzono pliku");
		}
		
		File testFileIn = new File("tests/RespondTestFile");
		if(!testFileIn.createNewFile())
		{
			fail("Nie utworzono pliku");
		}
		
		FileDownloader downloader = new FileDownloader("tests/RequestTestFile",
				"tests/RespondTestFile");
		
		BlockingQueuePagesBuffer buffer = new BlockingQueuePagesBuffer(5);
	
		//Przygotowanie pliku z odpowiedzi¹
		OutputStream toOut = new FileOutputStream(testFileOut);
		PrintWriter to = new PrintWriter(toOut);
		to.write("HeaderOdpowiedz\n\nBodyOdpowiedz");
		to.flush();
		to.close();
		
		//Tworzymy nowy obiekt w¹tku, podpinaj¹c do niego strumienie do plików-testów
		DownloadThread testThread = new DownloadThread(997,new ArrayBlockingQueue<String>(5),
				buffer,downloader);
		
		String[] respondFromServer = testThread.respond("Zapytanie");
		
		//Odczytujemy zawartoœæ pliku, do którego zapisaliœmy zapytanie do servera
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFileIn)));
		
		String line = "";
		StringBuilder requestToServer = new StringBuilder();
		
		try {
			while((line = from.readLine()) != null)
			{
				requestToServer.append(line);
			}
			
			from.close();

		} catch (IOException e) {
			fail("B³¹d przy czytaniu z pliku");
		}
		
		//Sprawdzenie czy zadane zapytanie jest zgodne z oczekiwaniami
		//oraz czy na to zapytanie otrzymaliœmy odpowiedni¹ odpowiedz
		assertEquals("Zapytanie",requestToServer.toString());
		assertEquals("HeaderOdpowiedz",respondFromServer[0]);
		assertEquals("BodyOdpowiedz",respondFromServer[1]);
		
		//Usuwanie plików
		if(testFileIn.exists())
		{
			if(!testFileIn.delete())
			{
				fail("Nie usuniêto pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn nie istnia³!");
		}
		
		if(testFileOut.exists())
		{
			if(!testFileOut.delete())
			{
				fail("Nie utworzono pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia³!");
		}
	}
}
//TODO napiszmy testy dla ró¿nych mo¿liwoœci! Co tak ubogo? Niech dzia³a ró¿nie dla
//Kodów 302, 301, 502, 100 chocia¿by, a co!