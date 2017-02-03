package pl.edu.agh.kis;

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
 * Klasa w¹tków, których zadaniem jest wyodrêbnienie zawartoœci ze strony, która znajduje
 * siê ju¿ w buforze, metod tej klasy nie interesuje pochodzenie stron, a jedynie ustalony
 * w odpowiednim pliku XPath elementów, które musi wyodrêbniæ, posiada obiekt implementuj¹cy 
 * interfejs StoreBusInfo, do którego oddelegowuje obowi¹zek zesk³adowania otrzymanych 
 * informacji, posiada równie¿ swój w³asny system logów, z wyjœciem do pliku o nazwie równej 
 * id dzia³aj¹cego w¹tku
 * @author Szymon Majkut
 * @version 1.3
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
	 * Zestaw wyra¿eñ XPath, z których bêdziemy korzystaæ
	 * W tej wersji programu, powinniœmy otrzymaæ szeœæ wyra¿eñ, w kolejnoœci: pierwsze
	 * odpowiada za numer linii, druga za nazwê przystanku, trzecia za godzinê, czwarta
	 * za minuty dnia powszedniego, pi¹ta sobotê, a szósta niedzieli
	 */
	private HashMap<String,String> xPathExpressions;
	
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
	 * na których mo¿emy korzystaæ z przechodzenia poprzez XPath, wykorzystuje mo¿liwoœci
	 * pakietu JTidy, s³u¿¹cego do przetwarzania stron HTML, wyniki zapisuje w pliku
	 * o unikatowej nazwie w¹tku, 
	 * @param pageHTML pe³ny kod Ÿród³owy strony, któr¹ bêdziemy przetwarzaæ
	 * @return pe³ny kod Ÿród³owy strony przetworzony do formy XHTML'a
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
			
			snatchLogger.info("Utworzy³em dokument XHTML z dokumentu HTML");
		} catch (UnsupportedEncodingException e) {
			snatchLogger.warning("Nie uda³o siê utworzyæ dokumentu XHTML z HTML");
		}

		return "<myroot>"+result+"</myroot>";
		
	}
	
	/**
	 * Funkcja odpowiedzialna jest za wyodrêbnienie przydatnych informacji 
	 * z pe³nego kodu Ÿród³owego strony przy pomocy przechowywanych XPath, 
	 * oraz zwrócenie wyniku wyodrêbnienia zesk³adowanego w mapie
	 * @param pageXHTML kod Ÿród³owy strony w formacie XHTML
	 * @return mapa wyodrêbnionych danych
	 */
	private Map<String,String> analiseXMLPage(String pageXHTML) {
		
		//Wiemy ¿e kluczami musz¹ byæ direction, lineNumber, buStopName, hours
		
		//Przygotowujê zmienne, do przechowywania wyodrêbnionych informacji
		Map<String,String> results = new HashMap<String,String>();
		String direction = "";
		String lineNumber = "";
		String buStopName = "";
		String hours = "";
		StringBuilder buildHours = new StringBuilder();
		
		try {

		    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document docXML = docBuilder.parse(new ByteArrayInputStream(pageXHTML.getBytes("UTF-8")));
		    
		    XPath xPath =  XPathFactory.newInstance().newXPath();
 		    
		    //Tutaj wyci¹gamy pojedyncze String nazwy linii, przystanku i kierunku
		  
		    buStopName = xPath.compile(xPathExpressions.get("buStopName")).
		    		evaluate(docXML).replaceAll("\\s","");
		   
		    lineNumber = xPath.compile(xPathExpressions.get("lineNumber")).
		    		evaluate(docXML).replaceAll("\\s","");
		    
		    direction = xPath.compile(xPathExpressions.get("direction"))
		    		.evaluate(docXML).replaceAll("\\s","");
		    
		    //Tutaj bêdziemy wyci¹gaæ ca³e wiersze, aby wyci¹gn¹æ z nich nastêpnie odpowiednie czasy
		    NodeList hourss = (NodeList) xPath.compile(xPathExpressions.get("hours")).
		    		evaluate(docXML, XPathConstants.NODESET);
		    		    
		    //Przelatujê po wszystkich linijkach pasuj¹cych do XPath
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
		    
		    snatchLogger.info("Wyodrêbni³em informacje ze strony XHTML");
		} catch (SAXParseException err) {
			snatchLogger.error("Problem przy analizowaniu dokumentu XHTML");
			err.printStackTrace();
		} catch (SAXException e) {
			snatchLogger.error("Problem przy analizowaniu dokumentu XHTML");
			e.printStackTrace();
		} catch (Throwable t) {
			snatchLogger.error("Bardzo powa¿ny i nieznany problem z analizowaniem XHTML");
			t.printStackTrace();
		}
		
		results.put("lineNumber", lineNumber);
		results.put("direction", direction);
		results.put("buStopName", buStopName);
		results.put("hours", hours);
		
		return results;
	}
	
	/**
	 * G³ówna pêtla w¹tku - konsumenta, jej zadaniem jest pobieranie strony z bufora, oraz 
	 * wœród zawartych na niej danych, wyodrêbnienie danych pasuj¹cych do posiadanych przez
	 * w¹tek wyra¿eñ XPath oraz pózniejsze zesk³adowanie ich, oddelegowuj¹c zadanie do
	 * odpowiedniego obiektu implementuj¹cego interfejs StoreBusInfo, w przypadku pustego
	 * Bufora, maj¹ zostaæ obudzeni producenci
	 */
	public void run()
	{	
		try {
			do
			{
				infoSaving.storeInfo(analiseXMLPage(prepareXMLPage(pagesToAnalise.takePage())));
				snatchLogger.info("Pobieram z kolejki stron");
				
				snatchLogger.execute();
			}while(BuScrapper.numberOfWorkingThreads.intValue() > 0);
		} catch (InterruptedException e) {
			snatchLogger.info("Niepoprawnie wybudzony!",e.getMessage());
			snatchLogger.execute();
		}
		snatchLogger.info("SnatchThread o imieniu "+threadName+" koñczy pracê!");
		snatchLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów, mia³ te¿ dostêp do bufora z kolejnymi pobranymi stronami, oraz podarowany przez
	 * w¹tek nadrzêdny obiekt, któremu bêdzie delegowa³ sk³adowanie uzyskanych danych, dodatkowo
	 * posiada mo¿liwoœæ ustalenia w³asnego systemu logów
	 * @param id unikalny numer w¹tku
	 * @param pagesToAnalise bufor ze stronami do przetworzenia
	 * @param infoSaving obiekt odpowiedzialny za zes³adowanie danych
	 * @param xPathExpressions mapa wyra¿eñ Regex do przetworzenia
	 * @param appender obiekt odpowiedzialny za sk³adowanie logów
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			HashMap<String,String> xPathExpressions,Appends appender)
	{
		threadName = "SnatchThread number " + id;
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
		this.xPathExpressions = xPathExpressions;
		snatchLogger = new Logger();
		snatchLogger.changeAppender(appender);
		snatchLogger.info("SnatchThread o imieniu "+threadName+" rozpoczyna pracê!");
		StringBuilder regexs = new StringBuilder();
		for(String s : xPathExpressions.keySet())
		{
			regexs.append(s);
		}
		snatchLogger.info("Otrzyma³em œcie¿ki XPath:",regexs.toString());
		snatchLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego znaczenie polega na tym, aby ka¿dy nowo utworzony
	 * w¹tek przetwarzaj¹cy, posiada³ unikatow¹ nazwê, któr¹ bêdziemy wykorzystywaæ w systemie
	 * logów, mia³ te¿ dostêp do bufora z kolejnymi pobranymi stronami, oraz podarowany przez
	 * w¹tek nadrzêdny obiekt, któremu bêdzie delegowa³ sk³adowanie uzyskanych danych, ustala
	 * domyœln¹ nazwê dla pliku do systemu logów
	 * @param id unikalny numer w¹tku
	 * @param pagesToAnalise bufor ze stronami do przetworzenia
	 * @param infoSaving obiekt odpowiedzialny za zes³adowanie danych
	 * @param xPathExpressions mapa wyra¿eñ Regex do przetworzenia
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			HashMap<String,String> xPathExpressions)
	{
		this(id,pagesToAnalise,infoSaving,xPathExpressions,
				new FileAppender("SnatchThread number " + id));
	}
}
