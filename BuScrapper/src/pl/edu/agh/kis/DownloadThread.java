package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wej�cia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wyj�cia do hosta
	 */
	private OutputStream output;
	
	/**
	 * Funkcja otrzymuje poprawnie u�o�one zapytanie do servera, oraz zwraca odpowiedz,
	 * kt�r� od niego otrzymuje, jest public, aby mo�na j� by�o przetestowa�
	 * @param request poprawne zapytanie do servera o zas�b
	 * @return para nag��wek oraz zawarto�� strony w HTML
	 */
	public String[] respond(String request)
	{
		PrintWriter to = new PrintWriter(output);
		to.write(request);
		to.flush();
		to.close();
		
		BufferedReader from = new BufferedReader(new InputStreamReader(input));
		
		String line = "";
		String respond = "";
		String header = "";
		
		try {
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
			
		} catch (IOException e) {
			downloadLogger.error("Problem z czytaniem odpowiedzi servera");
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
	private boolean isCorrectRespond(String respond)
	{
		if(respond == null)
		{
			downloadLogger.warning("Zamiast zasobu otrzyma�em null");
			return false;
		}
		else if(respond.contains("HTTP/1.1 200 OK") || respond.contains("HTTP/1.0 200 OK"))
		{
			downloadLogger.info("Pobrano poprawny zas�b");
			return true;
		}
		else
		{
			downloadLogger.info("Nie zwr�cono poprawnego zasobu, nie wstawiam go do kolejki");
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
				
				if(isCorrectRespond(respondFromServer[0]))
				{
					//Wystarczy dzi�ki specyfikacji BlockingQueue
					pagesToAnalise.addPage(respondFromServer[1]);
					downloadLogger.info("Dodaj� now� stron� do kolejki stron");
				}
			}
			downloadLogger.execute();
			BuScrapper.numberOfWorkingThreads.decrementAndGet();
			
		}while(BuScrapper.numberOfWorkingThreads.intValue() > 0);
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
	 * @param input strumie� wej�cia przez kt�ry b�d� przychodzi� odpowiedzi servera
	 * @param output strumie� wyj�cia przez kt�ry wysy�amy zapytania do servera
	 * @param appender obiekt odpowiedzialny za sk�adowanie log�w
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			InputStream input, OutputStream output, Appends appender)
	{
		threadName = "DownloadThread number " + id;
		this.requests = requests;
		this.pagesToAnalise = pagesToAnalise;
		this.input = input;
		this.output = output;
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
	 * @param input strumie� wej�cia przez kt�ry b�d� przychodzi� odpowiedzi servera
	 * @param output strumie� wyj�cia przez kt�ry wysy�amy zapytania do servera
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			InputStream input, OutputStream output)
	{
		this(id,requests,pagesToAnalise,input,output,new FileAppender("DownloadThread number " + id));
	}
}