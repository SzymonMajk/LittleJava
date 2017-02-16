package pl.edu.agh.kis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Klasa przechowuj�ca pojedyncze zadanie do wykonania dla BuScrappera
 * mo�e zwraca� swoje szczeg�y: numer identyfikacyjny, kolejk� zapyta� do wykonania oraz
 * zapyta� do powt�rzenia, numer linii, oraz dane zwi�zane z nowotrzowonymi zapytaniami.
 * Nowoutrzowony Task przechowuje w kolejce obiekty Request, z kt�rych wszystkie
 * posiadaj� ten sam adres hosta. Dany taks pobiera dane dla konkretnej linii.
 * B��dy oraz wa�niejsze kroki programu s� umieszczane w logach.
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
	 * Przechowuje numer linii obs�ugiwanej przez zadanie
	 */
	private String lineNumber;
	
	/**
	 * Przechowuje maksymaln� liczb� przystank�w
	 */
	private int maxBuStop;
	
	/**
	 * Przechowuje maksymaln� liczb� kierunk�w
	 */
	private int maxDirection;
	
	/**
	 * Przechowuje nazw� metody dla zapyta�
	 */
	private String method;
	
	/**
	 * Przechowuje nazw� hosta dla zapyta�
	 */
	private String urlPath = "";
	
	/**
	 * Przechowuje nazw� hosta dla zapyta�
	 */
	private String host = "";
	
	/**
	 * Funkcja s�u�y do utworzenia kolejki request�w zgodnie z wytycznymi z p�l prywatnych
	 * danego zadania
	 * @param newTask nowe zadanie, na podstawie kt�rego tworzymy kolejk� zapyta�
	 */
	private void prepareNewRequests()
	{
		String lineNumber = getLineNumber();
		maxBuStop = getMaxBuStop();
		maxDirection = getMaxDirection();
		
			
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
	 * Zadaniem funkcji jest zwr�cenie blokuj�cej kolejki zapyta� do wykonania. Je�eli 
	 * nie zosta�a jeszcze wykonana funkcja prepareRequestsBeforeUsing, zostanie zwr�cona 
	 * pusta kolejka.
	 * @return kolejka zapyta� do wykonania, przechowywana w polu prywatnym.
	 */
	public BlockingQueue<Request> getRequestsToDo()
	{
		return requestsToDo;
	}
	
	/**
	 * Zadaniem funkcji jest zwr�cenie blokuj�cej kolejki zapyta� do powt�rzenia. Mo�liwe
	 * jest zwr�cenie pustej kolejki.
	 * @return kolejka zapyta� do wykonania, przechowywana w polu prywatnym.
	 */
	public BlockingQueue<Request> getRequestsToRepeat()
	{
		return requestsToRepeat;
	}
	
	/**
	 * Funkcja zwraca indywidualny numer obiektu Task jednoznacznie go okre�laj�cy. Numer
	 * identyfikacyjny jest przeznawany jeszcze w momencie tworzenia obiektu przez obiekt
	 * odpowiedzialny za konfiguracj�, na obiekcie tym spoczywa odpowiedzialno�� ustalenia
	 * unikalno�ci przyznawanych numer�w identyfikacyjnych.
	 * @return indywidualny numer obiektu Task jednoznacznie go okre�laj�cy.
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Zwraca numer linii obs�ugiwanej przez obiekt Task.
	 * @return numer linii obs�ugiwanej przez obiekt Task.
	 */
	public String getLineNumber()
	{
		return lineNumber;
	}
	
	/**
	 * Zwraca maksymaln� liczb� przystank�w. Jest wykorzystywany przy tworzeniu kolejki
	 * zapyta� do wykonania przy pierwszym u�yciu obiektu Task.
	 * @return maksymalna liczba przystank�w obiektu Task.
	 */
	public int getMaxBuStop()
	{
		return maxBuStop;
	}
	
	/**
	 * Zwraca maksymaln� liczb� kierunk�w. Jest wykorzystywana przy tworzeniu kolejki
	 * zapyta� do wykonania przy pierwszym u�yciu obiektu Task.
	 * @return maksymalna liczba kierunk�w nowotworzonych zapyta� obiektu Task.
	 */
	public int getMaxDirection()
	{
		return maxDirection;
	}
	
	/**
	 * Zwraca nazw� metody dla zapyta�. Jest wykorzystywana przy tworzeniu kolejki
	 * zapyta� do wykonania przy pierwszym u�yciu obiektu Task.
	 * @return nazwa metody dla zapyta�  nowotworzonych zapyta� obiektu Task.
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Zwraca nazw� pierwotny adres zasobu zapyta�. Jest wykorzystywana przy tworzeniu kolejki
	 * zapyta� do wykonania przy pierwszym u�yciu obiektu Task.
	 * @return nazwa pierwotnego adresu zasobu nowotworzonych zapyta� obiektu Task.
	 */
	public String getUrlPath()
	{
		return urlPath;
	}

	/**
	 * Zwraca nazw� pierwotnego hosta zapyta�. Jest wykorzystywana przy tworzeniu kolejki
	 * zapyta� do wykonania przy pierwszym u�yciu obiektu Task.
	 * @return nazwa pierwotnego hosta nowotworzonych zapyta� obiektu Task.
	 */
	public String getHost()
	{
		return host;
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
	 * Funkcja zwraca warto�� logiczn� pytania czy kolejka zapyta� do powt�rzenia oraz
	 * kolejka zapyta� do wykonania s� jednocze�nie puste. Je�eli odzpowied� b�dzie
	 * przecz�ca, nast�pi przepisanie wszystkich zapyta� znajduj�cych si� w kolejce zapyta�
	 * do powt�rzenia, do kolejki zapyta� do wykonania oraz wyczyszczenie kolejki zapyta�
	 * do wykonania.
	 * @return zwr�ci prawd� je�eli kolejka zapyta� do wykonania oraz kolejka zapyta�
	 * 		do powt�rzenia oka�� si� jednocze�nie puste, w przeciwnym razie zwr�ci fa�sz.
	 */
	public boolean isEmptyRequestsQueues()
	{
		if(requestsToRepeat.isEmpty() && requestsToDo.isEmpty())
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
	 * Konstruktor domy�lny uruchamiaj�cy konstruktor sparametryzowany warto�ciami
	 * zerowymi. S�u�y do utworzenia pustego obiektu Task, kt�ry ma s�u�y� dzia�aniom
	 * tymczasowym, nie powinno si� korzysta� z jego funkcji publicznych.
	 */
	public Task()
	{
		this(0,"",0,0,"","");
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest poprawne przypisanie numeru
	 * identyfikuj�cego, numeru linii, maksymalnej liczby przystank�w, maksymalnej liczby
	 * kierunk�w, metody oraz adresu hosta oraz zasobu, zdobytych z podanego w argumencie
	 * adresu url. Funkcja przypisuje r�wnie� nowe puste kolejki blokuj�ce dla kolejek
	 * zapyta� do wykonania oraz do powt�rzenia.
	 * @param id unikalny numer identyfikacyjny danego obiektu Task.
	 * @param lineNumber numer linii potrzebny przy konstrukcji pierwotnych zapyta�.
	 * @param maxBuStop maksymalna liczba przystank�w potrzebna przy konstrukcji 
	 * 		pierwotnych zapyta�.
	 * @param maxDirection maksymalna liczba kierunk�w potrzebna przy konstrukcji 
	 * 		pierwotnych zapyta�.
	 * @param method metoda dla zapyta� potrzebna przy konstrukcji pierwotnych zapyta�.
	 * @param pageUrl adres url strony, z kt�rej wydobi�dziemy adres hosta oraz adres
	 * 		zasobu.
	 */
	public Task(int id,String lineNumber, int maxBuStop, int maxDirection,
			String method, String pageUrl)
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