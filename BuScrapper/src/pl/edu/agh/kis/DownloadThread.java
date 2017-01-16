package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

/**
 * Klasa w¹tków, których zadaniem jest umieszczenie poprawnie otrzymanej zawartoœci w bufofrze,
 * metod tej klasy nie interesuje co dalej stanie siê z otrzyman¹ przez ni¹ treœci¹,
 * zak³ada te¿, ¿e otrzymana w konstruktorze kolejka zapytañ zawiera poprawnie utworzone
 * zapytania do servera, wiêc w¹tki tej klasy odpowiadaj¹ wy³¹cznie za samo pobranie zasobów
 * z servera
 * @author Szymon Majkut
 * @version 1.1b
 */
public class DownloadThread extends Thread {

	/**
	 * W³asny system logów
	 */
	private Logger downloadLogger;
	
	/**
	 * Unikatowa nazwa przyporz¹dkowana danemu w¹tkowi
	 */
	private String threadName;
	
	/**
	 * Referencja do kolejki zapytañ, które w¹tek powinieñ wykonaæ
	 */
	private BlockingQueue<String> requests;
	
	/**
	 * Referencja do bufora przechowuj¹cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wejœcia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wyjœcia do hosta
	 */
	private OutputStream output;
	
	/**
	 * Funkcja otrzymuje poprawnie u³o¿one zapytanie do servera, oraz zwraca odpowiedz,
	 * któr¹ od niego otrzymuje, jest public, aby mo¿na j¹ by³o przetestowaæ
	 * @param request poprawne zapytanie do servera o zasób
	 * @return para nag³ówek oraz zawartoœæ strony w HTML
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
	 * Funkcja s³u¿y do odfiltrowania od niepoprawnych zasób tych, które posiadaj¹ HTTP 200 OK
	 * tak aby nie zaœmiecaæ bufora niepotrzebnymi danymi, co wiêcej zapewniæ w buforze istnienie
	 * tylko poprawnych stron
	 * @param header nag³ówek zasobu otrzymany dla poprawnego zapytania
	 * @return informacja czy pobrany zasób jest zasobem poprawnym z punktu widzenia u¿ytkownika
	 */
	private boolean isCorrectRespond(String respond)
	{
		if(respond == null)
		{
			downloadLogger.warning("Zamiast zasobu otrzyma³em null");
			return false;
		}
		else if(respond.contains("HTTP/1.1 200 OK") || respond.contains("HTTP/1.0 200 OK"))
		{
			downloadLogger.info("Pobrano poprawny zasób");
			return true;
		}
		else
		{
			downloadLogger.info("Nie zwrócono poprawnego zasobu, nie wstawiam go do kolejki");
			return false;
		}
	}
	
	/**
	 * G³ówna pêtla w¹tku - producenta, jej zadaniem jest zasypianie w przypadku pe³nego bufora
	 * oraz budzenie w¹tków dzia³aj¹cych na buforze, je¿eli sama umieœci w nim jakieœ dane, 
	 * otrzymane poprzez poprzez odpowiednio nastawiony strumieñ wejœcia, wysy³aj¹c uprzednio
	 * strumieniem wyjœcia odpowiednie zapytanie o zasób, które jest pobierane z kolejki
	 * przygotowanej przez Configurator i udostêpnione w postaci kolejki requests
	 */
	public void run()
	{		
		do
		{
			BuScrapper.numberOfWorkingThreads.incrementAndGet();
			
			//Pobiera kolejne zapytanie z kolejki zapytañ
			if(!requests.isEmpty())
			{
				String[] respondFromServer = respond(requests.poll());
				
				if(isCorrectRespond(respondFromServer[0]))
				{
					//Wystarczy dziêki specyfikacji BlockingQueue
					pagesToAnalise.addPage(respondFromServer[1]);
					downloadLogger.info("Dodajê now¹ stronê do kolejki stron");
				}
			}
			downloadLogger.execute();
			BuScrapper.numberOfWorkingThreads.decrementAndGet();
			
		}while(BuScrapper.numberOfWorkingThreads.intValue() > 0);
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów oraz do³¹czyæ do odpowiednich pól strumienie podane przez w¹tek nadrzêdny do komunikacji,
	 * mia³ tak¿e dostêp do bufora œci¹gniêtych stron oraz kolejki zapytañ z Configurator'a, dodatkowo
	 * ustala równie¿ sposób sk³adowania logów
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku nadrzêdnym
	 * @param requests snychronizowana kolejka zapytañ
	 * @param pagesToAnalise bufor stron, zapewniaj¹cy blokowanie udostêpnianych przez siebie metod
	 * @param input strumieñ wejœcia przez który bêd¹ przychodziæ odpowiedzi servera
	 * @param output strumieñ wyjœcia przez który wysy³amy zapytania do servera
	 * @param appender obiekt odpowiedzialny za sk³adowanie logów
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
		downloadLogger.info("DownloadThread o imieniu "+threadName+" rozpoczyna pracê!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów oraz do³¹czyæ do odpowiednich pól strumienie podane przez w¹tek nadrzêdny do komunikacji,
	 * mia³ tak¿e dostêp do bufora œci¹gniêtych stron oraz kolejki zapytañ z Configurator'a
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku nadrzêdnym
	 * @param requests snychronizowana kolejka zapytañ
	 * @param pagesToAnalise bufor stron, zapewniaj¹cy blokowanie udostêpnianych przez siebie metod
	 * @param input strumieñ wejœcia przez który bêd¹ przychodziæ odpowiedzi servera
	 * @param output strumieñ wyjœcia przez który wysy³amy zapytania do servera
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			InputStream input, OutputStream output)
	{
		this(id,requests,pagesToAnalise,input,output,new FileAppender("DownloadThread number " + id));
	}
}