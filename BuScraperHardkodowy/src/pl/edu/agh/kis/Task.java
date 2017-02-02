package pl.edu.agh.kis;

/**
 * Klasa przechowuj¹ca pojedyncze zadanie do wykonania dla BuScrappera
 * mo¿e zwracaæ szczegó³y zadania, a jego stan jest ustalany w konstruktorze
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class Task {

	/**
	 * Przechowuje numer linii obs³ugiwanej przez zadanie
	 */
	private String lineNumber;
	
	/**
	 * Zwraca numer linii obs³ugiwanej przez zadanie
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
	 * Zwraca maksymaln¹ liczbê przystanków
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
	 * Zwraca maksymaln¹ liczbê kierunków
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
	 * Zwraca nazwê metody dla zapytañ
	 * @return nazwa metody dla zapytañ
	 */
	public String getmethod()
	{
		return method;
	}
	
	/**
	 * Przechowuje nazwê hosta dla zapytañ
	 */
	private String host = "";
	
	/**
	 * Zwraca nazwê hosta dla zapytañ
	 * @return nazwa hosta dla zapytañ
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest ustalenie stanu obiektu
	 * @param lineNumber numer linii dla zapytañ
	 * @param maxBuStop maksymalna liczba przystanków
	 * @param maxDirection maksymalna liczba kierunków
	 * @param method metoda dla zapytañ
	 * @param host nazwa hosta dla zapytañ
	 */
	Task(String lineNumber, String maxBuStop, String maxDirection, String method, String host)
	{
		this.lineNumber = lineNumber;
		this.maxBuStop = maxBuStop;
		this.maxDirection = maxDirection;
		this.method = method;
		this.host = host;
	}
}
