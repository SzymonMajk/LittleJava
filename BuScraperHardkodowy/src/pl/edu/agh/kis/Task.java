package pl.edu.agh.kis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Klasa przechowuj¹ca pojedyncze zadanie do wykonania dla BuScrappera
 * mo¿e zwracaæ szczegó³y zadania, a jego stan jest ustalany w konstruktorze.
 * Jeden Task przeprowadza zapytania dla jednego hosta.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class Task {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(TaskManager.class.getName());

	private int id;
	
	
	/**
	 * Kolejka zadañ do powtórzenia
	 */
	private BlockingQueue<Request> requestsToRepeat;
	
	public void setRequestsToRepeat(BlockingQueue<Request> requestsToRepeat)
	{
		this.requestsToRepeat = requestsToRepeat;
	}
	
	public BlockingQueue<Request> pollRequestsToRepeat()
	{
		BlockingQueue<Request> result = requestsToRepeat;
		requestsToRepeat = new ArrayBlockingQueue<Request>(1);
		return result;
	}
	
	/**
	 * Funkcja zwraca indywidualny numer zadania jednoznacznie je okreœlaj¹cy.
	 * @return indywidualny numer zadania jednoznacznie je okreœlaj¹cy
	 */
	public int getId()
	{
		return id;
	}
	
	private int status;
	
	/**
	 * Funkcja pozwala na zmianê statusu zadania wraz z okreœleniem poprawnoœci argumentu.
	 * @param i nowy status zadania
	 */
	public void setStatus(int i)
	{
		if(i >= 0 && i <= 2)
		{
			status = i;
		}
	}
	
	/**
	 * Funkcja zwraca status zadania.
	 * @return status zadania
	 */
	public int getStatus()
	{
		return status;
	}
	
	/**
	 * Przechowuje numer linii obs³ugiwanej przez zadanie
	 */
	private String lineNumber;
	
	/**
	 * Zwraca numer linii obs³ugiwanej przez zadanie.
	 * @return numer linii obs³ugiwanej przez zadanie
	 */
	public String getLineNumber()
	{
		return lineNumber;
	}
	
	/**
	 * Przechowuje maksymaln¹ liczbê przystanków
	 */
	private String maxBuStop;
	
	/**
	 * Zwraca maksymaln¹ liczbê przystanków.
	 * @return maksymalna liczba przystanków
	 */
	public String getMaxBuStop()
	{
		return maxBuStop;
	}
	
	/**
	 * Przechowuje maksymaln¹ liczbê kierunków
	 */
	private String maxDirection = "1";
	
	/**
	 * Zwraca maksymaln¹ liczbê kierunków.
	 * @return maksymalna liczba kierunków
	 */
	public String getMaxDirection()
	{
		return maxDirection;
	}
	
	/**
	 * Przechowuje nazwê metody dla zapytañ
	 */
	private String method = "GET";
	
	/**
	 * Zwraca nazwê metody dla zapytañ.
	 * @return nazwa metody dla zapytañ
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Przechowuje nazwê hosta dla zapytañ
	 */
	private String urlPath = "";
	
	/**
	 * Zwraca nazwê hosta zapytañ.
	 * @return nazwa hosta zapytañ
	 */
	public String getUrlPath()
	{
		return urlPath;
	}
	
	/**
	 * Przechowuje nazwê hosta dla zapytañ
	 */
	private String host = "";
	
	/**
	 * Zwraca nazwê hosta zapytañ.
	 * @return nazwa hosta zapytañ
	 */
	public String getHost()
	{
		return host;
	}
	
	Task()
	{
		this.id = 0;
		this.lineNumber = "";
		this.maxBuStop = "";
		this.maxDirection = "";
		this.method = "";
		this.host = "";
		this.urlPath = "";
		status = 0;
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest poprawne przypisanie wszystkich
	 * danych potrzebnych do ustalenia stanu pocz¹tkowego zadania, otrzymanych w argumentach.
	 * @param lineNumber numer linii dla zapytañ
	 * @param maxBuStop maksymalna liczba przystanków
	 * @param maxDirection maksymalna liczba kierunków
	 * @param method metoda dla zapytañ
	 * @param host nazwa hosta dla zapytañ
	 */
	Task(int id,String lineNumber, String maxBuStop, String maxDirection, String method, 
			String pageUrl)
	{
		this.id = id;
		this.lineNumber = lineNumber;
		this.maxBuStop = maxBuStop;
		this.maxDirection = maxDirection;
		this.method = method;
		
		URL url;
		try {
			url = new URL(pageUrl);
			this.host = url.getHost();
			this.urlPath = url.getPath();
		} catch (MalformedURLException e) {
			log4j.error("Problem z po³¹czeniem z URL"+e.getMessage());
		}
		
		status = 0;
	}
}