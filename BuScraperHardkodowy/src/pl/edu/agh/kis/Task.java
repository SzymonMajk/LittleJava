package pl.edu.agh.kis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Klasa przechowuj¹ca pojedyncze zadanie do wykonania dla BuScrappera
 * mo¿e zwracaæ swoje szczegó³y: numer identyfikacyjny, kolejkê zapytañ do wykonania oraz
 * zapytañ do powtórzenia, numer linii, oraz dane zwi¹zane z nowotrzowonymi zapytaniami.
 * Nowoutrzowony Task przechowuje w kolejce obiekty Request, z których wszystkie
 * posiadaj¹ ten sam adres hosta. Dany taks pobiera dane dla konkretnej linii.
 * B³êdy oraz wa¿niejsze kroki programu s¹ umieszczane w logach.
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
	 * Przechowuje numer linii obs³ugiwanej przez zadanie
	 */
	private String lineNumber;
	
	/**
	 * Przechowuje maksymaln¹ liczbê przystanków
	 */
	private int maxBuStop;
	
	/**
	 * Przechowuje maksymaln¹ liczbê kierunków
	 */
	private int maxDirection;
	
	/**
	 * Przechowuje nazwê metody dla zapytañ
	 */
	private String method;
	
	/**
	 * Przechowuje nazwê hosta dla zapytañ
	 */
	private String urlPath = "";
	
	/**
	 * Przechowuje nazwê hosta dla zapytañ
	 */
	private String host = "";
	
	/**
	 * Funkcja s³u¿y do utworzenia kolejki requestów zgodnie z wytycznymi z pól prywatnych
	 * danego zadania
	 * @param newTask nowe zadanie, na podstawie którego tworzymy kolejkê zapytañ
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
				log4j.info("Doda³em nowe zapytanie o parametrach:"+builder.toString());	
			}
		}
	}
	
	/**
	 * Zadaniem funkcji jest zwrócenie blokuj¹cej kolejki zapytañ do wykonania. Je¿eli 
	 * nie zosta³a jeszcze wykonana funkcja prepareRequestsBeforeUsing, zostanie zwrócona 
	 * pusta kolejka.
	 * @return kolejka zapytañ do wykonania, przechowywana w polu prywatnym.
	 */
	public BlockingQueue<Request> getRequestsToDo()
	{
		return requestsToDo;
	}
	
	/**
	 * Zadaniem funkcji jest zwrócenie blokuj¹cej kolejki zapytañ do powtórzenia. Mo¿liwe
	 * jest zwrócenie pustej kolejki.
	 * @return kolejka zapytañ do wykonania, przechowywana w polu prywatnym.
	 */
	public BlockingQueue<Request> getRequestsToRepeat()
	{
		return requestsToRepeat;
	}
	
	/**
	 * Funkcja zwraca indywidualny numer obiektu Task jednoznacznie go okreœlaj¹cy. Numer
	 * identyfikacyjny jest przeznawany jeszcze w momencie tworzenia obiektu przez obiekt
	 * odpowiedzialny za konfiguracjê, na obiekcie tym spoczywa odpowiedzialnoœæ ustalenia
	 * unikalnoœci przyznawanych numerów identyfikacyjnych.
	 * @return indywidualny numer obiektu Task jednoznacznie go okreœlaj¹cy.
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Zwraca numer linii obs³ugiwanej przez obiekt Task.
	 * @return numer linii obs³ugiwanej przez obiekt Task.
	 */
	public String getLineNumber()
	{
		return lineNumber;
	}
	
	/**
	 * Zwraca maksymaln¹ liczbê przystanków. Jest wykorzystywany przy tworzeniu kolejki
	 * zapytañ do wykonania przy pierwszym u¿yciu obiektu Task.
	 * @return maksymalna liczba przystanków obiektu Task.
	 */
	public int getMaxBuStop()
	{
		return maxBuStop;
	}
	
	/**
	 * Zwraca maksymaln¹ liczbê kierunków. Jest wykorzystywana przy tworzeniu kolejki
	 * zapytañ do wykonania przy pierwszym u¿yciu obiektu Task.
	 * @return maksymalna liczba kierunków nowotworzonych zapytañ obiektu Task.
	 */
	public int getMaxDirection()
	{
		return maxDirection;
	}
	
	/**
	 * Zwraca nazwê metody dla zapytañ. Jest wykorzystywana przy tworzeniu kolejki
	 * zapytañ do wykonania przy pierwszym u¿yciu obiektu Task.
	 * @return nazwa metody dla zapytañ  nowotworzonych zapytañ obiektu Task.
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Zwraca nazwê pierwotny adres zasobu zapytañ. Jest wykorzystywana przy tworzeniu kolejki
	 * zapytañ do wykonania przy pierwszym u¿yciu obiektu Task.
	 * @return nazwa pierwotnego adresu zasobu nowotworzonych zapytañ obiektu Task.
	 */
	public String getUrlPath()
	{
		return urlPath;
	}

	/**
	 * Zwraca nazwê pierwotnego hosta zapytañ. Jest wykorzystywana przy tworzeniu kolejki
	 * zapytañ do wykonania przy pierwszym u¿yciu obiektu Task.
	 * @return nazwa pierwotnego hosta nowotworzonych zapytañ obiektu Task.
	 */
	public String getHost()
	{
		return host;
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
	 * Funkcja zwraca wartoœæ logiczn¹ pytania czy kolejka zapytañ do powtórzenia oraz
	 * kolejka zapytañ do wykonania s¹ jednoczeœnie puste. Je¿eli odzpowiedŸ bêdzie
	 * przecz¹ca, nast¹pi przepisanie wszystkich zapytañ znajduj¹cych siê w kolejce zapytañ
	 * do powtórzenia, do kolejki zapytañ do wykonania oraz wyczyszczenie kolejki zapytañ
	 * do wykonania.
	 * @return zwróci prawdê je¿eli kolejka zapytañ do wykonania oraz kolejka zapytañ
	 * 		do powtórzenia oka¿¹ siê jednoczeœnie puste, w przeciwnym razie zwróci fa³sz.
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
				log4j.error("Nieudane umieszczenie zapytania w kolejsce do powtórzenia"
						+e.getMessage());
			}
			
		}

		requestsToDo.clear();
		return false;
	}
	
	/**
	 * Konstruktor domyœlny uruchamiaj¹cy konstruktor sparametryzowany wartoœciami
	 * zerowymi. S³u¿y do utworzenia pustego obiektu Task, który ma s³u¿yæ dzia³aniom
	 * tymczasowym, nie powinno siê korzystaæ z jego funkcji publicznych.
	 */
	public Task()
	{
		this(0,"",0,0,"","");
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest poprawne przypisanie numeru
	 * identyfikuj¹cego, numeru linii, maksymalnej liczby przystanków, maksymalnej liczby
	 * kierunków, metody oraz adresu hosta oraz zasobu, zdobytych z podanego w argumencie
	 * adresu url. Funkcja przypisuje równie¿ nowe puste kolejki blokuj¹ce dla kolejek
	 * zapytañ do wykonania oraz do powtórzenia.
	 * @param id unikalny numer identyfikacyjny danego obiektu Task.
	 * @param lineNumber numer linii potrzebny przy konstrukcji pierwotnych zapytañ.
	 * @param maxBuStop maksymalna liczba przystanków potrzebna przy konstrukcji 
	 * 		pierwotnych zapytañ.
	 * @param maxDirection maksymalna liczba kierunków potrzebna przy konstrukcji 
	 * 		pierwotnych zapytañ.
	 * @param method metoda dla zapytañ potrzebna przy konstrukcji pierwotnych zapytañ.
	 * @param pageUrl adres url strony, z której wydobiêdziemy adres hosta oraz adres
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
			log4j.error("Niew³aœciwy format url"+e.getMessage());
		}
		
		requestsToDo = new ArrayBlockingQueue<Request>(1);
		requestsToRepeat = new ArrayBlockingQueue<Request>(1);
	}
}