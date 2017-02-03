package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest umieszczenie poprawnie otrzymanej odpowiedzi w bufofrze,
 * wykorzystuj�c do tego celu strumienie uzyskane przez obiekt implementuj�cy Downloader, oraz
 * kolejk� zapyta�, utworzon� przez RequestCreator poprzez BuScrappera
 * @author Szymon Majkut
 * @version 1.3
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
	 * Obiekt udost�pniaj�cy strumienie do wysy�ania zapyta� oraz odbierania odpowiedzi
	 */
	private Downloader downloader;
	
	/**
	 * Funkcja otrzymuje poprawnie u�o�one zapytanie do servera, oraz zwraca odpowied�,
	 * kt�r� od niego otrzymuje. Funckja jest publiczna, poniewa� w ten spos�b pozwala
	 * na dogodne testowanie, posiada na sztywno ustalone kodowanie UTF-8
	 * @param request poprawne zapytanie do servera o zas�b
	 * @return para nag��wek oraz zawarto�� strony w HTML
	 */
	public String[] respond(String request)
	{
		InputStream input;
		OutputStream output;
		
		String line = "";
		StringBuilder respond = new StringBuilder();
		StringBuilder header = new StringBuilder();
		
		if(downloader.createStreams())
		{
			input = downloader.getInputSteam();
			output = downloader.getOutputStream();
			try {
				
				OutputStreamWriter to = new OutputStreamWriter(output,"UTF-8");
				to.write(request);
				to.flush();
			
				downloadLogger.info("Request:",request);
				BufferedReader from;
				
				from = new BufferedReader(new InputStreamReader(input,"UTF-8"));
				
				while((line = from.readLine()) != null)
				{
					header.append(line);
					if(line.equals(""))
					{
						break;
					}
				}
				
				while((line = from.readLine()) != null)
				{
					respond.append(line);
				}
				
				to.close();
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
		
		String result[] = {header.toString(),respond.toString()};

		return result;
	}
	
	/**
	 * Funkcja s�u�y do odfiltrowania od niepoprawnych odpowiedzi
	 * @param header nag��wek zasobu otrzymanego z poprawnego zapytania
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
	 * G��wna p�tla w�tku - producenta, jej zadaniem budzenie w�tk�w dzia�aj�cych na buforze,
	 * je�eli sama umie�ci w nim jakie� dane otrzymane poprzezodpowiednio nastawiony strumie�
	 * wej�cia, wysy�aj�c uprzednio strumieniem wyj�cia odpowiednie zapytanie, kt�re jest 
	 * pobierane z kolejki przygotowanej przez RequestCreator za po�rednictwem BuScrapper,
	 * zasypia gdy bufor oka�e si� przepe�niony, funkcja dzia�a dop�ki pozostaj� jeszcze jakie�
	 * zapytania do wys�ania
	 */
	public void run()
	{		
		
		BuScrapper.numberOfWorkingThreads.incrementAndGet();
		boolean alreadyDecrement = false;
		
		try {
			do
			{
				String[] respondFromServer = null;
				//Pobiera kolejne zapytanie z kolejki zapyta�
				if(!requests.isEmpty())
				{
					respondFromServer = respond(requests.take());
				}
				
				if(respondFromServer != null && isCorrectRespond(respondFromServer))
				{
					//Wystarczy dzi�ki specyfikacji BlockingQueue
						
					pagesToAnalise.addPage(respondFromServer[1]);
					downloadLogger.info("Dodaj� now� stron� do kolejki stron");
				}
				else if(!alreadyDecrement)
				{
					BuScrapper.numberOfWorkingThreads.decrementAndGet();
					alreadyDecrement = true;
				}
				downloadLogger.execute();
			}while(BuScrapper.numberOfWorkingThreads.intValue() > 0);
		} catch (InterruptedException e) {
			downloadLogger.info("Niepoprawnie wybudzony!",e.getMessage());
		}
		
		if(downloader.closeStreams())
		{
			downloadLogger.info("Strumienie zosta�y zamkni�te poprawnie");
		}
		else
		{
			downloadLogger.error("Strumienie nie zosta�y zamkni�te poprawnie");
		}
		
		downloadLogger.info("DownloadThread o imieniu "+threadName+" ko�czy prac�!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w oraz do��czy� do odpowiednich p�l strumienie podane przez w�tek nadrz�dny do
	 * komunikacji, mia� tak�e dost�p do bufora �ci�gni�tych stron oraz kolejki zapyta�
	 * przygotowanych przez RequestCreator, dodatkowo ustala r�wnie� spos�b sk�adowania log�w
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku nadrz�dnym
	 * @param requests snychronizowana kolejka zapyta�
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez siebie metod
	 * @param downloader obiekt przechowuj�cy i udost�pniaj�ce strumienie do pobierania zasob�w
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
	 * log�w oraz do��czy� do odpowiednich p�l strumienie podane przez w�tek nadrz�dny do
	 * komunikacji, mia� tak�e dost�p do bufora �ci�gni�tych stron oraz kolejki zapyta�
	 * przygotowanych przez RequestCreator, ustala spos�b sk�adowania log�w na domy�lny
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