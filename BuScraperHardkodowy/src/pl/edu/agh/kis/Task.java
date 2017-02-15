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

	/**
	 * 
	 */
	private int id;
	
	/**
	 * Kolejka poprawnych zapytañ
	 */
	private BlockingQueue<Request> requestsToDo;
	
	/**
	 * Kolejka zadañ do powtórzenia
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
	 * Funkcja zwraca indywidualny numer zadania jednoznacznie je okreœlaj¹cy.
	 * @return indywidualny numer zadania jednoznacznie je okreœlaj¹cy
	 */
	public int getId()
	{
		return id;
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
	
	/**
	 * Funkcja s³u¿y do utworzenia kolejki requestów zgodnie z wytycznymi z pól prywatnych
	 * danego zadania
	 * @param newTask nowe zadanie, na podstawie którego tworzymy kolejkê zapytañ
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
			log4j.warn("Z³y format liczby: "+e.getMessage());
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
				log4j.info("Doda³em nowe zapytanie o parametrach:"+builder.toString());	
			}
		}
	}
	
	/**
	 * Funkcja ma za zadanie poprawnie ustaliæ stan kolejek z zapytaniami do wykonania
	 * oraz zapytaniami do powtórzenia, przed rozpoczêciem korzystania z tych kolejek
	 * przez w¹tki pobieraj¹ce. Sprawdza czy kolejka zapytañ do powtórzenia jest pusta
	 * je¿eli tak, oznacza to, ¿e Task nie by³ jeszcze wykonywany i nale¿y dla niego
	 * przygotowaæ nowy zestaw zapytañ wed³ug wytycznych zdobytych w konstruktorze,
	 * natomiast w przeciwnym wypadku przek³ada kolejkê do powtórzenia w miejsce kolejki
	 * zapytañ do wykonania, oraz tworzy now¹ kolejkê do powtórzenia, o rozmiarze równym
	 * rozmiarowi kolejce do zrobienia, dziêki czemu zabezpieczamy siê przed mo¿liwoœci¹,
	 * ¿e wszystkie zapytania z kolejki do wykonania bêd¹ potrzebowa³y powtórzenia.
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
				log4j.error("Nieudane umieszczenie zapytania w kolejsce do powtórzenia"
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
			log4j.error("Niew³aœciwy format url"+e.getMessage());
		}
		
		requestsToDo = new ArrayBlockingQueue<Request>(1);
		requestsToRepeat = new ArrayBlockingQueue<Request>(1);
	}
}