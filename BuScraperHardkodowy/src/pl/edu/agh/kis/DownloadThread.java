package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Klasa w¹tków, których zadaniem jest umieszczenie poprawnie otrzymanej odpowiedzi
 * w obiekcie implementuj¹cym PagesBuffer. Do uzyskania odpowiedzi wykorzystuje strumienie 
 * uzyskane przez implementacjê interfejsu Downloader, oraz korzysta kolejki zapytañ, 
 * utworzonej przez RequestCreator. Ma dostêp do dwóch statycznych pól BuScrapper'a,
 * numberOfWorkingDownloadThreads okreœla liczbê wci¹¿ pracuj¹cych w¹tków pobieraj¹cych
 * natomiast correctTaskExecute okreœla czy w¹tki pobieraj¹ce nie pope³ni³y b³êdu przy
 * wykonywaniu danego zadania, aby w ten sposób unikn¹æ pomijania danych niepobranych
 * w poprawny sposób. Niepoprawnoœæ pobieranych danych jest okreœlana poprzez wy³apywane
 * z metod prywatnych wyj¹tki.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class DownloadThread extends Thread {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(DownloadThread.class.getName());
	
	/**
	 * Unikatowy numer przyporz¹dkowana danemu w¹tkowi
	 */
	private int threadId;
	
	/**
	 * Referencja do kolejki zapytañ, które w¹tek powinieñ wykonaæ
	 */
	private RequestCreator requestCreator;
	
	/**
	 * Obiekt udostêpniaj¹cy strumienie do wysy³ania zapytañ oraz odbierania odpowiedzi
	 */
	private Downloader downloader;
	
	/**
	 * Referencja do bufora przechowuj¹cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	private boolean connectionProblems;
	
	private Request currentRequest;
	
	private String currentHeader = "";
	
	private String currentResponse = "";
	
	private void storeRequest(Request request)
	{
		requestCreator.putInvalidRequest(request);
	}
	
	private String writeRequest(Request request)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(request.getMethod());
		builder.append(" ");
		builder.append(request.getUrlPath());
		
		if(request.getMethod().equals("GET"))
		{
			builder.append("?");
			builder.append(request.getParameters());
			builder.append(" HTTP/1.1\r\n");
			builder.append("Host: ");
			builder.append(request.getHost());
			builder.append("\r\n");
			builder.append("Accept-Charset: ");
			builder.append(request.getAcceptCharset());
			builder.append("\r\n");
			builder.append("Connection: close\r\n\r\n");
		}
		else if(request.getMethod().equals("POST"))
		{
			builder.append(" HTTP/1.1\r\n");
			builder.append("Host: ");
			builder.append(request.getHost());
			builder.append("\r\n");
			builder.append("Accept-Charset: ");
			builder.append(request.getAcceptCharset());
			builder.append("\r\n");
			builder.append("Connection: close\r\n\r\n");
			builder.append("Content-Type: application/x-www-form-urlencoded\r\n");
			builder.append("Content-Length: ");
			builder.append(request.getParameters().length());
			builder.append("\r\n\r\n");
			builder.append(request.getParameters());
			builder.append("\r\n");
		}
		
		return builder.toString();
	}
	
	/**
	 * Funkcja otrzymuje poprawnie u³o¿one zapytanie do servera, oraz zwraca odpowiedŸ,
	 * któr¹ od niego otrzymuje. Funckja jest publiczna, poniewa¿ w ten sposób pozwala
	 * na dogodne testowanie, posiada na sztywno ustalone kodowanie UTF-8.
	 * @param request poprawne zapytanie do servera uzyskane z kolejki udostêpnionej przez
	 * 		  RequestCreator
	 * @return 
	 */
	public boolean proceedRequest(Request request)
	{
		boolean result = true;
		String line = "";
		StringBuilder response = new StringBuilder("");
		StringBuilder header = new StringBuilder("");
		
		if(request != null && downloader.initDownloader(request.getHost()))
		{
			try (OutputStreamWriter to = new OutputStreamWriter(
				downloader.getOutputStream(),"UTF-8");
				BufferedReader headerResponseReader = new BufferedReader(
				new InputStreamReader(downloader.getInputSteam(),"UTF-8"))) {
				
				//Wykonujemy zapytanie
				to.write(writeRequest(request));
				to.flush();
				log4j.info("Wykona³em zapytanie o parametrach:"+request.getParameters());
				
							
				
				//Odbieramy nag³ówek odpowiedzi
				while((line = headerResponseReader.readLine()) != null)
				{
					if(line.equals(""))
					{
						break;
					}
					header.append(line);
				}
				
				//Odbieramy treœæ odpowiedzi
				while((line = headerResponseReader.readLine()) != null)
				{
					response.append(line);
				}
				
				log4j.info("Otrzyma³em odpowiedŸ na zapytanie o parametrach:"
						+request.getParameters());
			} catch (UnsupportedEncodingException e) {
				result = false;
				connectionProblems = true;
				log4j.error("Niewspierane kodowanie:"+e.getMessage());
				storeRequest(currentRequest);
			} catch (IOException e) {
				//Tutaj mamy sytuacjê gdy zerwano internet, wtedy mamy zakoñczyæ run()
				result = false;
				connectionProblems = true;
				log4j.warn("Utracono po³¹czenie z hostem:"+e.getMessage());
				storeRequest(currentRequest);
			} 
		}
		else
		{
			result = false;
			connectionProblems = true;
			log4j.warn("Niemo¿liwe zainicjowanie po³¹czenia z hostem.");
			storeRequest(currentRequest);
		}

		currentHeader = header.toString();
		currentResponse = response.toString();
				
		return result;
	}
	
	/**
	 * Funkcja s³u¿y do odfiltrowania od niepoprawnych odpowiedzi
	 * @param header nag³ówek zasobu otrzymanego z poprawnego zapytania
	 * @return informacja czy pobrany zasób jest zasobem poprawnym z punktu widzenia u¿ytkownika
	 */
	private boolean checkHeaderAndResponse()
	{
		
		if(currentHeader == null  || currentResponse == null)
		{
			log4j.warn("Header lub Response s¹ równe null.");
			return false;
		}
		else if(currentHeader.equals("") || currentResponse.equals(""))
		{
			log4j.warn("Header lub Response s¹ puste.");
			return false;
		}
				
		String headerCode = currentHeader.substring(9,12);
		
		if(headerCode.equals("200"))
		{
			log4j.info("Pobrano poprawny zasób.");
			return true;
		}
		else if(headerCode.equals("500") || headerCode.equals("502") || 
				headerCode.equals("503") || headerCode.equals("110") || 
				headerCode.equals("111"))
		{
			log4j.info("Konieczne ponowienie zapytania.");
			storeRequest(currentRequest);
			return false;
		}
		else if(headerCode.equals("301"))
		{
			log4j.info("Zasób zosta³ przeniesiony.");
			
			String[] splited = currentHeader.split("\r\n");
			String newResource = "";
			
			for(String l : splited)
			{
				if(l.startsWith("Location"))
				{
					newResource = l.substring(10);
					break;
				}
			}
			
			try {
				URL newUrl = new URL(newResource);
				
				String newHost = newUrl.getHost();
				String newPath = newUrl.getPath();
				
				storeRequest(new Request(currentRequest.getMethod(),newPath,newHost,
						currentRequest.getParameters(),currentRequest.getAcceptCharset()));
			} catch (MalformedURLException e) {
				log4j.error("B³êdny format URL"+e.getMessage());
			}
			
			return false;
		}
		else
		{
			log4j.info("Nie obs³u¿y³em zapytania:"+currentHeader);
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
		BuScrapper.numberOfWorkingDownloadThreads.incrementAndGet();
		connectionProblems = false;
		
		do
		{
			//Pobiera kolejne zapytanie z kolejki zapytañ
			try {
				currentRequest = requestCreator.getRequests().poll(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				log4j.error("Niepoprawnie wybudzony przy pobieraniu nowego zapytania:"
						+e.getMessage());
			} finally {
				if(currentRequest == null)
				{
					continue;
				}
			}
					
			if(proceedRequest(currentRequest) && checkHeaderAndResponse())
			{
				//Wystarczy dziêki specyfikacji BlockingQueue
				try {
					pagesToAnalise.addPage(currentResponse);
					log4j.info("Dodajê now¹ stronê do kolejki stron.");
				} catch (InterruptedException e) {
					log4j.error("Niepoprawnie wybudzony przy dok³adaniu nowej strony:"
							+e.getMessage());
				}		
			}
				
		}while(!requestCreator.getRequests().isEmpty() && !connectionProblems);
		
		currentHeader = "";
		currentResponse = "";
		
		BuScrapper.numberOfWorkingDownloadThreads.decrementAndGet();
		log4j.info("DownloadThread o id "+threadId+" koñczy pracê!");
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadanie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów, implementacjê Downloader, która bêdzie udostêpnia³a strumienie potrzebne przy
	 * uzyskiwaniu zasobów, implementacjê PagesBuffer do zesk³adowania ju¿ pobranych zasobów,
	 * kolejkê zapytañ przygotowan¹ przez RequestCreator.
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku
	 *        nadrzêdnym
	 * @param requestCreator obiekt udostêpniaj¹cy kolejkê zapytañ do wykonania, oraz
	 * 		  pozwalaj¹cy na zesk³adowanie wykonañ, które musz¹ zostaæ powtórzone
	 * @param pagesToAnalise bufor stron, zapewniaj¹cy blokowanie udostêpnianych przez
	 * @param requests kolejka blokuj¹ca zapytañ do wykonania  
	 *        siebie metod
	 * @param downloader obiekt przechowuj¹cy i udostêpniaj¹cy strumienie do pobierania 
	 *        zasobów

	 */
	DownloadThread(int id,PagesBuffer pagesToAnalise, RequestCreator requestCreator,
			Downloader downloader)
	{
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.requestCreator = requestCreator;
		this.downloader = downloader;
		log4j.info("DownloadThread o id "+threadId+" rozpoczyna pracê!");
	}
}