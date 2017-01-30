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
 * Klasa w�tk�w, kt�rych zadaniem jest wyodr�bnienie zawarto�ci ze strony, kt�ra znajduje
 * si� ju� w buforze, metod tej klasy nie interesuje pochodzenie stron, a jedynie ustalony
 * w odpowiednim pliku XPath element�w, kt�re musi wyodr�bni�, posiada obiekt implementuj�cy 
 * interfejs StoreBusInfo, do kt�rego oddelegowuje obowi�zek zesk�adowania otrzymanych 
 * informacji, posiada r�wnie� sw�j w�asny system log�w, z wyj�ciem do pliku o nazwie r�wnej 
 * id dzia�aj�cego w�tku
 * @author Szymon Majkut
 * @version 1.1b
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
	 * Zestaw wyra�e� XPath, z kt�rych b�dziemy korzysta�
	 * W tej wersji programu, powinni�my otrzyma� sze�� wyra�e�, w kolejno�ci: pierwsze
	 * odpowiada za numer linii, druga za nazw� przystanku, trzecia za godzin�, czwarta
	 * za minuty dnia powszedniego, pi�ta sobot�, a sz�sta niedzieli
	 */
	private String[] xPathExpressions;
	
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
	    //tidy.setErrout(null);
	    tidy.setShowErrors(0);
	    tidy.setForceOutput(true);
	    
		ByteArrayInputStream inputStream;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			inputStream = new ByteArrayInputStream(pageHTML.getBytes("UTF-8"));
		    tidy.parseDOM(inputStream, outputStream);
			result = outputStream.toString("UTF-8");
			
			snatchLogger.info("Utworzy�em dokument XHTML z dokumentu HTML");
		} catch (UnsupportedEncodingException e) {
			snatchLogger.warning("Nie uda�o si� utworzy� dokumentu XHTML z HTML");
		}

		return "<myroot>"+result+"</myroot>";
		
	}
	
	/**
	 * Funkcja odpowiedzialna za zaimplementowanie interfejsu AnalizePage, jej zadaniem
	 * jest wyodr�bnienie przydatnych informacji z pe�nego kodu �r�d�owego strony przy
	 * pomocy odpowiednich XPath, oraz zwr�cenie ich zesk�adowanych w mapie
	 * @param pageXHTML pe�ny kod �r�d�owy strony, z kt�rego b�dziemy wyodr�bnia�
	 * @return zesk�adowane informacje wyodr�bnione ze strony podanej w parametrze
	 */
	private Map<String,String> analiseXMLPage(String pageXHTML) {
		
		/*Wiemy �e kluczami musz� by� direction, lineNumber, buStopName, hours,
		 * p�zniej bedziemy to dostawa� razem z XPathami, wi�c b�dzie jeszcze �atwiej*/
		
		//Przygotowuj� zmienne, do przechowywania wyodr�bnionych informacji
		Map<String,String> results = new HashMap<String,String>();
		String direction = "";
		String lineNumber = "";
		String buStopName = "";
		String hours = "";
		
		try {

		    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document docXML = docBuilder.parse(new ByteArrayInputStream(pageXHTML.getBytes("UTF-8")));
		    
		    XPath xPath =  XPathFactory.newInstance().newXPath();
 		    
		    //Tutaj wyci�gamy pojedyncze String nazwy linii, przystanku i kierunku
		  
		    buStopName = xPath.compile(xPathExpressions[0]).evaluate(docXML).replaceAll("\\s","");
		   
		    lineNumber = xPath.compile(xPathExpressions[1]).evaluate(docXML).replaceAll("\\s","");
		    
		    direction = xPath.compile(xPathExpressions[3])
		    		.evaluate(docXML).replaceAll("\\s","");
		    
		    //Tutaj b�dziemy wyci�ga� ca�e wiersze, aby wyci�gn�� z nich nast�pnie odpowiednie czasy
		    NodeList hourss = (NodeList) xPath.compile(xPathExpressions[2]).evaluate(docXML, XPathConstants.NODESET);
		    		    
		    //Przelatuj� po wszystkich linijkach pasuj�cych do XPath
		    for (int i = 0;null!=hourss && i < hourss.getLength(); ++i) 
		    {
		    	
	    		Node nod = hourss.item(i);
	    		if(nod.getNodeType() == Node.ELEMENT_NODE)
	    		{
	    			String tmpTimes = "";
	    			if(!(tmpTimes = nod.getTextContent()).equals(""))
	    			{
		    			hours += tmpTimes;
	    			}
	    		}
	    	}
		    
		    snatchLogger.info("Wyodr�bni�em informacje ze strony XHTML");
		} catch (SAXParseException err) {
			snatchLogger.error("Problem przy analizowaniu dokumentu XHTML");
			err.printStackTrace();
		} catch (SAXException e) {
			snatchLogger.error("Problem przy analizowaniu dokumentu XHTML");
			e.printStackTrace();
		} catch (Throwable t) {
			snatchLogger.error("Bardzo powa�ny i nieznany problem z analizowaniem XHTML");
			t.printStackTrace();
		}
		
		results.put("lineNumber", lineNumber);
		results.put("direction", direction);
		results.put("buStopName", buStopName);
		results.put("hours", hours);
		
		return results;
	}
	
	/**
	 * G��wna p�tla w�tku - konsumenta, jej zadaniem jest oddawanie czasu procesora w przypadku
	 * pustego bufora, w przeciwnym wypadku pobieranie z niego strony, oraz przetworzenie zawartych
	 * na stronie informacji, oraz p�zniejsze zesk�adowanie ich, oddelegowuj�c zadanie do
	 * odpowiedniego obiektu implementuj�cego interfejs StoreBusInfo
	 */
	public void run()
	{		
		
		do
		{
			BuScrapper.numberOfWorkingThreads.incrementAndGet();
			snatchLogger.info("Pobieram z kolejki stron");
			
			infoSaving.storeInfo(analiseXMLPage(prepareXMLPage(pagesToAnalise.takePage())));
			
			snatchLogger.execute();
			BuScrapper.numberOfWorkingThreads.decrementAndGet();
			
		}while(BuScrapper.numberOfWorkingThreads.intValue() > 0 || !pagesToAnalise.isEmpty());
		
		snatchLogger.info("SnatchThread o imieniu "+threadName+" ko�czy prac�!");
		snatchLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego znaczenie polega na tym, aby ka�dy nowo utworzony
	 * w�tek przetwarzaj�cy, posiada� unikatow� nazw�, kt�r� b�dziemy wykorzystywa� w systemie
	 * log�w, mia� te� dost�p do bufora z kolejnymi pobranymi stronami, oraz podarowany przez
	 * w�tek nadrz�dny obiekt, kt�remu b�dzie delegowa� sk�adowanie uzyskanych danych, dodatkowo
	 * posiada mo�liwo�� ustalenia w�asnego systemu log�w
	 * @param id unikatowy numer, przyznawany jeszcze w czasie tworzenia w�tk�w w w�tku nadrz�dnym
	 * @param pagesToAnalise referencja do synchronizowanego bufora z pobranymi stronami
	 * @param infoSaving referencja do obiektu, kt�remu nale�y delegowa� sk�adowanie informacji
	 * @param appender obiekt odpowiedzialny za wysy�anie log�w
	 */
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			String[] xPathExpressions,Appends appender)
	{
		threadName = "SnatchThread number " + id;
		this.pagesToAnalise = pagesToAnalise;
		this.infoSaving = infoSaving;
		this.xPathExpressions = xPathExpressions;
		snatchLogger = new Logger();
		snatchLogger.changeAppender(appender);
		snatchLogger.info("SnatchThread o imieniu "+threadName+" rozpoczyna prac�!");
		String regexs = "";
		for(String s : xPathExpressions)
		{
			regexs += s;
		}
		snatchLogger.info("Otrzyma�em �cie�ki XPath:",regexs);
		snatchLogger.execute();
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
	SnatchThread(int id, PagesBuffer pagesToAnalise, StoreBusInfo infoSaving,
			String[] xPathExpressions)
	{
		this(id,pagesToAnalise,infoSaving,xPathExpressions,
				new FileAppender("SnatchThread number " + id));
	}
}
