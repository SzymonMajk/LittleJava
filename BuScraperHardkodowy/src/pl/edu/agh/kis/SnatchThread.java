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
 * Klasa w¹tków, których zadaniem jest wyodrêbnienie zawartoœci ze strony, która znajduje
 * siê ju¿ w buforze. W przypadku zauwa¿enia pustoœci bufora, w¹tek jest usypiany w celu
 * umo¿liwienia producentom nadrobienia stron. W przypadku zauwa¿enia pustoœci bufora oraz
 * braku pracuj¹cych w¹tków pobieraj¹cych, w¹tek wy³uskuj¹cy decyduje siê na zakoñczenie
 * swojej metody run. B³êdy oraz wa¿niejsze kroki programu s¹ umieszczane w logach. B³êdy 
 * oraz wa¿niejsze kroki programu s¹ umieszczane w logach.
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
	 * Unikatowy numer przyporz¹dkowany danemu w¹tkowi
	 */
	private int threadId;
	
	/**
	 * Zestaw wyra¿eñ XPath, z których bêdziemy korzystaæ
	 * W tej wersji programu, powinniœmy otrzymaæ szeœæ wyra¿eñ, w kolejnoœci: pierwsze
	 * odpowiada za numer linii, druga za nazwê przystanku, trzecia za godzinê, czwarta
	 * za minuty dnia powszedniego, pi¹ta sobotê, a szósta niedzieli
	 */
	private Map<String,String> xPathExpressions;
	
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
	 * Obiekt przechowuj¹cy aktualnie przetwarzan¹ stronê
	 */
	private String currentPage;
	
	/**
	 * Funkcja s³u¿y do przetworzenia stron zapisanych w jêzyku HTML na dokument XML przy
	 * u¿yciu pakietu JTidy, wykorzystuj¹c do tego dwie tablice bitów, potrzebne dla 
	 * funkcji parse z klasy Tidy, zapisuj¹c wynik tej operacji ze strumienia odpowiadaj¹cego
	 * za strumieñ wyjœcia funkcji, do przygotowanego String'a. Wynik jest jeszcze opakowywany
	 * przez znaczniki <myroot></myroot> w celu uchronienia siê przed wynikiem przetwarznia,
	 * w którym nie istnia³by jeden korzeñ, co mo¿e siê zdarzyæ przy specyficznie zbudowanych
	 * stronach HTML.
	 * @param pageHTML pe³ny kod Ÿród³owy HTML strony, któr¹ bêdziemy przetwarzaæ
	 * @return pe³ny kod Ÿród³owy strony przetworzony do formy zgodnej z XML
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
			log4j.info("Utworzy³em dokument XHTML z dokumentu HTML");
		} catch (UnsupportedEncodingException e) {
			log4j.error("B³¹d kodowania:"+e.getMessage());
		} catch (IOException e) {
			log4j.error("B³¹d wejœcia/wyjœcia:"+e.getMessage());
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("<myroot>");
		builder.append(result);
		builder.append("</myroot>");

		return builder.toString();
	}
	
	/**
	 * Funkcja odpowiedzialna jest za wyodrêbnienie przydatnych informacji 
	 * z pe³nego kodu Ÿród³owego strony przy pomocy przechowywanych XPath, 
	 * oraz zwrócenie wyniku wyodrêbnienia zesk³adowanego w mapie
	 * @param pageXHTML kod Ÿród³owy strony w formacie XHTML
	 * @return mapa wyodrêbnionych danych
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
		    log4j.info("Wyodrêbni³em informacje ze strony XHTML");
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
	 * G³ówna pêtla w¹tku - konsumenta, jej zadaniem jest pobieranie strony z bufora, oraz 
	 * wœród zawartych na niej danych, wyodrêbnienie danych pasuj¹cych do posiadanych przez
	 * w¹tek wyra¿eñ XPath oraz pózniejsze zesk³adowanie ich, oddelegowuj¹c zadanie do
	 * odpowiedniego obiektu implementuj¹cego interfejs StoreBusInfo. Na pocz¹tku funkcji
	 * nastêpuje zwiêkszenie wartoœci pola statycznego przechowywuj¹cego liczbê pracuj¹cych
	 * w¹tków wy³uskuj¹cych, natomiast na koñcu funkcji nast¹pi zmniejszenie tej iloœci, za
	 * ka¿dym razem nastêpuje zmiana o wartoœæ równ¹ jeden. W przypadku pustego
	 * bufora, maj¹ zostaæ obudzone w¹tki pobieraj¹ce, je¿eli zostanie przekroczony czas
	 * oczekiwania na buforze, zostanie zwrócony obiekt null, dlatego bezpoœrednio po
	 * pobraniu zabezpieczamy siê sprawdzaj¹c czy otrzymany zasób nie jest null'em.
	 * W sytuacji gdy w¹tki pobieraj¹ce jeszczce pracuj¹, ale bufor jest pusty, w¹tek
	 * wy³uskuj¹cy zostaje uœpiony.
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
					log4j.error("W¹tek niepoprawnie wybudzony:"+e.getMessage());
				} finally {
					if(currentPage == null)
					{
						continue;
					}
				}
				log4j.info("Pobra³em z kolejki stron");
				infoSaving.storeInfo(analiseXMLPage(prepareXMLPage(
						currentPage)));
				log4j.info("Strona zosta³a przetworzona");
			}
			else
			{
				log4j.info("Kolejka pusta, usypiamy w oczekiwaniu na producenta.");
				try {
					sleep(1000);
					yield();
				} catch (InterruptedException e) {
					log4j.error("W¹tek niepoprawnie wybudzony:"+e.getMessage());
				}
			}
				
		}while((BuScrapper.numberOfWorkingDownloadThreads.intValue() > 0 
				|| !pagesToAnalise.isEmpty()) );
		 
		BuScrapper.numberOfWorkingSnatchThreads.decrementAndGet();
		log4j.info("SnatchThread o id "+threadId+" koñczy pracê!");
	}
	

	/**
	 * Konstruktor sparametryzowany, który przypisuje nowotworzonemu obiektowi jego numer
	 * identyfikacyjny, bufor stron dla konsumenta, implementacjê interfejsu StoreBusInfo,
	 * do której bêdzie oddelegowywa³ zadanie zapisania informacji wyodrêbionych ze stron,
	 * oraz mapê zapytañ XPath, gdzie kluczemjest nazwa danego zapytania, natomiast
	 * wartoœci¹ samo zapytanie.
	 * @param id unikatowy numer w¹tku wzglêdem innych w¹tków tej klasy, wykonywanych
	 * 		w jednej puli w¹tków.
	 * @param pagesToAnalise implementacja interfejsu PagesBuffer, umo¿liwiaj¹ca pobranie
	 * 		stron w celu wyodrêbnienia z nich informacji.
	 * @param infoSaving implementacja interfejsu StoreBusInfo, odpowiedzialna za zesk³adowanie
	 * 		wyodrêbnionych informacji.
	 * @param xPathExpressions mapa zapytañ XPath, gdzie kluczem jest nazwa danego zapytania,
	 * natomiast wartoœci¹ samo zapytanie. Wa¿ne, aby zarówno obiekt wy³uskuj¹cy jak
	 * i przechowywana przez niego implementacja interfejsu StoreBusInfo zna³a mo¿liwe
	 * nazwy zapytañ i aby by³y one spójne.
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			Map<String,String> xPathExpressions)
	{
		log4j.info("SnatchThread o id "+id+" rozpoczyna pracê!");
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
		this.xPathExpressions = xPathExpressions;
	}
}
