package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

import pl.edu.agh.kis.storeinfo.StoreBusInfo;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest wyodr�bnienie zawarto�ci ze strony, kt�ra znajduje
 * si� ju� w buforze. W przypadku zauwa�enia pusto�ci bufora, w�tek jest usypiany w celu
 * umo�liwienia producentom nadrobienia stron. W przypadku zauwa�enia pusto�ci bufora oraz
 * braku pracuj�cych w�tk�w pobieraj�cych, w�tek wy�uskuj�cy decyduje si� na zako�czenie
 * swojej metody run. B��dy oraz wa�niejsze kroki programu s� umieszczane w logach. B��dy 
 * oraz wa�niejsze kroki programu s� umieszczane w logach.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class SnatchThread extends Thread {
	
	private String allLinesEx = "//a[starts-with(@class,'linia')]";
	
	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(SnatchThread.class.getName());
	
	/**
	 * Unikatowy numer przyporz�dkowany danemu w�tkowi
	 */
	private int threadId;
	
	/**
	 * Zestaw wyra�e� XPath, z kt�rych b�dziemy korzysta�
	 * W tej wersji programu, powinni�my otrzyma� sze�� wyra�e�, w kolejno�ci: pierwsze
	 * odpowiada za numer linii, druga za nazw� przystanku, trzecia za godzin�, czwarta
	 * za minuty dnia powszedniego, pi�ta sobot�, a sz�sta niedzieli
	 */
	private Map<String,String> xPathExpressions;
	
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
	 * Obiekt przechowuj�cy aktualnie przetwarzan� stron�
	 */
	private String currentPage;
	
	/**
	 * Funkcja s�u�y do przetworzenia stron zapisanych w j�zyku HTML na dokument XML przy
	 * u�yciu pakietu JTidy, wykorzystuj�c do tego dwie tablice bit�w, potrzebne dla 
	 * funkcji parse z klasy Tidy, zapisuj�c wynik tej operacji ze strumienia odpowiadaj�cego
	 * za strumie� wyj�cia funkcji, do przygotowanego String'a. Wynik jest jeszcze opakowywany
	 * przez znaczniki <myroot></myroot> w celu uchronienia si� przed wynikiem przetwarznia,
	 * w kt�rym nie istnia�by jeden korze�, co mo�e si� zdarzy� przy specyficznie zbudowanych
	 * stronach HTML.
	 * @param pageHTML pe�ny kod �r�d�owy HTML strony, kt�r� b�dziemy przetwarza�
	 * @return pe�ny kod �r�d�owy strony przetworzony do formy zgodnej z XML
	 */
	private String prepareXMLPage(String pageHTML)
	{		
		String result = "";	
		Tidy tidy = new Tidy();
		tidy.setInputEncoding("UTF-8");
	    tidy.setOutputEncoding("UTF-8");
	    tidy.setWraplen(Integer.MAX_VALUE);
	    tidy.setPrintBodyOnly(true);
	    tidy.setXmlOut(true);
	    tidy.setSmartIndent(true);
	    tidy.setQuiet(true);
	    tidy.setShowErrors(0);
	    tidy.setShowWarnings(false);
	    tidy.setForceOutput(true);
	    
		try (ByteArrayInputStream inputStream = 
				new ByteArrayInputStream(pageHTML.getBytes("UTF-8"));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
		{
		    tidy.parseDOM(inputStream, outputStream);
			result = outputStream.toString("UTF-8");
			log4j.info("Utworzy�em dokument XHTML z dokumentu HTML");
		} catch (UnsupportedEncodingException e) {
			log4j.error("B��d kodowania:"+e.getMessage());
		} catch (IOException e) {
			log4j.error("B��d wej�cia/wyj�cia:"+e.getMessage());
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("<myroot>");
		builder.append(result);
		builder.append("</myroot>");

		return builder.toString();
	}
	
	/**
	 * Funkcja odpowiedzialna jest za wyodr�bnienie przydatnych informacji 
	 * z pe�nego kodu �r�d�owego strony przy pomocy przechowywanych XPath, 
	 * oraz zwr�cenie wyniku wyodr�bnienia zesk�adowanego w mapie
	 * @param pageXHTML kod �r�d�owy strony w formacie XHTML
	 * @return mapa wyodr�bnionych danych
	 */
	private Map<String,String> analiseXMLPage(String pageXHTML) {
		
		Map<String,String> timeXPathExpressions = new HashMap<String,String>();
		
		timeXPathExpressions.put("XPathHours", 
				xPathExpressions.get("XPathHours"));
		
		timeXPathExpressions.put("XPathMinutesOrdinary", 
				xPathExpressions.get("XPathMinutesOrdinary"));
		
		timeXPathExpressions.put("XPathMinutesSaturday", 
				xPathExpressions.get("XPathMinutesSaturday"));
		
		timeXPathExpressions.put("XPathMinutesSunday", 
				xPathExpressions.get("XPathMinutesSunday"));
			
		Map<String,String> results = new HashMap<String,String>();
		String direction = "";
		String lineNumber = "";
		String buStopName = "";
		int hoursLength = 0;
		ArrayList<String> validMinutes = new ArrayList<String>();
		StringBuilder builder;
		
		try {

		    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document docXML = docBuilder.
		    		parse(new ByteArrayInputStream(pageXHTML.getBytes("UTF-8")));
		    
		    XPath xPath =  XPathFactory.newInstance().newXPath();
 		    		    
		    buStopName = xPath.compile(xPathExpressions.get("XPathBusStopName")).
		    		evaluate(docXML).replaceAll("\\s","");
		   
		    lineNumber = xPath.compile(xPathExpressions.get("XPathLineNumber")).
		    		evaluate(docXML).replaceAll("\\s","");
		    
		    direction = xPath.compile(xPathExpressions.get("XPathLineDirection"))
		    		.evaluate(docXML).replaceAll("\\s","");
		    	
		    NodeList timeNodes; 
		    
		    for(String l : timeXPathExpressions.keySet())
		    {
		    	builder = new StringBuilder();
		    	timeNodes = (NodeList) xPath.compile(timeXPathExpressions.get(l)).
			    		evaluate(docXML, XPathConstants.NODESET);
		    			
		    	if(timeNodes == null || timeNodes.getLength() == 0)
		    	{
		    		validMinutes.add(l);
		    	}
		    	
		    	for (int i = 0; i < timeNodes.getLength(); ++i) 
			    {
		    		Node nod = timeNodes.item(i);
		    		if(nod.getNodeType() == Node.ELEMENT_NODE)
		    		{
		    			builder.append(nod.getTextContent());
		    			builder.append(":\n");
		    			if(l.equals("XPathHours"))
		    				hoursLength++;
		    		}
		    	}
		    	
		    	results.put(l, builder.toString());
		    }
		    log4j.info("Wyodr�bni�em informacje ze strony XHTML");
		} catch (SAXException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());
		} catch (ParserConfigurationException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());
		} catch (XPathExpressionException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());
		} catch (IOException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage()); 
		}
		
		builder = new StringBuilder();
		for(int i = 0; i < hoursLength; ++i)
		{
			builder.append(":\n");
		}
		
		for(String l : validMinutes)
		{
			results.put(l, builder.toString());
		}
		
		results.put("XPathLineNumber", lineNumber);
		results.put("XPathLineDirection", direction);
		results.put("XPathBusStopName", buStopName);

		return results;
	}
	/**
	 * G��wna p�tla w�tku - konsumenta, jej zadaniem jest pobieranie strony z bufora, oraz 
	 * w�r�d zawartych na niej danych, wyodr�bnienie danych pasuj�cych do posiadanych przez
	 * w�tek wyra�e� XPath oraz p�zniejsze zesk�adowanie ich, oddelegowuj�c zadanie do
	 * odpowiedniego obiektu implementuj�cego interfejs StoreBusInfo. Na pocz�tku funkcji
	 * nast�puje zwi�kszenie warto�ci pola statycznego przechowywuj�cego liczb� pracuj�cych
	 * w�tk�w wy�uskuj�cych, natomiast na ko�cu funkcji nast�pi zmniejszenie tej ilo�ci, za
	 * ka�dym razem nast�puje zmiana o warto�� r�wn� jeden. W przypadku pustego
	 * bufora, maj� zosta� obudzone w�tki pobieraj�ce, je�eli zostanie przekroczony czas
	 * oczekiwania na buforze, zostanie zwr�cony obiekt null, dlatego bezpo�rednio po
	 * pobraniu zabezpieczamy si� sprawdzaj�c czy otrzymany zas�b nie jest null'em.
	 * W sytuacji gdy w�tki pobieraj�ce jeszczce pracuj�, ale bufor jest pusty, w�tek
	 * wy�uskuj�cy zostaje u�piony.
	 */
	public void run()
	{	
		BuScrapper.numberOfWorkingSnatchThreads.incrementAndGet();

		do
		{
			if(!pagesToAnalise.isEmpty())
			{
				try {
					currentPage = pagesToAnalise.pollPage();
					log4j.info("Pobieram z kolejki stron");
				} catch (InterruptedException e) {
					log4j.error("W�tek niepoprawnie wybudzony:"+e.getMessage());
				} finally {
					if(currentPage == null)
					{
						continue;
					}
				}
				log4j.info("Pobra�em z kolejki stron");
				infoSaving.storeInfo(analiseXMLPage(prepareXMLPage(
						currentPage)));
				log4j.info("Strona zosta�a przetworzona");
			}
			else
			{
				log4j.info("Kolejka pusta, usypiamy w oczekiwaniu na producenta.");
				try {
					sleep(1000);
					yield();
				} catch (InterruptedException e) {
					log4j.error("W�tek niepoprawnie wybudzony:"+e.getMessage());
				}
			}
				
		}while((BuScrapper.numberOfWorkingDownloadThreads.intValue() > 0 
				|| !pagesToAnalise.isEmpty()) );
		 
		BuScrapper.numberOfWorkingSnatchThreads.decrementAndGet();
		log4j.info("SnatchThread o id "+threadId+" ko�czy prac�!");
	}
	

	/**
	 * Konstruktor sparametryzowany, kt�ry przypisuje nowotworzonemu obiektowi jego numer
	 * identyfikacyjny, bufor stron dla konsumenta, implementacj� interfejsu StoreBusInfo,
	 * do kt�rej b�dzie oddelegowywa� zadanie zapisania informacji wyodr�bionych ze stron,
	 * oraz map� zapyta� XPath, gdzie kluczemjest nazwa danego zapytania, natomiast
	 * warto�ci� samo zapytanie.
	 * @param id unikatowy numer w�tku wzgl�dem innych w�tk�w tej klasy, wykonywanych
	 * 		w jednej puli w�tk�w.
	 * @param pagesToAnalise implementacja interfejsu PagesBuffer, umo�liwiaj�ca pobranie
	 * 		stron w celu wyodr�bnienia z nich informacji.
	 * @param infoSaving implementacja interfejsu StoreBusInfo, odpowiedzialna za zesk�adowanie
	 * 		wyodr�bnionych informacji.
	 * @param xPathExpressions mapa zapyta� XPath, gdzie kluczem jest nazwa danego zapytania,
	 * natomiast warto�ci� samo zapytanie. Wa�ne, aby zar�wno obiekt wy�uskuj�cy jak
	 * i przechowywana przez niego implementacja interfejsu StoreBusInfo zna�a mo�liwe
	 * nazwy zapyta� i aby by�y one sp�jne.
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			Map<String,String> xPathExpressions)
	{
		log4j.info("SnatchThread o id "+id+" rozpoczyna prac�!");
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
		this.xPathExpressions = xPathExpressions;
	}
}
