package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Klasa w�tk�w, kt�rych zadaniem jest wyodr�bnienie zawarto�ci ze strony, kt�ra znajduje
 * si� ju� w buforze, metod tej klasy nie interesuje pochodzenie stron, a jedynie to za
 * pomoc� jakich wyra�e� XPath ma dobra� si� do element�w, kt�re musi wyodr�bni�, posiada 
 * obiekt implementuj�cy interfejs StoreBusInfo, do kt�rego oddelegowuje obowi�zek 
 * zesk�adowania wyodr�bnionych informacji, posiada r�wnie� sw�j w�asny system log�w, 
 * z wyj�ciem do pliku o nazwie r�wnej id dzia�aj�cego w�tku.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class SnatchThread extends Thread {
	
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
	private HashMap<String,String> xPathExpressions;
	
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
	 * na kt�rych mo�emy korzysta� z przechodzenia poprzez XPath, wykorzystuje mo�liwo�ci
	 * pakietu JTidy, s�u��cego do przetwarzania stron HTML, wyniki zapisuje w pliku
	 * o unikatowej nazwie w�tku, 
	 * @param pageHTML pe�ny kod �r�d�owy strony, kt�r� b�dziemy przetwarza�
	 * @return pe�ny kod �r�d�owy strony przetworzony do formy XHTML'a
	 */
	private String prepareXMLPage(String pageHTML)
	{		
		String result = "";
				
		Tidy tidy = new Tidy();
		//tidy.setXHTML(true);
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
	    
		ByteArrayInputStream inputStream;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			inputStream = new ByteArrayInputStream(pageHTML.getBytes("UTF-8"));
		    tidy.parseDOM(inputStream, outputStream);
			result = outputStream.toString("UTF-8");
			
			log4j.info("Utworzy�em dokument XHTML z dokumentu HTML");
		} catch (UnsupportedEncodingException e) {
			log4j.error("B��d kodowania:"+
					e.getMessage());
		}

		return "<myroot>"+result+"</myroot>";
		
	}
	
	/**
	 * Funkcja odpowiedzialna jest za wyodr�bnienie przydatnych informacji 
	 * z pe�nego kodu �r�d�owego strony przy pomocy przechowywanych XPath, 
	 * oraz zwr�cenie wyniku wyodr�bnienia zesk�adowanego w mapie
	 * @param pageXHTML kod �r�d�owy strony w formacie XHTML
	 * @return mapa wyodr�bnionych danych
	 */
	private Map<String,String> analiseXMLPage(String pageXHTML) {
		
		//Wiemy �e kluczami musz� by� direction, lineNumber, buStopName, hours
		
		//Przygotowuj� zmienne, do przechowywania wyodr�bnionych informacji
		Map<String,String> results = new HashMap<String,String>();
		String direction = "";
		String lineNumber = "";
		String buStopName = "";
		String hours = "";
		StringBuilder buildHours = new StringBuilder();
		
		try {

		    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document docXML = docBuilder.
		    		parse(new ByteArrayInputStream(pageXHTML.getBytes("UTF-8")));
		    
		    XPath xPath =  XPathFactory.newInstance().newXPath();
 		    
		    //Tutaj wyci�gamy pojedyncze String nazwy linii, przystanku i kierunku
		  
		    buStopName = xPath.compile(xPathExpressions.get("buStopName")).
		    		evaluate(docXML).replaceAll("\\s","");
		   
		    lineNumber = xPath.compile(xPathExpressions.get("lineNumber")).
		    		evaluate(docXML).replaceAll("\\s","");
		    
		    direction = xPath.compile(xPathExpressions.get("direction"))
		    		.evaluate(docXML).replaceAll("\\s","");
		    
		    //Tutaj b�dziemy wyci�ga� ca�e wiersze, aby wyci�gn�� z nich nast�pnie odpowiednie czasy
		    NodeList hourss = (NodeList) xPath.compile(xPathExpressions.get("hours")).
		    		evaluate(docXML, XPathConstants.NODESET);
		    		    
		    //Przelatuj� po wszystkich linijkach pasuj�cych do XPath
		    for (int i = 0;null!=hourss && i < hourss.getLength(); ++i) 
		    {
		    	
	    		Node nod = hourss.item(i);
	    		if(nod.getNodeType() == Node.ELEMENT_NODE)
	    		{
	    			String tmpTimes = "";
	    			if(!(tmpTimes = nod.getTextContent()).equals(""))
	    			{
	    				buildHours.append(tmpTimes);
	    			}
	    		}
	    	}
		    
		    hours = buildHours.toString();
		    
		    log4j.info("Wyodr�bni�em informacje ze strony XHTML");
		} catch (SAXParseException err) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+err.getMessage());
		} catch (SAXException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());
		} catch (Throwable t) {
			log4j.
			error("Bardzo powa�ny i do obczajenia problem z analizowaniem XHTML:"+
					t.getMessage());
		}
		
		results.put("lineNumber", lineNumber);
		results.put("direction", direction);
		results.put("buStopName", buStopName);
		results.put("hours", hours);
		
		return results;
	}
	//TODO no tutaj pewnie b�dzie wymaga�o zmian... �eby nie zawiesza�o tak tych w�tk�w!
	/**
	 * G��wna p�tla w�tku - konsumenta, jej zadaniem jest pobieranie strony z bufora, oraz 
	 * w�r�d zawartych na niej danych, wyodr�bnienie danych pasuj�cych do posiadanych przez
	 * w�tek wyra�e� XPath oraz p�zniejsze zesk�adowanie ich, oddelegowuj�c zadanie do
	 * odpowiedniego obiektu implementuj�cego interfejs StoreBusInfo, w przypadku pustego
	 * Bufora, maj� zosta� obudzone w�tki pobieraj�ce.
	 */
	public void run()
	{	
		
		try {
			do
			{
				if(!pagesToAnalise.isEmpty())
				{
					infoSaving.storeInfo(analiseXMLPage(prepareXMLPage(
							pagesToAnalise.takePage())));
					log4j.info("Pobieram z kolejki stron");
				}
				else
				{
					log4j.info("Kolejka pusta, oddaj� sw�j czas");
					//TODO zastna�w si� nad odpowiednim wyj�ciem
					yield();
					log4j.info("Zasypiam, bo nie mam co robi�...");
					sleep(1000);
				}
				
			}while(BuScrapper.numberOfWorkingDownloadThreads.intValue() > 0);
		} catch (InterruptedException e) {
			log4j.error("Niepoprawnie wybudzony!"+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Niepoprawne zapisanie!"+t.getMessage());
		}
		log4j.info("SnatchThread o id "+threadId+" ko�czy prac�!");
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo
	 * utworzony w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa�
	 * w systemie log�w, mia� te� dost�p do bufora z kolejnymi pobranymi stronami, oraz 
	 * podarowany przez w�tek nadrz�dny obiekt, kt�remu b�dzie delegowa� sk�adowanie 
	 * uzyskanych danych.
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku
	 *        nadrz�dnym
	 * @param pagesToAnalise bufor stron, zapewniaj�cy blokowanie udost�pnianych przez 
	 *        siebie metod
	 * @param infoSaving obiekt odpowiedzialny za zes�adowanie danych
	 * @param xPathExpressions mapa wyra�e� XPath wykorzystywanych przy wyodr�bnianiu
	 *        danych
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			HashMap<String,String> xPathExpressions)
	{
		threadId = id;
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
		this.xPathExpressions = xPathExpressions;
		log4j.info("SnatchThread o id "+threadId+" rozpoczyna prac�!");
		StringBuilder regexs = new StringBuilder();
		for(String s : xPathExpressions.keySet())
		{
			regexs.append(s);
		}
		log4j.info("Otrzyma�em �cie�ki XPath:"+regexs.toString());
	}
	//TODO, niech w konstrutkorze dostaj� tylko buffer. id i storeBusInfo i XPAth, no i tutaj
	//chyba nie trzeba dodatkowych funkcji... zastan�w si�!
}
