package pl.edu.agh.kis;

/**
 * Klasa pomocnicza przechowuj¹ca szczegó³y, przy pomocy których w¹tki pobieraj¹ce
 * s¹ w stanie utworzyæ poprawne zapytanie. Przechowuje i udostêpnia rodzaj metody zapytania
 * œciê¿kê zasobu, adres hosta, parametry zapytania oraz dozwolone kodowanie.
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
	 * Funkcja ma za zadanie zwróciæ nazwê metody, za pomoc¹ której ma zostaæ wykonane
	 * utworzone zapytanie. Nie mamy gwarancji poprawnoœci zwracanej danej.
	 * @return nazwa metody, za pomoc¹ której ma zostaæ wykonane utworzone zapytanie.
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Funkcja ma za zadanie zwróciæ adres zasobu, do którego ma siê odnieœæ utworzone
	 * zapytanie. Nie mamy gwarancji poprawnoœci zwracanej danej.
	 * @return adres zasobu, do którego ma siê odnieœæ utworzone zapytanie.
	 */
	public String getUrlPath()
	{
		return urlPath;
	}
	
	/**
	 * Funkcja ma za zadanie zwróciæ adres hosta, z którym ma siê po³¹czyæ utworzone zapytanie.
	 * Nie mamy gwarancji poprawnoœci zwracanej danej.
	 * @return adres hosta, z którym ma siê po³¹czyæ utworzone zapytanie.
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * Funkcja ma za zadanie zwróciæ parametry, które wezm¹ udzia³ w tworzeniu zapytania.
	 * Nie mamy gwarancji poprawnoœci zwracanej danej.
	 * @return parametry tworzonego zapytania.
	 */
	public String getParameters()
	{
		return parameters;
	}
	
	/**
	 * Funkcja ma za zadanie zwróciæ kodowanie, które mo¿e byæ zaakceptowane przez
	 * utworzone zapytanie. Nie mamy gwarancji poprawnoœci zwracanej danej.
	 * @return kodowanie, które zapytanie bêdzie w stanie zaakceptowaæ.
	 */
	public String getAcceptCharset()
	{
		return acceptCharset;
	}
	
	/**
	 * Konstruktor sparametryzowany, które zadaniem jest przypisanie prywanym polom wartoœci
	 * znajduj¹cych siê w argumentach programu. Konstruktor nie sprawdza poprawnoœci otrzymanych
	 * danych, zrzucaj¹c tê odpowiedzialnoœæ na obiekty od których dane zosta³y otrzymane oraz
	 * te, które bêd¹ wykorzystywaæ jego przypisane dane.
	 * @param method nazwa metody, za pomoc¹ której ma zostaæ wykonane utworzone zapytanie.
	 * @param urlPath adres zasobu, do którego ma siê odnieœæ utworzone zapytanie.
	 * @param host adres hosta, z którym ma siê po³¹czyæ utworzone zapytanie.
	 * @param parameters parametry tworzonego zapytania.
	 * @param charset kodowanie, które zapytanie bêdzie w stanie zaakceptowaæ.
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
