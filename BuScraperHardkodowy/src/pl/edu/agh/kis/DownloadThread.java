package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

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
 * @version 1.4
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
	 * na dogodne testowanie, posiada na sztywno ustalone kodowanie UTF-8.
	 * @param request poprawne zapytanie do servera uzyskane z kolejki udostêpnionej przez
	 * 		  RequestCreator
	 * @return tablica obiektów String, elementem o indeksie zero jest nag³ówek odpowiedzi,
	 *        natomiast elementem o indeksie jeden jest treœæ odpowiedz
	 * @throws UnknownHostException wyrzucany w przypadku b³êdnego ustalenia stanu obiektu
	 *         przeznaczonego do komunikacji sieciowej lub braku dostêpu do internetu
	 * @throws IOException wyrzucany gdy nastêpuje problem z korzystaniem ze strumieni
	 *         na poziomie niesieciowym
	 */
	public String[] respond(String request) throws UnknownHostException, IOException
	{
		String line = "";
		StringBuilder respond = new StringBuilder();
		StringBuilder header = new StringBuilder();
		OutputStreamWriter to;
		BufferedReader from;
		
		if(downloader.initDownloader())
		{
			try {
				
				to = new OutputStreamWriter(
						downloader.getOutputStream(),"UTF-8");
				to.write(request);
				to.flush();
				log4j.info("Wykonujê request: "+request);
				
				from = new BufferedReader(new InputStreamReader(
						downloader.getInputSteam(),"UTF-8"));			
				
				while((line = from.readLine()) != null)
				{
					if(line.equals(""))
					{
						break;
					}
					header.append(line);
				}
				
				//TODO wyci¹gnijmy z headera charset dla odpowiedzi i inne ¿byry jak trzeba..
								
				while((line = from.readLine()) != null)
				{
					respond.append(line);
				}
				
				downloader.closeStreams();
				
			} catch (UnsupportedEncodingException e1) {
				log4j.error("Niew³aœciwe kodowanie dokumentu:"+e1.getMessage());
				e1.printStackTrace();		
			} catch (IOException e) {
				log4j.error("Problem z czytaniem odpowiedzi servera:"+e.getMessage());
			} 
		}
		else
		{
			log4j.error("Niemo¿liwe zainicjowanie po³¹czenia.");
		}

		//TODO to zmienimy na przypisywanie prywatnych pól!
		String[] allRespond = {header.toString(),respond.toString()};
		
		return allRespond;
	}
	
	/**
	 * Funkcja s³u¿y do odfiltrowania od niepoprawnych odpowiedzi
	 * @param header nag³ówek zasobu otrzymanego z poprawnego zapytania
	 * @return informacja czy pobrany zasób jest zasobem poprawnym z punktu widzenia u¿ytkownika
	 */
	private boolean isCorrectRespond(String[] respond)
	{
		if(respond[0] == null || respond[0].equals("") || 
				respond[1] == null || respond[1].equals(""))
		{
			log4j.warn("Zamiast zasobu otrzyma³em null.");
			return false;
		}
		else if(respond[0].contains("HTTP/1.1 200 OK") || 
				respond[0].contains("HTTP/1.0 200 OK"))
		{
			log4j.info("Pobrano poprawny zasób.");
			return true;
		}
		else
		{
			log4j.
			info("Nie zwrócono poprawnego zasobu, nie wstawiam go do kolejki:"+
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
		//TODO zaprzêgniêcie w to ca³e obiektu odpowiedzialnego za zarz¹dzanie b³êdami
		//teraz musimy odró¿niaæ kiedy w¹tek koñczy siê b³êdem a kiedy poprawnie
		
		BuScrapper.numberOfWorkingDownloadThreads.incrementAndGet();
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
				else
				{
					BuScrapper.correctTaskExecute.set(true);
					break;
				}

				if(isCorrectRespond(respondFromServer))
				{
					//Wystarczy dziêki specyfikacji BlockingQueue
					pagesToAnalise.addPage(respondFromServer[1]);
					log4j.info("Dodajê now¹ stronê do kolejki stron.");
				}
				
			}while(BuScrapper.numberOfWorkingDownloadThreads.intValue() > 0);
		} catch (InterruptedException e) {
			log4j.error("Niepoprawnie wybudzony!"+e.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} catch (UnknownHostException e) {
			log4j.error("Problem z po³¹czeniem internetowym!"+e.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} catch (IOException e) {
			log4j.error("Problem ze strumieniami!"+e.getMessage());
			BuScrapper.numberOfWorkingDownloadThreads.set(0);
			BuScrapper.correctTaskExecute.set(false);
		} catch (Throwable t) {
			log4j.error("Powa¿ny problem!"+t.getMessage());
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
		}
				
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
	 * @param requests snychronizowana kolejka zapytañ
	 * @param pagesToAnalise bufor stron, zapewniaj¹cy blokowanie udostêpnianych przez 
	 *        siebie metod
	 * @param downloader obiekt przechowuj¹cy i udostêpniaj¹cy strumienie do pobierania 
	 *        zasobów

	 */
	DownloadThread(int id,BlockingQueue<String> requests,PagesBuffer pagesToAnalise,
			Downloader downloader)
	{
		threadId = id;
		this.requests = requests;
		this.pagesToAnalise = pagesToAnalise;
		this.downloader = downloader;
		log4j.info("DownloadThread o id "+threadId+" rozpoczyna pracê!");
	}
	
	//TODO, niech w konstrutkorze dostaj¹ tylko buffer. id i downloadera, a requesty
	//przy odpalaniu w pêtli ju¿ w osobnej funkcji!
}