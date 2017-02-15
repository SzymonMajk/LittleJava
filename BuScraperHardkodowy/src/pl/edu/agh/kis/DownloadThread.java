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
 * @version %I%, %G%
 */
public class DownloadThread extends Thread {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(DownloadThread.class.getName());
	
	/**
	 * Unikatowy numer przyporz�dkowana danemu w�tkowi
	 */
	private int threadId;
	
	/**
	 * Referencja do kolejki zapyta�, kt�re w�tek powinie� wykona�
	 */
	private RequestCreator requestCreator;
	
	/**
	 * Obiekt udost�pniaj�cy strumienie do wysy�ania zapyta� oraz odbierania odpowiedzi
	 */
	private Downloader downloader;
	
	/**
	 * Referencja do bufora przechowuj�cego strony do przetworzenia
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
	 * Funkcja otrzymuje poprawnie u�o�one zapytanie do servera, oraz zwraca odpowied�,
	 * kt�r� od niego otrzymuje. Funckja jest publiczna, poniewa� w ten spos�b pozwala
	 * na dogodne testowanie, posiada na sztywno ustalone kodowanie UTF-8.
	 * @param request poprawne zapytanie do servera uzyskane z kolejki udost�pnionej przez
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
				log4j.info("Wykona�em zapytanie o parametrach:"+request.getParameters());
				
							
				
				//Odbieramy nag��wek odpowiedzi
				while((line = headerResponseReader.readLine()) != null)
				{
					if(line.equals(""))
					{
						break;
					}
					header.append(line);
				}
				
				//Odbieramy tre�� odpowiedzi
				while((line = headerResponseReader.readLine()) != null)
				{
					response.append(line);
				}
				
				log4j.info("Otrzyma�em odpowied� na zapytanie o parametrach:"
						+request.getParameters());
			} catch (UnsupportedEncodingException e) {
				result = false;
				connectionProblems = true;
				log4j.error("Niewspierane kodowanie:"+e.getMessage());
				storeRequest(currentRequest);
			} catch (IOException e) {
				//Tutaj mamy sytuacj� gdy zerwano internet, wtedy mamy zako�czy� run()
				result = false;
				connectionProblems = true;
				log4j.warn("Utracono po��czenie z hostem:"+e.getMessage());
				storeRequest(currentRequest);
			} 
		}
		else
		{
			result = false;
			connectionProblems = true;
			log4j.warn("Niemo�liwe zainicjowanie po��czenia z hostem.");
			storeRequest(currentRequest);
		}

		currentHeader = header.toString();
		currentResponse = response.toString();
				
		return result;
	}
	
	/**
	 * Funkcja s�u�y do odfiltrowania od niepoprawnych odpowiedzi
	 * @param header nag��wek zasobu otrzymanego z poprawnego zapytania
	 * @return informacja czy pobrany zas�b jest zasobem poprawnym z punktu widzenia u�ytkownika
	 */
	private boolean checkHeaderAndResponse()
	{
		
		if(currentHeader == null  || currentResponse == null)
		{
			log4j.warn("Header lub Response s� r�wne null.");
			return false;
		}
		else if(currentHeader.equals("") || currentResponse.equals(""))
		{
			log4j.warn("Header lub Response s� puste.");
			return false;
		}
				
		String headerCode = currentHeader.substring(9,12);
		
		if(headerCode.equals("200"))
		{
			log4j.info("Pobrano poprawny zas�b.");
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
			log4j.info("Zas�b zosta� przeniesiony.");
			
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
				log4j.error("B��dny format URL"+e.getMessage());
			}
			
			return false;
		}
		else
		{
			log4j.info("Nie obs�u�y�em zapytania:"+currentHeader);
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
		connectionProblems = false;
		
		do
		{
			//Pobiera kolejne zapytanie z kolejki zapyta�
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
				//Wystarczy dzi�ki specyfikacji BlockingQueue
				try {
					pagesToAnalise.addPage(currentResponse);
					log4j.info("Dodaj� now� stron� do kolejki stron.");
				} catch (InterruptedException e) {
					log4j.error("Niepoprawnie wybudzony przy dok�adaniu nowej strony:"
							+e.getMessage());
				}		
			}
				
		}while(!requestCreator.getRequests().isEmpty() && !connectionProblems);
		
		currentHeader = "";
		currentResponse = "";
		
		BuScrapper.numberOfWorkingDownloadThreads.decrementAndGet();
		log4j.info("DownloadThread o id "+threadId+" ko�czy prac�!");
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadanie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w, implementacj� Downloader, kt�ra b�dzie udost�pnia�a strumienie potrzebne przy
	 * uzyskiwaniu zasob�w, implementacj� PagesBuffer do zesk�adowania ju� pobranych zasob�w,
	 * kolejk� zapyta� przygotowan� przez RequestCreator.
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku
	 *        nadrz�dnym
	 * @param requestCreator obiekt udost�pniaj�cy kolejk� zapyta� do wykonania, oraz
	 * 		  pozwalaj�cy na zesk�adowanie wykona�, kt�re musz� zosta� powt�rzone
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez
	 * @param requests kolejka blokuj�ca zapyta� do wykonania  
	 *        siebie metod
	 * @param downloader obiekt przechowuj�cy i udost�pniaj�cy strumienie do pobierania 
	 *        zasob�w

	 */
	DownloadThread(int id,PagesBuffer pagesToAnalise, RequestCreator requestCreator,
			Downloader downloader)
	{
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.requestCreator = requestCreator;
		this.downloader = downloader;
		log4j.info("DownloadThread o id "+threadId+" rozpoczyna prac�!");
	}
}