package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest umieszczenie poprawnie otrzymanej odpowiedzi
 * w obiekcie implementuj�cym PagesBuffer. Do uzyskania odpowiedzi wykorzystuje strumienie 
 * uzyskane przez implementacj� interfejsu Downloader, oraz korzysta kolejki zapyta�, 
 * utworzonej przez RequestCreator. Ma dost�p do dw�ch statycznych p�l BuScrapper'a,
 * numberOfWorkingDownloadThreads okre�la liczb� wci�� pracuj�cych w�tk�w pobieraj�cych
 * natomiast correctTaskExecute okre�la czy w�tki pobieraj�ce nie pope�ni�y b��du przy
 * wykonywaniu danego zadania, aby w ten spos�b unikn�� pomijania danych niepobranych
 * w poprawny spos�b. Niepoprawno�� pobieranych danych jest okre�lana poprzez wy�apywane
 * z metod prywatnych wyj�tki.
 * @author Szymon Majkut
 * @version 1.4
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
	 * na dogodne testowanie, posiada na sztywno ustalone kodowanie UTF-8.
	 * @param request poprawne zapytanie do servera uzyskane z kolejki udost�pnionej przez
	 * 		  RequestCreator
	 * @return tablica obiekt�w String, elementem o indeksie zero jest nag��wek odpowiedzi,
	 *        natomiast elementem o indeksie jeden jest tre�� odpowiedz
	 * @throws UnknownHostException wyrzucany w przypadku b��dnego ustalenia stanu obiektu
	 *         przeznaczonego do komunikacji sieciowej lub braku dost�pu do internetu
	 * @throws IOException wyrzucany gdy nast�puje problem z korzystaniem ze strumieni
	 *         na poziomie niesieciowym
	 */
	public String[] respond(String request) throws UnknownHostException, IOException
	{
		String line = "";
		StringBuilder respond = new StringBuilder();
		StringBuilder header = new StringBuilder();
		OutputStreamWriter to;
		BufferedReader from;
		
		if(downloader.initStreams())
		{
			try {
				
				to = new OutputStreamWriter(
						downloader.getOutputStream(),"UTF-8");
				to.write(request);
				to.flush();
				downloadLogger.info("Request:",request);
				
				from = new BufferedReader(new InputStreamReader(
						downloader.getInputSteam(),"UTF-8"));			

				boolean content = false;

				while((line = from.readLine()) != null)
				{
					if(line.equals(""))
					{
						content = true;
					}
					if(content)
					{
						respond.append(line);
					}
					else
					{
						header.append(line);
					}
				}

				downloadLogger.info("Pobrano odpowiedz servera");
				downloader.closeStreams();
				
			} catch (UnsupportedEncodingException e1) {
				downloadLogger.error("Niew�a�ciwe kodowanie dokumentu");
				e1.printStackTrace();		
			} catch (IOException e) {
				downloadLogger.error("Problem z czytaniem odpowiedzi servera");
			} 
		}
		else
		{
			downloadLogger.error("Nie utworzono socketu, brak po��czenia");
		}

		String[] allRespond = {header.toString(),respond.toString()};
		
		return allRespond;
	}
	
	/**
	 * Funkcja s�u�y do odfiltrowania od niepoprawnych odpowiedzi
	 * @param header nag��wek zasobu otrzymanego z poprawnego zapytania
	 * @return informacja czy pobrany zas�b jest zasobem poprawnym z punktu widzenia u�ytkownika
	 */
	private boolean isCorrectRespond(String[] respond)
	{
		if(respond[0] == null || respond[0].equals("") || 
				respond[1] == null || respond[1].equals(""))
		{
			downloadLogger.warning("Zamiast zasobu otrzyma�em null");
			return false;
		}
		else if(respond[0].contains("HTTP/1.1 200 OK") || 
				respond[0].contains("HTTP/1.0 200 OK"))
		{
			downloadLogger.info("Pobrano poprawny zas�b");
			return true;
		}
		else
		{
			downloadLogger.
			info("Nie zwr�cono poprawnego zasobu, nie wstawiam go do kolejki",
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
		
		BuScrapper.numberOfWorkingDownloadThreads.incrementAndGet();
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
				else
				{
					BuScrapper.correctTaskExecute.set(true);
					break;
				}

				if(isCorrectRespond(respondFromServer))
				{
					//Wystarczy dzi�ki specyfikacji BlockingQueue
					pagesToAnalise.addPage(respondFromServer[1]);
					downloadLogger.info("Dodaj� now� stron� do kolejki stron");
				}
				
				downloadLogger.execute();
			}while(BuScrapper.numberOfWorkingDownloadThreads.intValue() > 0);
		} catch (InterruptedException e) {
			downloadLogger.error("Niepoprawnie wybudzony!",e.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} catch (UnknownHostException e) {
			downloadLogger.error("Problem z po��czeniem internetowym!",e.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} catch (IOException e) {
			downloadLogger.error("Problem ze strumieniami!",e.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} catch (Throwable t) {
			downloadLogger.error("Powa�ny problem!",t.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} finally {
			
			if(!alreadyDecrement && !BuScrapper.correctTaskExecute.get())
			{
				alreadyDecrement = true;
			}
			else if(!alreadyDecrement)
			{
				BuScrapper.numberOfWorkingDownloadThreads.decrementAndGet();
				alreadyDecrement = true;
			}
			downloadLogger.execute();
		}
				
		downloadLogger.info("DownloadThread o imieniu "+threadName+" ko�czy prac�!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadanie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w, implementacj� Downloader, kt�ra b�dzie udost�pnia�a strumienie potrzebne przy
	 * uzyskiwaniu zasob�w, implementacj� PagesBuffer do zesk�adowania ju� pobranych zasob�w,
	 * kolejk� zapyta� przygotowan� przez RequestCreator, oraz obiekt odpowiedzialny za
	 * sk�adowanie log�w.
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku
	 *        nadrz�dnym
	 * @param requests snychronizowana kolejka zapyta�
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez 
	 *        siebie metod
	 * @param downloader obiekt przechowuj�cy i udost�pniaj�cy strumienie do pobierania 
	 *        zasob�w
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
	 * Konstruktor sparametryzowany, kt�rego zadanie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w, implementacj� Downloader, kt�ra b�dzie udost�pnia�a strumienie potrzebne przy
	 * uzyskiwaniu zasob�w, implementacj� PagesBuffer do zesk�adowania ju� pobranych zasob�w,
	 * oraz kolejk� zapyta� przygotowan� przez RequestCreator. Ustala domy�lny spos�b
	 * sk�adowania log�w.
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku
	 *        nadrz�dnym
	 * @param requests snychronizowana kolejka zapyta�
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez 
	 *        siebie metod
	 * @param downloader obiekt przechowuj�cy i udost�pniaj�cy strumienie do pobierania 
	 *        zasob�w
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			Downloader downloader)
	{
		this(id,requests,pagesToAnalise,downloader,
				new ConsoleAppender());
	}
}