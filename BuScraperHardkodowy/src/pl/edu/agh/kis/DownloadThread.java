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
import java.util.concurrent.BlockingQueue;
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
 * z metod prywatnych wyj�tki. B��dy oraz wa�niejsze kroki programu s� umieszczane w logach.
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
	 * Kolejka poprawnych zapyta�
	 */
	private BlockingQueue<Request> requestsToDo;
	
	/**
	 * Kolejka zada� do powt�rzenia
	 */
	private BlockingQueue<Request> requestsToRepeat;
	
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
		try {
			requestsToRepeat.put(request);
		} catch (InterruptedException e) {
			log4j.error("Nieudana pr�ba zesk�adowania zapytania do powt�rzenia: "
					+e.getMessage());
		}
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
	 * Funkcja odpowiada za przeprowadzenie procesu wys�ania zapytania oraz pobrania
	 * odpowiedzi od servera przy zg�oszeniu problem�w w przypadku problem�w z przeprowadzeniem
	 * tego procesu. Otrzymuje obiekt Request, przy pomocy kt�rego buduje zapytanie 
	 * do servera, nast�pnie wysy�a to zapytanie, odczytuje nag��wek oraz tre�� odpowiedzi,
	 * po czym zapisuje je do prywatnych p�l obiektu. Je�eli w trakcie komunikacji wyst�pi
	 * wyj�tek zwi�zany z niepoprawnym kodowaniem lub wyj�tek typu wej�cia/wyj�cia, lub
	 * obiekt odpowiedzialny za stworzenie zapytania b�dzie pusty lub strumienie do komunikacji
	 * nie zostan� poprawnie zainicjowane, spowoduje to ustalenie prywatnego pola logicznego
	 * connectionProglems na warto�� prawdy oraz przerwanie samej funkcji z warto�ci�
	 * logicznego fa�szu.
	 * @param request obiekt Request przechowuj�cy dane potrzebne do utworzenia zapytania, kt�re
	 * 		mo�e zosta� wys�ane do servera.
	 * @return zwraca warto�� logicznej prawdy, je�eli podczas wykonywania funkcji nie dosz�o
	 * 		do pobrania pustego zapytania, niemo�liwo�ci zainicjowania strumieni, wyst�pienia
	 * 		wyj�tku typu wej�cia/wyj�cia przy wysy�aniu zapytania lub odbieraniu odpowiedzi,
	 *		wyj�tku typu niewspieranego kodowania, w przeciwnym wypadku zwr�ci warto��
	 *		logicznej prawdy.
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
	 * G��wna p�tla w�tku - producenta, dzia�aj�cego na implementacji PagesBuffer otrzymanej
	 * w konstruktorze. Na pocz�tku dzia�ania funkcji zwi�kszana jest warto�� pola statycznego
	 * obiektu BuScrapper informuj�cego o ilo�ci w�tk�w �ci�gaj�cych, natomiast po przej�ciu
	 * funkcji warto�� ta jest zmniejszana. Funckja wykonuje p�tle, kt�rej warunkami zako�czenia
	 * s� niepusto�� kolejki zapyta� do wykonania oraz warto�� logiczna pola prywatnego
	 * connectionProblems, kt�ra mo�e zosta� ustalona na logiczn� prawd� w funkcji
	 * proceedRequest. W poprawnym wykonaniu z kolejki pobierane jest nowe zapytanie,
	 * nast�pnie poprzez funkcj� proceedRequest jest ono wysy�ane a nag��wek oraz cia�o
	 * odpowiedzi zostaje zapisne w prywatnych polach obiektu. Sprawdzana jest poprawno��
	 * otrzymanych p�l, je�eli nast�pi wyst�pienie nag��wka powoduj�cego konieczno�� powt�rzenia
	 * zapytania, zapytanie trafia do kolejki zapyta� do ponownego wykonania, a cia�o odpowiedzi
	 * nie jest umieszczana w buforze producenta. Je�eli natomiast nag��wek jak i cia�o
	 * odpowiedzi oka�� si� poprawne, cia�o odpowiedzi zostaje umieszczone w buforze producenta.
	 */
	public void run()
	{			
		BuScrapper.numberOfWorkingDownloadThreads.incrementAndGet();
		connectionProblems = false;
		
		do
		{
			try {
				currentRequest = requestsToDo.poll(2, TimeUnit.SECONDS);
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
				try {
					pagesToAnalise.addPage(currentResponse);
					log4j.info("Dodaj� now� stron� do kolejki stron.");
				} catch (InterruptedException e) {
					log4j.error("Niepoprawnie wybudzony przy dok�adaniu nowej strony:"
							+e.getMessage());
				}		
			}
				
		}while(!requestsToDo.isEmpty() && !connectionProblems);
		
		currentHeader = "";
		currentResponse = "";
		
		BuScrapper.numberOfWorkingDownloadThreads.decrementAndGet();
		log4j.info("DownloadThread o id "+threadId+" ko�czy prac�!");
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�ry przypisuje nowotworzonemu obiektowi jego numer
	 * identyfikacyjny, bufor stron producenta, kolejk� zapyta�, kt�re nale�y wykona�,
	 * kolejk� zapyta� do ponownego wykonania, w kt�rej obiekt mo�e umieszcza� takowe
	 * zapytania, je�eli stwierdzi tak� konieczno��, oraz obiekt implementuj�cy interfejs
	 * Downloader, kt�rego zadaniem jest inicjowanie oraz udost�pnianie strumieni, potrzebnych
	 * przy komunikacji, w celu uzyskania odpowiedzi dla jakiego� zapytania.
	 * @param id unikatowy numer w�tku wzgl�dem innych w�tk�w tej klasy, wykonywanych
	 * 		w jednej puli w�tk�w.
	 * @param pagesToAnalise implementacja interfejsu PagesBuffer, umo�liwiaj�ca umieszczanie
	 * 		poprawnie pobranych stron w buforze dla w�tk�w wy�uskuj�cych.
	 * @param requestsToDo kolejka obiekt�w Request, zawieraj�cych detale, z kt�rych mo�liwe
	 * 		jest utworzenie zapytania.
	 * @param requestsToRepeat kolejka obiekt�w Request, kt�re musz� zosta� powt�rzone,
	 * 		ze wzgl�du na mo�liwe, ale nie stwarzaj�ce poprawnych danych dzia�anie obiektu.
	 * @param downloader obiekt implementuj�cy interfejs Downloader, umo�liwiaj�cy
	 * 		zainicjowanie strumieni do komunikacji: zadania zapytania oraz odczytania
	 * 		odpowiedzi na to zapytanie, oraz udost�pniaj�cy te strumienie.
	 */
	DownloadThread(int id,PagesBuffer pagesToAnalise, BlockingQueue<Request> requestsToDo,
			BlockingQueue<Request> requestsToRepeat,Downloader downloader)
	{
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.requestsToDo = requestsToDo;
		this.requestsToRepeat = requestsToRepeat;
		this.downloader = downloader;
		log4j.info("DownloadThread o id "+threadId+" rozpoczyna prac�!");
	}
}