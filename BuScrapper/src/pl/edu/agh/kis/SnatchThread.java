package pl.edu.agh.kis;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest wyodr�bnienie zawarto�ci ze strony, kt�ra znajduje
 * si� ju� w buforze, metod tej klasy nie interesuje pochodzenie stron, a jedynie ustalony
 * w odpowiednim pliku XPath element�w, kt�re musi wyodr�bni�, posiada obiekt implementuj�cy 
 * interfejs StoreBusInfo, do kt�rego oddelegowuje obowi�zek zesk�adowania otrzymanych 
 * informacji, posiada r�wnie� sw�j w�asny system log�w, z wyj�ciem do pliku o nazwie r�wnej 
 * id dzia�aj�cego w�tku
 * @author Szymon Majkut
 * @version 1.1a
 *
 */
public class SnatchThread extends Thread{
	
	/**
	 * W�asny system log�w
	 */
	private Logger snatchLogger;
	
	/**
	 * Unikatowa nazwa przyporz�dkowana danemu w�tkowi
	 */
	private String threadName;
	
	/**
	 * Referencja do bufora przechowuj�cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	/**
	 * Obiekt odpowiedzialny za sk�adowanie informacji zdobytych przez analiseXMLPage
	 * ka�dy w�tek posiada sw�j unikatowy
	 */
	private StoreBusInfo infoSaving;
	
	/**
	 * Funkcja s�u�y do przetworzenia stron zapisanych w j�zyku HTML na strony XHTML,
	 * na kt�rych mo�emy korzysta� z przechodzenia poprzez XPath
	 * @param pageHTML pe�ny kod �r�d�owy strony, kt�r� b�dziemy przetwarza�
	 * @return pe�ny kod �r�d�owy strony przetworzony do formy XHTML'a
	 */
	private String prepareXMLPage(String pageHTML)
	{
		//zabawa, �eby doda� XML...
		
		snatchLogger.info("Utworzy�em dokument XHTML z dokumentu HTML");
		return "";
	}
	
	/**
	 * Funkcja odpowiedzialna za zaimplementowanie interfejsu AnalizePage, jej zadaniem
	 * jest wyodr�bnienie przydatnych informacji z pe�nego kodu �r�d�owego strony przy
	 * pomocy odpowiednich XPath, oraz zwr�cenie ich w postaci jednego Stringa do dalszej
	 * obr�bki, publiczna do testowania
	 * @param pageXHTML pe�ny kod �r�d�owy strony, z kt�rego b�dziemy wyodr�bnia�
	 * @return zesk�adowane informacje wyodr�bnione ze strony podanej w parametrze
	 */
	public String analiseXMLPage(String pageXHTML) {
		// zabawy z XPath...
		
		snatchLogger.info("Wyodr�bni�em informacje ze strony XHTML");
		return "";
	}
	
	/**
	 * G��wna p�tla w�tku - konsumenta, jej zadaniem jest oddawanie czasu procesora w przypadku
	 * pustego bufora, w przeciwnym wypadku pobieranie z niego strony, oraz przetworzenie zawartych
	 * na stronie informacji, oraz p�zniejsze zesk�adowanie ich, oddelegowuj�c zadanie do
	 * odpowiedniego obiektu implementuj�cego interfejs StoreBusInfo
	 */
	public void run()
	{		
		String tmpSite = "";
		
		do
		{
			BuScrapper.numberOfWorkingThreads.incrementAndGet();
			
			snatchLogger.info("Pobieram z kolejki stron");
			tmpSite = analiseXMLPage(prepareXMLPage(pagesToAnalise.takePage()));
			
			if(tmpSite != "")
			{
				snatchLogger.info("Sk�aduj� wyci�gni�te informacje");
				infoSaving.storeInfo(tmpSite);
			}

			snatchLogger.execute();
			BuScrapper.numberOfWorkingThreads.decrementAndGet();
			yield();
		}while(BuScrapper.numberOfWorkingThreads.intValue() > 0);
		
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w, mia� te� dost�p do bufora z kolejnymi pobranymi stronami, oraz podarowany przez
	 * w�tek nadrz�dny obiekt, kt�remu b�dzie delegowa� sk�adowanie uzyskanych danych
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku nadrz�dnym
	 * @param pagesToAnalise referencja do synchronizowanego bufora z pobranymi stronami
	 * @param infoSaving referencja do obiektu, kt�remu nale�y delegowa� sk�adowanie informacji
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving)
	{
		threadName = "SnatchThread number " + id;
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
		snatchLogger = new Logger();
		snatchLogger.changeAppender(new FileAppender(threadName));
		snatchLogger.info("SnatchThread o imieniu "+threadName+" rozpoczyna prac�!");
		snatchLogger.execute();
	}
}

/* Wszystkie wyci�gni�te informacje musz� by� odpowiednio sk��dowane,
 * w programie u�ywam konwencji, �e co�=warto��/r/n, wraz z unikatowo�ci�
 * mo�e by�
 * name=nazwaPrzystanku
 * number=numerLinii
 * hour=[0-2]godzina,kolejne minuty, po przecinkach; 0-2 oznacza rodzaj dnia
 * pozosta�e dane b�d� wrzucane jako warningi
 */
