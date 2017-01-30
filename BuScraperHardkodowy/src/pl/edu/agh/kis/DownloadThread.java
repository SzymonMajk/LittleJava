package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest umieszczenie poprawnie otrzymanej zawarto�ci w bufofrze,
 * metod tej klasy nie interesuje co dalej stanie si� z otrzyman� przez ni� tre�ci�,
 * zak�ada te�, �e otrzymana w konstruktorze kolejka zapyta� zawiera poprawnie utworzone
 * zapytania do servera, wi�c w�tki tej klasy odpowiadaj� wy��cznie za samo pobranie zasob�w
 * z servera
 * @author Szymon Majkut
 * @version 1.1b
 */
public class DownloadThread extends Thread {

	/**
	 * W�asny system log�w
	 */
	private Logger downloadLogger;
	
	/**
	 * Unikatowa nazwa przyporz�dkowana danemu w�tkowi
	 */
	private String threadName;
	
	/**
	 * Referencja do kolejki zapyta�, kt�re w�tek powinie� wykona�
	 */
	private BlockingQueue<String> requests;
	
	/**
	 * Referencja do bufora przechowuj�cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	/**
	 * Obiekt udost�pniaj�cy strumienie do komunikacji z serverem
	 */
	private Downloader downloader;
	
	/**
	 * Funkcja otrzymuje poprawnie u�o�one zapytanie do servera, oraz zwraca odpowiedz,
	 * kt�r� od niego otrzymuje, jest public, aby mo�na j� by�o przetestowa�
	 * @param request poprawne zapytanie do servera o zas�b
	 * @return para nag��wek oraz zawarto�� strony w HTML
	 */
	public String[] respond(String request)
	{
		InputStream input;
		OutputStream output;
		
		String line = "";
		String respond = "";
		String header = "";
		
		if(downloader.createSocket())
		{
			input = downloader.getInputSteam();
			output = downloader.getOutputStream();
			
			PrintWriter to = new PrintWriter(output);
			to.print(request);
			to.flush();
			downloadLogger.info("req",request);
			BufferedReader from;
							
			try {
				
				//p�zniej zrobimy, �� u�ytkownik podaje kodowanie
				from = new BufferedReader(new InputStreamReader(input,"UTF-8"));
				
				while((line = from.readLine()) != null)
				{
					header += line;
					if(line.equals(""))
					{
						break;
					}
				}
				
				while((line = from.readLine()) != null)
				{
					respond += line;
				}
				
				from.close();
				
				downloadLogger.info("Pobrano odpowiedz servera");
			} catch (UnsupportedEncodingException e1) {
				downloadLogger.error("Niew�a�ciwe kodowanie dokumentu");
				e1.printStackTrace();		
			} catch (IOException e) {
				downloadLogger.error("Problem z czytaniem odpowiedzi servera");
			}
			
		}
		else
		{
			downloadLogger.error("Nie utworzono poprawnie socketu");
		}
		
		String result[] = {header,respond};

		return result;
	}
	
	/**
	 * Funkcja s�u�y do odfiltrowania od niepoprawnych zas�b tych, kt�re posiadaj� HTTP 200 OK
	 * tak aby nie za�mieca� bufora niepotrzebnymi danymi, co wi�cej zapewni� w buforze istnienie
	 * tylko poprawnych stron
	 * @param header nag��wek zasobu otrzymany dla poprawnego zapytania
	 * @return informacja czy pobrany zas�b jest zasobem poprawnym z punktu widzenia u�ytkownika
	 */
	private boolean isCorrectRespond(String[] respond)
	{
		if(respond[0] == null || respond[0].equals("") || respond[1] == null || respond[1].equals(""))
		{
			downloadLogger.warning("Zamiast zasobu otrzyma�em null");
			return false;
		}
		else if(respond[0].contains("HTTP/1.1 200 OK") || respond[0].contains("HTTP/1.0 200 OK"))
		{
			downloadLogger.info("Pobrano poprawny zas�b");
			return true;
		}
		else
		{
			downloadLogger.info("Nie zwr�cono poprawnego zasobu, nie wstawiam go do kolejki",
					respond[0]);
			return false;
		}
	}
	
	/**
	 * G��wna p�tla w�tku - producenta, jej zadaniem jest zasypianie w przypadku pe�nego bufora
	 * oraz budzenie w�tk�w dzia�aj�cych na buforze, je�eli sama umie�ci w nim jakie� dane, 
	 * otrzymane poprzez poprzez odpowiednio nastawiony strumie� wej�cia, wysy�aj�c uprzednio
	 * strumieniem wyj�cia odpowiednie zapytanie o zas�b, kt�re jest pobierane z kolejki
	 * przygotowanej przez Configurator i udost�pnione w postaci kolejki requests
	 */
	public void run()
	{		
		do
		{
			BuScrapper.numberOfWorkingThreads.incrementAndGet();
			
			//Pobiera kolejne zapytanie z kolejki zapyta�
			if(!requests.isEmpty())
			{
				String[] respondFromServer = respond(requests.poll());

				if(isCorrectRespond(respondFromServer))
				{
					//Wystarczy dzi�ki specyfikacji BlockingQueue
					pagesToAnalise.addPage(respondFromServer[1]);
					downloadLogger.info("Dodaj� now� stron� do kolejki stron");
				}
			}
			downloadLogger.execute();
			//System.out.println("dupa");
			BuScrapper.numberOfWorkingThreads.decrementAndGet();
		//}while(BuScrapper.numberOfWorkingThreads.intValue() > 0);
		}while(!requests.isEmpty());
		
		downloadLogger.info("DownloadThread o imieniu "+threadName+" ko�czy prac�!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w oraz do��czy� do odpowiednich p�l strumienie podane przez w�tek nadrz�dny do komunikacji,
	 * mia� tak�e dost�p do bufora �ci�gni�tych stron oraz kolejki zapyta� z Configurator'a, dodatkowo
	 * ustala r�wnie� spos�b sk�adowania log�w
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku nadrz�dnym
	 * @param requests snychronizowana kolejka zapyta�
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez siebie metod
	 * @param downloader obiekt przechowuj�cy i udost�pniaj�ce strumienie do komunikacji z serverem
	 * @param appender obiekt odpowiedzialny za sk�adowanie log�w
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			Downloader downloader, Appends appender)
	{
		threadName = "DownloadThread number " + id;
		this.requests = requests;
		this.pagesToAnalise = pagesToAnalise;
		this.downloader = downloader;
		downloadLogger = new Logger();
		downloadLogger.changeAppender(appender);
		downloadLogger.info("DownloadThread o imieniu "+threadName+" rozpoczyna prac�!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w oraz do��czy� do odpowiednich p�l strumienie podane przez w�tek nadrz�dny do komunikacji,
	 * mia� tak�e dost�p do bufora �ci�gni�tych stron oraz kolejki zapyta� z Configurator'a
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku nadrz�dnym
	 * @param requests snychronizowana kolejka zapyta�
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez siebie metod
	 * @param downloader obiekt przechowuj�cy i udost�pniaj�ce strumienie do komunikacji z serverem
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			Downloader downloader)
	{
		this(id,requests,pagesToAnalise,downloader,new FileAppender("DownloadThread number " + id));
	}
}