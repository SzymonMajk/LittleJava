package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

/**
 * Klasa w�tk�w, kt�rych zadaniem jest wyodr�bnienie zawarto�ci ze strony, kt�ra znajduje
 * si� ju� w buforze, metod tej klasy nie interesuje pochodzenie stron, a jedynie to za
 * pomoc� jakich wyra�e� XPath ma dobra� si� do element�w, kt�re musi wyodr�bni�, posiada 
 * obiekt implementuj�cy interfejs StoreBusInfo, do kt�rego oddelegowuje obowi�zek 
 * zesk�adowania wyodr�bnionych informacji, posiada r�wnie� sw�j w�asny system log�w, 
 * z wyj�ciem do pliku o nazwie r�wnej id dzia�aj�cego w�tku.
 * @author Szymon Majkut
 * @version %I%, %G%
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
		} catch (SAXException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());
		} catch (ParserConfigurationException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());			e.printStackTrace();
		} catch (XPathExpressionException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());			e.printStackTrace();
		} catch (IOException e) {
			log4j.error("Problem przy analizowaniu dokumentu XHTML:"+e.getMessage());			e.printStackTrace(); 
		}
		
		results.put("lineNumber", lineNumber);
		results.put("direction", direction);
		results.put("buStopName", buStopName);
		results.put("hours", hours);
		
		return results;
	}
	/**
	 * G��wna p�tla w�tku - konsumenta, jej zadaniem jest pobieranie strony z bufora, oraz 
	 * w�r�d zawartych na niej danych, wyodr�bnienie danych pasuj�cych do posiadanych przez
	 * w�tek wyra�e� XPath oraz p�zniejsze zesk�adowanie ich, oddelegowuj�c zadanie do
	 * odpowiedniego obiektu implementuj�cego interfejs StoreBusInfo, w przypadku pustego
	 * Bufora, maj� zosta� obudzone w�tki pobieraj�ce.
	 */
	public void run()
	{	
		BuScrapper.numberOfWorkingSnatchThreads.incrementAndGet();

		do
		{
			if(!pagesToAnalise.isEmpty())
			{
				try {
					currentPage = pagesToAnalise.takePage();
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
					sleep(5000);
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
}
