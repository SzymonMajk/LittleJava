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
 * Klasa w¹tków, których zadaniem jest umieszczenie poprawnie otrzymanej odpowiedzi w bufofrze,
 * wykorzystuj¹c do tego celu strumienie uzyskane przez obiekt implementuj¹cy Downloader, oraz
 * kolejkê zapytañ, utworzon¹ przez RequestCreator poprzez BuScrappera
 * @author Szymon Majkut
 * @version 1.3
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
	 * Obiekt udostêpniaj¹cy strumienie do wysy³ania zapytañ oraz odbierania odpowiedzi
	 */
	private Downloader downloader;
	
	/**
	 * Funkcja otrzymuje poprawnie u³o¿one zapytanie do servera, oraz zwraca odpowiedŸ,
	 * któr¹ od niego otrzymuje. Funckja jest publiczna, poniewa¿ w ten sposób pozwala
	 * na dogodne testowanie, posiada na sztywno ustalone kodowanie UTF-8
	 * @param request poprawne zapytanie do servera o zasób
	 * @return para nag³ówek oraz zawartoœæ strony w HTML
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
				downloadLogger.error("Niew³aœciwe kodowanie dokumentu");
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
	 * Funkcja s³u¿y do odfiltrowania od niepoprawnych odpowiedzi
	 * @param header nag³ówek zasobu otrzymanego z poprawnego zapytania
	 * @return informacja czy pobrany zasób jest zasobem poprawnym z punktu widzenia u¿ytkownika
	 */
	private boolean isCorrectRespond(String[] respond)
	{
		if(respond[0] == null || respond[0].equals("") || respond[1] == null || respond[1].equals(""))
		{
			downloadLogger.warning("Zamiast zasobu otrzyma³em null");
			return false;
		}
		else if(respond[0].contains("HTTP/1.1 200 OK") || respond[0].contains("HTTP/1.0 200 OK"))
		{
			downloadLogger.info("Pobrano poprawny zasób");
			return true;
		}
		else
		{
			downloadLogger.info("Nie zwrócono poprawnego zasobu, nie wstawiam go do kolejki",
					respond[0]);
			return false;
		}
	}
	
	/**
	 * G³ówna pêtla w¹tku - producenta, jej zadaniem budzenie w¹tków dzia³aj¹cych na buforze,
	 * je¿eli sama umieœci w nim jakieœ dane otrzymane poprzezodpowiednio nastawiony strumieñ
	 * wejœcia, wysy³aj¹c uprzednio strumieniem wyjœcia odpowiednie zapytanie, które jest 
	 * pobierane z kolejki przygotowanej przez RequestCreator za poœrednictwem BuScrapper,
	 * zasypia gdy bufor oka¿e siê przepe³niony, funkcja dzia³a dopóki pozostaj¹ jeszcze jakieœ
	 * zapytania do wys³ania
	 */
	public void run()
	{		
		
		BuScrapper.numberOfWorkingThreads.incrementAndGet();
		boolean alreadyDecrement = false;
		
		try {
			do
			{
				String[] respondFromServer = null;
				//Pobiera kolejne zapytanie z kolejki zapytañ
				if(!requests.isEmpty())
				{
					respondFromServer = respond(requests.take());
				}
				
				if(respondFromServer != null && isCorrectRespond(respondFromServer))
				{
					//Wystarczy dziêki specyfikacji BlockingQueue
						
					pagesToAnalise.addPage(respondFromServer[1]);
					downloadLogger.info("Dodajê now¹ stronê do kolejki stron");
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
			downloadLogger.info("Strumienie zosta³y zamkniête poprawnie");
		}
		else
		{
			downloadLogger.error("Strumienie nie zosta³y zamkniête poprawnie");
		}
		
		downloadLogger.info("DownloadThread o imieniu "+threadName+" koñczy pracê!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów oraz do³¹czyæ do odpowiednich pól strumienie podane przez w¹tek nadrzêdny do
	 * komunikacji, mia³ tak¿e dostêp do bufora œci¹gniêtych stron oraz kolejki zapytañ
	 * przygotowanych przez RequestCreator, dodatkowo ustala równie¿ sposób sk³adowania logów
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku nadrzêdnym
	 * @param requests snychronizowana kolejka zapytañ
	 * @param pagesToAnalise bufor stron, zapewniaj¹cy blokowanie udostêpnianych przez siebie metod
	 * @param downloader obiekt przechowuj¹cy i udostêpniaj¹ce strumienie do pobierania zasobów
	 * @param appender obiekt odpowiedzialny za sk³adowanie logów
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
		downloadLogger.info("DownloadThread o imieniu "+threadName+" rozpoczyna pracê!");
		downloadLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów oraz do³¹czyæ do odpowiednich pól strumienie podane przez w¹tek nadrzêdny do
	 * komunikacji, mia³ tak¿e dostêp do bufora œci¹gniêtych stron oraz kolejki zapytañ
	 * przygotowanych przez RequestCreator, ustala sposób sk³adowania logów na domyœlny
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku nadrzêdnym
	 * @param requests snychronizowana kolejka zapytañ
	 * @param pagesToAnalise bufor stron, zapewniaj¹cy blokowanie udostêpnianych przez siebie metod
	 * @param downloader obiekt przechowuj¹cy i udostêpniaj¹ce strumienie do komunikacji z serverem
	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			Downloader downloader)
	{
		this(id,requests,pagesToAnalise,downloader,new FileAppender("DownloadThread number " + id));
	}
}