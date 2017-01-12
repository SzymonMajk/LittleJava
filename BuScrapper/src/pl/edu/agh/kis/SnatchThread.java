package pl.edu.agh.kis;

/**
 * Klasa w¹tków, których zadaniem jest wyodrêbnienie zawartoœci ze strony, która znajduje
 * siê ju¿ w buforze, metod tej klasy nie interesuje pochodzenie stron, a jedynie ustalony
 * w odpowiednim pliku XPath elementów, które musi wyodrêbniæ, posiada obiekt implementuj¹cy 
 * interfejs StoreBusInfo, do którego oddelegowuje obowi¹zek zesk³adowania otrzymanych 
 * informacji, posiada równie¿ swój w³asny system logów, z wyjœciem do pliku o nazwie równej 
 * id dzia³aj¹cego w¹tku
 * @author Szymon Majkut
 * @version 1.0
 *
 */
public class SnatchThread extends Thread{
	
	/**
	 * W³asny system logów
	 */
	private Logger snatchLogger;
	
	/**
	 * Unikatowa nazwa przyporz¹dkowana danemu w¹tkowi
	 */
	private String threadName;
	
	/**
	 * Referencja do bufora przechowuj¹cego strony do przetworzenia
	 */
	private PagesBuffer pagesToAnalise;
	
	/**
	 * Obiekt odpowiedzialny za sk³adowanie informacji zdobytych przez analiseXMLPage
	 * ka¿dy w¹tek posiada swój unikatowy
	 */
	private StoreBusInfo infoSaving;
	
	/**
	 * Funkcja s³u¿y do przetworzenia stron zapisanych w jêzyku HTML na strony XHTML,
	 * na których mo¿emy korzystaæ z przechodzenia poprzez XPath
	 * @param pageHTML pe³ny kod Ÿród³owy strony, któr¹ bêdziemy przetwarzaæ
	 * @return pe³ny kod Ÿród³owy strony przetworzony do formy XHTML'a
	 */
	private String prepareXMLPage(String pageHTML)
	{
		//zabawa, ¿eby dodaæ XML...
		return null;
	}
	
	/**
	 * Funkcja odpowiedzialna za zaimplementowanie interfejsu AnalizePage, jej zadaniem
	 * jest wyodrêbnienie przydatnych informacji z pe³nego kodu Ÿród³owego strony przy
	 * pomocy odpowiednich XPath, oraz zwrócenie ich w postaci jednego Stringa do dalszej
	 * obróbki
	 * @param pageXHTML pe³ny kod Ÿród³owy strony, z którego bêdziemy wyodrêbniaæ
	 * @return zesk³adowane informacje wyodrêbnione ze strony podanej w parametrze
	 */
	private String analiseXMLPage(String pageXHTML) {
		// zabawy z XPath...
		return null;
	}
	
	/**
	 * G³ówna pêtla w¹tku - konsumenta, jej zadaniem jest oddawanie czasu procesora w przypadku
	 * pustego bufora, w przeciwnym wypadku pobieranie z niego strony, oraz przetworzenie zawartych
	 * na stronie informacji, oraz pózniejsze zesk³adowanie ich, oddelegowuj¹c zadanie do
	 * odpowiedniego obiektu implementuj¹cego interfejs StoreBusInfo
	 */
	public void run()
	{		
		//Najszersza pêtla do while z warunkiem czy jakiekolwiek w¹tki jeszcze pracuj¹
		
		//Sprawdzamy ca³y czas czy kolejka pusta, póki pusta to yieldujemy
		
		//jeœli nie jest, to zaznaczamy, ¿e w¹tek pracuje
		//BuScrapper.numberOfWorkingThreads.incrementAndGet();
		
		//pobieramy zawartoœæ z kolejki jeœli nadal nie jest pusta, gdyby teraz
		//okaza³a siê pusta, to robimy zmniejszenie liczby pracuj¹cych w¹tków i continue
		
		//Get HTML page
		
		//Prepare XHTML page
		
		//Get Info from XHTML page
		
		//Store infos
		
		//po zakoñczonej pracy informujemy ¿e w¹tek skoñczy³ pracê
	    //BuScrapper.numberOfWorkingThreads.decrementAndGet();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów, mia³ te¿ dostêp do bufora z kolejnymi pobranymi stronami, oraz podarowany przez
	 * w¹tek nadrzêdny obiekt, któremu bêdzie delegowa³ sk³adowanie uzyskanych danych
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w¹tków w w¹tku nadrzêdnym
	 * @param pagesToAnalise referencja do synchronizowanego bufora z pobranymi stronami
	 * @param infoSaving referencja do obiektu, któremu nale¿y delegowaæ sk³adowanie informacji
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving)
	{
		threadName = "SnatchThread number " + id;
		snatchLogger = new Logger();
		snatchLogger.changeAppender(new FileAppender(threadName));
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
	}
}
