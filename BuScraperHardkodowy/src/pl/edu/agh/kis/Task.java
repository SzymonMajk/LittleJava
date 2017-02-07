package pl.edu.agh.kis;

/**
 * Klasa przechowuj�ca pojedyncze zadanie do wykonania dla BuScrappera
 * mo�e zwraca� szczeg�y zadania, a jego stan jest ustalany w konstruktorze.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class Task {

	private int id;
	
	/**
	 * Funkcja zwraca indywidualny numer zadania jednoznacznie je okre�laj�cy.
	 * @return indywidualny numer zadania jednoznacznie je okre�laj�cy
	 */
	public int getId()
	{
		return id;
	}
	
	private int status;
	
	/**
	 * Funkcja pozwala na zmian� statusu zadania wraz z okre�leniem poprawno�ci argumentu.
	 * @param i nowy status zadania
	 */
	public void changeStatus(int i)
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
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest poprawne przypisanie wszystkich
	 * danych potrzebnych do ustalenia stanu pocz�tkowego zadania, otrzymanych w argumentach.
	 * @param lineNumber numer linii dla zapyta�
	 * @param maxBuStop maksymalna liczba przystank�w
	 * @param maxDirection maksymalna liczba kierunk�w
	 * @param method metoda dla zapyta�
	 * @param host nazwa hosta dla zapyta�
	 */
	Task(int id,String lineNumber, String maxBuStop, String maxDirection, String method, String host)
	{
		this.id = id;
		this.lineNumber = lineNumber;
		this.maxBuStop = maxBuStop;
		this.maxDirection = maxDirection;
		this.method = method;
		this.host = host;
		status = 0;
	}
}