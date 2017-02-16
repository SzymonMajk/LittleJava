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
 * Klasa w¹tków, których zadaniem jest umieszczenie poprawnie otrzymanej odpowiedzi
 * w obiekcie implementuj¹cym PagesBuffer. Do uzyskania odpowiedzi wykorzystuje strumienie 
 * uzyskane przez implementacjê interfejsu Downloader, oraz korzysta kolejki zapytañ, 
 * utworzonej przez RequestCreator. Ma dostêp do dwóch statycznych pól BuScrapper'a,
 * numberOfWorkingDownloadThreads okreœla liczbê wci¹¿ pracuj¹cych w¹tków pobieraj¹cych
 * natomiast correctTaskExecute okreœla czy w¹tki pobieraj¹ce nie pope³ni³y b³êdu przy
 * wykonywaniu danego zadania, aby w ten sposób unikn¹æ pomijania danych niepobranych
 * w poprawny sposób. Niepoprawnoœæ pobieranych danych jest okreœlana poprzez wy³apywane
 * z metod prywatnych wyj¹tki. B³êdy oraz wa¿niejsze kroki programu s¹ umieszczane w logach.
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
	 * Kolejka poprawnych zapytañ
	 */
	private BlockingQueue<Request> requestsToDo;
	
	/**
	 * Kolejka zadañ do powtórzenia
	 */
	private BlockingQueue<Request> requestsToRepeat;
	
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
		try {
			requestsToRepeat.put(request);
		} catch (InterruptedException e) {
			log4j.error("Nieudana próba zesk³adowania zapytania do powtórzenia: "
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
	 * Funkcja odpowiada za przeprowadzenie procesu wys³ania zapytania oraz pobrania
	 * odpowiedzi od servera przy zg³oszeniu problemów w przypadku problemów z przeprowadzeniem
	 * tego procesu. Otrzymuje obiekt Request, przy pomocy którego buduje zapytanie 
	 * do servera, nastêpnie wysy³a to zapytanie, odczytuje nag³ówek oraz treœæ odpowiedzi,
	 * po czym zapisuje je do prywatnych pól obiektu. Je¿eli w trakcie komunikacji wyst¹pi
	 * wyj¹tek zwi¹zany z niepoprawnym kodowaniem lub wyj¹tek typu wejœcia/wyjœcia, lub
	 * obiekt odpowiedzialny za stworzenie zapytania bêdzie pusty lub strumienie do komunikacji
	 * nie zostan¹ poprawnie zainicjowane, spowoduje to ustalenie prywatnego pola logicznego
	 * connectionProglems na wartoœæ prawdy oraz przerwanie samej funkcji z wartoœci¹
	 * logicznego fa³szu.
	 * @param request obiekt Request przechowuj¹cy dane potrzebne do utworzenia zapytania, które
	 * 		mo¿e zostaæ wys³ane do servera.
	 * @return zwraca wartoœæ logicznej prawdy, je¿eli podczas wykonywania funkcji nie dosz³o
	 * 		do pobrania pustego zapytania, niemo¿liwoœci zainicjowania strumieni, wyst¹pienia
	 * 		wyj¹tku typu wejœcia/wyjœcia przy wysy³aniu zapytania lub odbieraniu odpowiedzi,
	 *		wyj¹tku typu niewspieranego kodowania, w przeciwnym wypadku zwróci wartoœæ
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
	 * G³ówna pêtla w¹tku - producenta, dzia³aj¹cego na implementacji PagesBuffer otrzymanej
	 * w konstruktorze. Na pocz¹tku dzia³ania funkcji zwiêkszana jest wartoœæ pola statycznego
	 * obiektu BuScrapper informuj¹cego o iloœci w¹tków œci¹gaj¹cych, natomiast po przejœciu
	 * funkcji wartoœæ ta jest zmniejszana. Funckja wykonuje pêtle, której warunkami zakoñczenia
	 * s¹ niepustoœæ kolejki zapytañ do wykonania oraz wartoœæ logiczna pola prywatnego
	 * connectionProblems, która mo¿e zostaæ ustalona na logiczn¹ prawdê w funkcji
	 * proceedRequest. W poprawnym wykonaniu z kolejki pobierane jest nowe zapytanie,
	 * nastêpnie poprzez funkcjê proceedRequest jest ono wysy³ane a nag³ówek oraz cia³o
	 * odpowiedzi zostaje zapisne w prywatnych polach obiektu. Sprawdzana jest poprawnoœæ
	 * otrzymanych pól, je¿eli nast¹pi wyst¹pienie nag³ówka powoduj¹cego koniecznoœæ powtórzenia
	 * zapytania, zapytanie trafia do kolejki zapytañ do ponownego wykonania, a cia³o odpowiedzi
	 * nie jest umieszczana w buforze producenta. Je¿eli natomiast nag³ówek jak i cia³o
	 * odpowiedzi oka¿¹ siê poprawne, cia³o odpowiedzi zostaje umieszczone w buforze producenta.
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
					log4j.info("Dodajê now¹ stronê do kolejki stron.");
				} catch (InterruptedException e) {
					log4j.error("Niepoprawnie wybudzony przy dok³adaniu nowej strony:"
							+e.getMessage());
				}		
			}
				
		}while(!requestsToDo.isEmpty() && !connectionProblems);
		
		currentHeader = "";
		currentResponse = "";
		
		BuScrapper.numberOfWorkingDownloadThreads.decrementAndGet();
		log4j.info("DownloadThread o id "+threadId+" koñczy pracê!");
	}
	
	/**
	 * Konstruktor sparametryzowany, który przypisuje nowotworzonemu obiektowi jego numer
	 * identyfikacyjny, bufor stron producenta, kolejkê zapytañ, które nale¿y wykonaæ,
	 * kolejkê zapytañ do ponownego wykonania, w której obiekt mo¿e umieszczaæ takowe
	 * zapytania, je¿eli stwierdzi tak¹ koniecznoœæ, oraz obiekt implementuj¹cy interfejs
	 * Downloader, którego zadaniem jest inicjowanie oraz udostêpnianie strumieni, potrzebnych
	 * przy komunikacji, w celu uzyskania odpowiedzi dla jakiegoœ zapytania.
	 * @param id unikatowy numer w¹tku wzglêdem innych w¹tków tej klasy, wykonywanych
	 * 		w jednej puli w¹tków.
	 * @param pagesToAnalise implementacja interfejsu PagesBuffer, umo¿liwiaj¹ca umieszczanie
	 * 		poprawnie pobranych stron w buforze dla w¹tków wy³uskuj¹cych.
	 * @param requestsToDo kolejka obiektów Request, zawieraj¹cych detale, z których mo¿liwe
	 * 		jest utworzenie zapytania.
	 * @param requestsToRepeat kolejka obiektów Request, które musz¹ zostaæ powtórzone,
	 * 		ze wzglêdu na mo¿liwe, ale nie stwarzaj¹ce poprawnych danych dzia³anie obiektu.
	 * @param downloader obiekt implementuj¹cy interfejs Downloader, umo¿liwiaj¹cy
	 * 		zainicjowanie strumieni do komunikacji: zadania zapytania oraz odczytania
	 * 		odpowiedzi na to zapytanie, oraz udostêpniaj¹cy te strumienie.
	 */
	DownloadThread(int id,PagesBuffer pagesToAnalise, BlockingQueue<Request> requestsToDo,
			BlockingQueue<Request> requestsToRepeat,Downloader downloader)
	{
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.requestsToDo = requestsToDo;
		this.requestsToRepeat = requestsToRepeat;
		this.downloader = downloader;
		log4j.info("DownloadThread o id "+threadId+" rozpoczyna pracê!");
	}
}