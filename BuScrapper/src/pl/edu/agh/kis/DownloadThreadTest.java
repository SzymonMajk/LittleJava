package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

/**
 * Dwa proste testy dla klasy DownloadThread
 * @author Szymon Majkut
 * @varsion 1.1a
 *
 */
public class DownloadThreadTest {

	/**
	 * Test b�dzie polega� na przygotowaniu dw�ch zestaw�w testowych, jednego z headerem
	 * posiadaj�cym informacj� o pozytywnym odbiorze, drugiego kt�ry stwierdzi, �e nie istnieje
	 * taki zas�b, a nast�pnie sprawdzenie czy w kolejce znajduje si� tylko ten pierwszy
	 */
	@Test
	public void testRun() throws IOException, InterruptedException {
		
		//Utworzenie plik�w oraz strumieni plikowych do testu, bufora, requests oraz dw�ch w�tk�w
		File testFileOut1 = new File("RequestTestFile1");
		testFileOut1.createNewFile();
				
		File testFileIn1 = new File("RespondTestFile1");
		testFileIn1.createNewFile();
		
		File testFileOut2 = new File("RequestTestFile2");
		testFileOut2.createNewFile();
				
		File testFileIn2 = new File("RespondTestFile2");
		testFileIn2.createNewFile();
				
		InputStream in1 = new FileInputStream(testFileIn1);
		OutputStream out1 = new FileOutputStream(testFileOut1);
		
		InputStream in2 = new FileInputStream(testFileIn2);
		OutputStream out2 = new FileOutputStream(testFileOut2);
				
		BlockingQueuePagesBuffer buffer = new BlockingQueuePagesBuffer();
		BlockingQueue<String> request = new ArrayBlockingQueue<String>(3);
		request.add("Zapytanie1");
		request.add("Zapytanie2");

		//Przygotowanie pliku z odpowiedzi�
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
		
		//Tworzenie dw�ch obiekt�w i wykonywanie zapyta� 
		DownloadThread testThread1 = new DownloadThread(999,request,buffer,in1, out1);
		DownloadThread testThread2 = new DownloadThread(998,request,buffer,in2, out2);

		testThread1.start();
		testThread2.start();

		testThread1.join();
		testThread2.join();
		
		//Sprawdzanie zawarto�ci kolejki powinna trafi� tylko odpowiedz poprawnego zapytania
		assertEquals("BodyOdpowiedz1",buffer.takePage());
		assertEquals(true,buffer.isEmpty());

		//Usuwanie plik�w
		if(testFileIn1.exists())
		{
			testFileIn1.delete();
		}
		else
		{
			fail("Plik testowy testFileIn1 nie istnia�!");
		}
				
		if(testFileOut1.exists())
		{
			testFileOut1.delete();
		}
		else
		{
			fail("Plik testowy testFileOut1 nie istnia�!");
		}

		if(testFileIn2.exists())
		{
			testFileIn2.delete();
		}
		else
		{
			fail("Plik testowy testFileIn2 nie istnia�!");
		}
				
		if(testFileOut2.exists())
		{
			testFileOut2.delete();
		}
		else
		{
			fail("Plik testowy testFileOut2 nie istnia�!");
		}
	}
	
	/**
	 * Test metody wewn�trz DownloadThread, kt�ra jest dosy� wa�na
	 * i warto by�oby jej zrobi� osobny test
	 * @throws IOException
	 */
	@Test
	public void testRequest() throws IOException {
		
		//Utworzenie plik�w oraz strumieni plikowych do testu oraz bufora
		File testFileOut = new File("RequestTestFile");
		testFileOut.createNewFile();
		
		File testFileIn = new File("RespondTestFile");
		testFileIn.createNewFile();
		
		InputStream in = new FileInputStream(testFileIn);
		OutputStream out = new FileOutputStream(testFileOut);
		
		BlockingQueuePagesBuffer buffer = new BlockingQueuePagesBuffer();
	
		//Przygotowanie pliku z odpowiedzi�
		
		OutputStream toOut = new FileOutputStream(testFileIn);
		PrintWriter to = new PrintWriter(toOut);
		to.write("HeaderOdpowiedz\n\nBodyOdpowiedz");
		to.flush();
		to.close();
		
		//Tworzymy nowy obiekt w�tku, podpinaj�c do niego strumienie do plik�w-test�w
		DownloadThread testThread = new DownloadThread(997,new ArrayBlockingQueue<String>(5),
				buffer,in, out);
		
		String[] respondFromServer = testThread.respond("Zapytanie");
		
		//Odczytujemy zawarto�� pliku, do kt�rego zapisali�my zapytanie do servera
		BufferedReader from = new BufferedReader(new InputStreamReader
				(new FileInputStream(testFileOut)));
		
		String line = "";
		String requestToServer = "";
		
		try {
			while((line = from.readLine()) != null)
			{
				requestToServer = line;
			}
			
			from.close();

		} catch (IOException e) {
			// np. logi zr�bmy tutaj
			//e.printStackTrace();
		}
		
		//Sprawdzenie czy zadane zapytanie jest zgodne z oczekiwaniami
		//oraz czy na to zapytanie otrzymali�my odpowiedni� odpowiedz
		assertEquals("Zapytanie",requestToServer);
		assertEquals("HeaderOdpowiedz",respondFromServer[0]);
		assertEquals("BodyOdpowiedz",respondFromServer[1]);
		
		//Usuwanie plik�w
		if(testFileIn.exists())
		{
			testFileIn.delete();
		}
		else
		{
			fail("Plik testowy testFileIn nie istnia�!");
		}
		
		if(testFileOut.exists())
		{
			testFileOut.delete();
		}
		else
		{
			fail("Plik testowy testFileOut nie istnia�!");
		}
	}
}
