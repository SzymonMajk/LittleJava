package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;

/**
 * Dwa proste testy dla klasy DownloadThread
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class DownloadThreadTest {

	/**
	 * Test b�dzie polega� na przygotowaniu dw�ch zestaw�w testowych, jednego z headerem
	 * posiadaj�cym informacj� o pozytywnym odbiorze, drugiego kt�ry stwierdzi, �e nie istnieje
	 * taki zas�b, a nast�pnie sprawdzenie czy w kolejce znajduje si� tylko ten pierwszy
	 * @throws IOException wyrzucany w przypadku b��d�w wej�cia wyj�cia
	 * @throws InterruptedException wyrzuany w przypadku b��dnego wybudzenia w�tk�w
	 */
	//@Test
	/*public void testRun() throws IOException, InterruptedException {
		
		//Utworzenie plik�w oraz strumieni plikowych do testu, bufora,
		//requests oraz dw�ch w�tk�w
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
		DownloadThread testThread1 = new DownloadThread(999,buffer,request,
				downloader1);
		DownloadThread testThread2 = new DownloadThread(998,buffer,request,
				downloader2);

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
			if(!testFileIn1.delete())
			{
				fail("Nie usuni�to pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn1 nie istnia�!");
		}
				
		if(testFileOut1.exists())
		{
			if(!testFileOut1.delete())
			{
				fail("Nie usuni�to pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut1 nie istnia�!");
		}

		if(testFileIn2.exists())
		{
			if(!testFileIn2.delete())
			{
				fail("Nie usuni�to pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn2 nie istnia�!");
		}
				
		if(testFileOut2.exists())
		{
			if(!testFileOut2.delete())
			{
				fail("Nie usuni�to pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileOut2 nie istnia�!");
		}
	}
	*/
	/**
	 * Test metody wewn�trz DownloadThread, kt�ra jest dosy� wa�na
	 * i warto by�oby jej zrobi� osobny test
	 * @throws IOException
	 */
	//@Test
	/*public void testRequest() throws IOException {
		
		//Utworzenie plik�w oraz strumieni plikowych do testu oraz bufora
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
	
		//Przygotowanie pliku z odpowiedzi�
		OutputStream toOut = new FileOutputStream(testFileOut);
		PrintWriter to = new PrintWriter(toOut);
		to.write("HeaderOdpowiedz\n\nBodyOdpowiedz");
		to.flush();
		to.close();
		
		//Tworzymy nowy obiekt w�tku, podpinaj�c do niego strumienie do plik�w-test�w
		DownloadThread testThread = new DownloadThread(997,buffer
				,new ArrayBlockingQueue<String>(5),downloader);
		
		String[] respondFromServer = testThread.respond("Zapytanie");
		
		//Odczytujemy zawarto�� pliku, do kt�rego zapisali�my zapytanie do servera
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
			fail("B��d przy czytaniu z pliku");
		}
		
		//Sprawdzenie czy zadane zapytanie jest zgodne z oczekiwaniami
		//oraz czy na to zapytanie otrzymali�my odpowiedni� odpowiedz
		assertEquals("Zapytanie",requestToServer.toString());
		assertEquals("HeaderOdpowiedz",respondFromServer[0]);
		assertEquals("BodyOdpowiedz",respondFromServer[1]);
		
		//Usuwanie plik�w
		if(testFileIn.exists())
		{
			if(!testFileIn.delete())
			{
				fail("Nie usuni�to pliku");
			}
		}
		else
		{
			fail("Plik testowy testFileIn nie istnia�!");
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
			fail("Plik testowy testFileOut nie istnia�!");
		}
	}
*/
	//TODO dla innych testy, no to b�dziemy sprawdza� co jest w tych innych kolejkach...
	/**
	 * Sprwadzamy przypadek, gdy otrzymujemy od servera odpowiedz 200 OK
	 */
	@Test
	public void testRunRespond200() throws IOException, InterruptedException {
	
		RequestCreator requestCreator = new RequestCreator();
		//TODO trzeba zmieni� requestCreatora, zeby mo�na mu by�o ustawi� requesty te�
		//zewn�trzn� funkcj�...
		
		BlockingQueuePagesBuffer pages = new BlockingQueuePagesBuffer(5);
		BlockingQueue<String> request = new ArrayBlockingQueue<String>(3);
		request.add("Zapytanie1");
		request.add("Zapytanie2");
		
		String testRoot = "tests/testData/";
		
		if(!new File("Tests/testConf").exists())
		{
			fail("Nie odnaleziono pliku testowego!");
		}
		
		String nameOfTestFile = "Tests/ServerRespond200";
		File testFile = new File(nameOfTestFile);
		
		DownloadThread d1 = new DownloadThread(999,pages,requestCreator,
				new FileDownloader(testRoot+"RespondTestFile1",nameOfTestFile));
		
		
		//Przygotowanie danych testowych
		if(!testFile.exists())
		{
			fail("Brak �adanego pliku testowego");
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
			fail("Pojawi� si� wyj�tek przy czytaniu z pliku testAnaliseHTMLPage");
		}
		pages.addPage(XMLDocument.toString());
		
		//Odpalmy to!
		d1.start();
		d1.join();
		
		//Wczytujemy plik por�wnawczy oraz utwrzony i por�wnujemy
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
			fail("Pojawi� si� wyj�tek przy czytaniu z pliku oczekiwanego");
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
			fail("Pojawi� si� wyj�tek przy czytaniu pliku otrzymanego z testu");
		}
		
		//Usuwamy utworzone pliki
		if(gotFile.exists())
		{
			if(!gotFile.delete())
			{
				fail("Nie usuni�to pliku");
			}
	
			if(!gotFile.getParentFile().delete())
			{
				fail("Nie usuni�to katalogu");
			}
		}
		else
		{
			fail("Plik zosta� niew���ciwie usuni�ty");
		}
		
		if(buStops.exists())
		{
			if(!buStops.delete())
			{
				fail("Nie usuni�to pliku");
			}
	
			if(!buStops.getParentFile().delete())
			{
				fail("Nie usuni�to katalogu");
			}
		}
		else
		{
			fail("Plik zosta� niew���ciwie usuni�ty");
		}
		
		if(testDataFolder.exists())
		{
			if(!testDataFolder.delete())
			{
				fail("Katalog g��wny test�w nie zosta� usuni�ty poprawnie");
			}
		}
		else
		{
			fail("Katalog g��wny test�w zosta� wcze�niej usuni�ty niepoprawnie");
		}		
		
		//Sprawdzamy co znajdowa�o si� w plikach
		assertEquals(expected.toString(),got.toString());
	}
}
//TODO napiszmy testy dla r�nych mo�liwo�ci! Co tak ubogo? Niech dzia�a r�nie dla
//Kod�w 302, 301, 502, 100 chocia�by, a co!