package pl.edu.agh.kis;

/**
 * Klasa pomocnicza przechowuj�ca szczeg�y, przy pomocy kt�rych w�tki pobieraj�ce
 * s� w stanie utworzy� poprawne zapytanie. Przechowuje i udost�pnia rodzaj metody zapytania
 * �ci�k� zasobu, adres hosta, parametry zapytania oraz dozwolone kodowanie.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class Request {

	private String method;
		
	private String urlPath;
	
	private String host;
	
	private String parameters;
	
	private String acceptCharset;
	
	/**
	 * Funkcja ma za zadanie zwr�ci� nazw� metody, za pomoc� kt�rej ma zosta� wykonane
	 * utworzone zapytanie. Nie mamy gwarancji poprawno�ci zwracanej danej.
	 * @return nazwa metody, za pomoc� kt�rej ma zosta� wykonane utworzone zapytanie.
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Funkcja ma za zadanie zwr�ci� adres zasobu, do kt�rego ma si� odnie�� utworzone
	 * zapytanie. Nie mamy gwarancji poprawno�ci zwracanej danej.
	 * @return adres zasobu, do kt�rego ma si� odnie�� utworzone zapytanie.
	 */
	public String getUrlPath()
	{
		return urlPath;
	}
	
	/**
	 * Funkcja ma za zadanie zwr�ci� adres hosta, z kt�rym ma si� po��czy� utworzone zapytanie.
	 * Nie mamy gwarancji poprawno�ci zwracanej danej.
	 * @return adres hosta, z kt�rym ma si� po��czy� utworzone zapytanie.
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * Funkcja ma za zadanie zwr�ci� parametry, kt�re wezm� udzia� w tworzeniu zapytania.
	 * Nie mamy gwarancji poprawno�ci zwracanej danej.
	 * @return parametry tworzonego zapytania.
	 */
	public String getParameters()
	{
		return parameters;
	}
	
	/**
	 * Funkcja ma za zadanie zwr�ci� kodowanie, kt�re mo�e by� zaakceptowane przez
	 * utworzone zapytanie. Nie mamy gwarancji poprawno�ci zwracanej danej.
	 * @return kodowanie, kt�re zapytanie b�dzie w stanie zaakceptowa�.
	 */
	public String getAcceptCharset()
	{
		return acceptCharset;
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�re zadaniem jest przypisanie prywanym polom warto�ci
	 * znajduj�cych si� w argumentach programu. Konstruktor nie sprawdza poprawno�ci otrzymanych
	 * danych, zrzucaj�c t� odpowiedzialno�� na obiekty od kt�rych dane zosta�y otrzymane oraz
	 * te, kt�re b�d� wykorzystywa� jego przypisane dane.
	 * @param method nazwa metody, za pomoc� kt�rej ma zosta� wykonane utworzone zapytanie.
	 * @param urlPath adres zasobu, do kt�rego ma si� odnie�� utworzone zapytanie.
	 * @param host adres hosta, z kt�rym ma si� po��czy� utworzone zapytanie.
	 * @param parameters parametry tworzonego zapytania.
	 * @param charset kodowanie, kt�re zapytanie b�dzie w stanie zaakceptowa�.
	 */
	Request(String method, String urlPath, String host, String parameters, String charset)
	{
		this.method = method;
		this.urlPath = urlPath;
		this.host = host;
		this.parameters = parameters;
		acceptCharset = charset;
	}
	
}
