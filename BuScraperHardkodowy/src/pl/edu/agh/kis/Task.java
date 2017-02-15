package pl.edu.agh.kis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Klasa przechowuj�ca pojedyncze zadanie do wykonania dla BuScrappera
 * mo�e zwraca� szczeg�y zadania, a jego stan jest ustalany w konstruktorze.
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

	/**
	 * 
	 */
	private int id;
	
	/**
	 * Kolejka poprawnych zapyta�
	 */
	private BlockingQueue<Request> requestsToDo;
	
	/**
	 * Kolejka zada� do powt�rzenia
	 */
	private BlockingQueue<Request> requestsToRepeat;
	
	/**
	 * 
	 * @return
	 */
	public BlockingQueue<Request> getRequestsToDo()
	{
		return requestsToDo;
	}
	
	/**
	 * 
	 * @return
	 */
	public BlockingQueue<Request> getRequestsToRepeat()
	{
		return requestsToRepeat;
	}
	
	/**
	 * Funkcja zwraca indywidualny numer zadania jednoznacznie je okre�laj�cy.
	 * @return indywidualny numer zadania jednoznacznie je okre�laj�cy
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Przechowuje numer linii obs�ugiwanej przez zadanie
	 */
	private String lineNumber;
	
	/**
	 * Zwraca numer linii obs�ugiwanej przez zadanie.
	 * @return numer linii obs�ugiwanej przez zadanie
	 */
	public String getLineNumber()
	{
		return lineNumber;
	}
	
	/**
	 * Przechowuje maksymaln� liczb� przystank�w
	 */
	private String maxBuStop;
	
	/**
	 * Zwraca maksymaln� liczb� przystank�w.
	 * @return maksymalna liczba przystank�w
	 */
	public String getMaxBuStop()
	{
		return maxBuStop;
	}
	
	/**
	 * Przechowuje maksymaln� liczb� kierunk�w
	 */
	private String maxDirection = "1";
	
	/**
	 * Zwraca maksymaln� liczb� kierunk�w.
	 * @return maksymalna liczba kierunk�w
	 */
	public String getMaxDirection()
	{
		return maxDirection;
	}
	
	/**
	 * Przechowuje nazw� metody dla zapyta�
	 */
	private String method = "GET";
	
	/**
	 * Zwraca nazw� metody dla zapyta�.
	 * @return nazwa metody dla zapyta�
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Przechowuje nazw� hosta dla zapyta�
	 */
	private String urlPath = "";
	
	/**
	 * Zwraca nazw� hosta zapyta�.
	 * @return nazwa hosta zapyta�
	 */
	public String getUrlPath()
	{
		return urlPath;
	}
	
	/**
	 * Przechowuje nazw� hosta dla zapyta�
	 */
	private String host = "";
	
	/**
	 * Zwraca nazw� hosta zapyta�.
	 * @return nazwa hosta zapyta�
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * Funkcja s�u�y do utworzenia kolejki request�w zgodnie z wytycznymi z p�l prywatnych
	 * danego zadania
	 * @param newTask nowe zadanie, na podstawie kt�rego tworzymy kolejk� zapyta�
	 */
	private void prepareNewRequests()
	{
		String lineNumber = getLineNumber();
		int maxBuStop = 1;
		int maxDirection = 1;
		
		try {
			maxBuStop = Integer.parseInt(getMaxBuStop());
			maxDirection = Integer.parseInt(getMaxDirection());
		} catch (NumberFormatException e) {
			log4j.warn("Z�y format liczby: "+e.getMessage());
			return;
		}
			
		requestsToDo = new ArrayBlockingQueue<Request>(maxBuStop*maxDirection);
		requestsToRepeat = new ArrayBlockingQueue<Request>(maxBuStop*maxDirection);
			
		for(int i = 0; i < maxDirection; ++i)
		{
			for(int j = 0; j < maxBuStop; ++j)
			{
				StringBuilder builder = new StringBuilder();
				builder.append("lang=PL&rozklad=20170120&linia=");
				builder.append(lineNumber);
				builder.append("__");
				builder.append(i);
				builder.append("__");
				builder.append(j);
				requestsToDo.add(new Request(getMethod(), getUrlPath(),
						getHost(),builder.toString(),"utf-8"));
				log4j.info("Doda�em nowe zapytanie o parametrach:"+builder.toString());	
			}
		}
	}
	
	/**
	 * Funkcja ma za zadanie poprawnie ustali� stan kolejek z zapytaniami do wykonania
	 * oraz zapytaniami do powt�rzenia, przed rozpocz�ciem korzystania z tych kolejek
	 * przez w�tki pobieraj�ce. Sprawdza czy kolejka zapyta� do powt�rzenia jest pusta
	 * je�eli tak, oznacza to, �e Task nie by� jeszcze wykonywany i nale�y dla niego
	 * przygotowa� nowy zestaw zapyta� wed�ug wytycznych zdobytych w konstruktorze,
	 * natomiast w przeciwnym wypadku przek�ada kolejk� do powt�rzenia w miejsce kolejki
	 * zapyta� do wykonania, oraz tworzy now� kolejk� do powt�rzenia, o rozmiarze r�wnym
	 * rozmiarowi kolejce do zrobienia, dzi�ki czemu zabezpieczamy si� przed mo�liwo�ci�,
	 * �e wszystkie zapytania z kolejki do wykonania b�d� potrzebowa�y powt�rzenia.
	 */
	public void prepareRequestsBeforeUsing()
	{
		if(requestsToRepeat.isEmpty())
		{
			prepareNewRequests();
		}
		else
		{
			requestsToDo = requestsToRepeat;
			requestsToRepeat = new ArrayBlockingQueue<Request>(requestsToDo.size());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEmptyRequestsToRepeat()
	{
		if(requestsToRepeat.isEmpty())
		{
			return true;
		}
		
		for(Request r : requestsToDo)
		{
			try {
				requestsToRepeat.add(r);
			} catch (IllegalStateException e) {
				log4j.error("Nieudane umieszczenie zapytania w kolejsce do powt�rzenia"
						+e.getMessage());
			}
			
		}

		requestsToDo.clear();
		return false;
	}
	
	/**
	 * 
	 */
	Task()
	{
		this.id = 0;
		this.lineNumber = "";
		this.maxBuStop = "";
		this.maxDirection = "";
		this.method = "";
		this.host = "";
		this.urlPath = "";
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest poprawne przypisanie wszystkich
	 * danych potrzebnych do ustalenia stanu pocz�tkowego zadania, otrzymanych w argumentach.
	 * @param lineNumber numer linii dla zapyta�
	 * @param maxBuStop maksymalna liczba przystank�w
	 * @param maxDirection maksymalna liczba kierunk�w
	 * @param method metoda dla zapyta�
	 * @param host nazwa hosta dla zapyta�
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
			log4j.error("Niew�a�ciwy format url"+e.getMessage());
		}
		
		requestsToDo = new ArrayBlockingQueue<Request>(1);
		requestsToRepeat = new ArrayBlockingQueue<Request>(1);
	}
}