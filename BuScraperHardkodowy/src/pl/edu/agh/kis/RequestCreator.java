package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Istot¹ istnienia klasy jest koniecznoœæ tworzenia zestawów zapytañ dla DownloadThread
 * przy pomocy metody publicznej, która otrzymuje odpowiednie zadanie w argumencie.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class RequestCreator {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(RequestCreator.class.getName());
	
	/**
	 * Kolejka poprawnych zapytañ
	 */
	private BlockingQueue<Request> requests = new ArrayBlockingQueue<Request>(1);
	
	/**
	 * Kolejka poprawnych zapytañ
	 */
	private BlockingQueue<Request> invalidRequests = new ArrayBlockingQueue<Request>(1);
	
	
	/**
	 * Funkcja pozwala na zwrócenie kolejki z gotowymi zapytaniami. U¿ytkonik jest zobligowany
	 * do wczeœniejszego uruchomienia prepareNewRequests() oraz jest zachêcony do u¿ycia
	 * clear() przed kolejnym u¿yciem tej funkcji.
	 * @return gotowe zapytania przechowywane w polu prywatnym.
	 */
	public BlockingQueue<Request> getRequests()
	{
		log4j.info("Zwracam kolejkê zapytañ");
		return requests;
	}
	
	public void addUnifinishedRequests()
	{
		if(requests.isEmpty())
		{
			return;
		}
		
		for(Request r : requests)
		{
			putInvalidRequest(r);
		}
	}
	
	public boolean hasNextRequest()
	{
		return !requests.isEmpty();
	}
	
	public BlockingQueue<Request> getInvalidRequests()
	{
		log4j.info("Zwracam kolejkê niewykonanych zapytañ");
		return invalidRequests;
	}
	
	public boolean isEmptyInvalidRequests()
	{
		return invalidRequests.isEmpty();
	}
	
	public void putInvalidRequest(Request request)
	{
		
		try {
			invalidRequests.put(request);
			log4j.info("Dodajê niewykonane zapytanie o parametrach:"
					+request.getParameters());
		} catch (InterruptedException e) {
			log4j.info("Niew³aœciwie wybudzony przy próbie w³o¿enia do invalidRequests:"
					+request.getParameters()+e.getMessage());
		}
		
	}
	
	/**
	 * Zadaniem funkcji jest sprawdzenie poprawnoœci zadania.
	 * @param taskToValidate zadanie którego poprawnoœæ musimy sprawdziæ
	 * @return informacja o poprawnoœci sprawdzenia zadania
	 */
	public boolean isGoodTask(Task taskToValidate)
	{
		return true;
	}
	
	/**
	 * Funkcja otrzymuje w argumencie zadanie, z za³o¿eniem, ¿e u¿ytkownik ju¿ wczeœniej
	 * sprawdzi³ jego poprawnoœæ za pomoc¹ metody isGoodTask(), tworzy na jego podstawie
	 * kolejkê blokuj¹c¹ z zapytaniami dla obiektów DownloadThread.
	 * @param newTask nowe zadanie, na podstawie którego tworzymy kolejkê zapytañ
	 */
	public void prepareRequests(Task newTask)
	{
		//Sprawdzamy czy mamy do czynienia z nowym zadaniem czy zadaniem do poprawienia
		if(newTask.getStatus() == 0)
		{
			String lineNumber = newTask.getLineNumber();
			int maxBuStop = Integer.parseInt(newTask.getMaxBuStop());
			int maxDirection = Integer.parseInt(newTask.getMaxDirection());
			
			BlockingQueue<Request> requests = 
					new ArrayBlockingQueue<Request>(maxBuStop*maxDirection);
			invalidRequests = new ArrayBlockingQueue<Request>(maxBuStop*maxDirection);
			
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
					requests.add(new Request(newTask.getMethod(), newTask.getUrlPath(),
							newTask.getHost(),builder.toString(),"utf-8"));
					log4j.info("Doda³em nowe zapytanie o parametrach:"+builder.toString());	
				}
			}
			
			this.requests = requests;
		}
		else
		{
			requests = newTask.pollRequestsToRepeat();
			invalidRequests = new ArrayBlockingQueue<Request>(requests.size());
		}	
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie kolejki, przypisuj¹c do pola prywatnego j¹
	 * przechowuj¹cego nowej pustej kolejki o maksymalnym rozmiarze równym jeden.
	 */
	public void clear()
	{
		requests = new ArrayBlockingQueue<Request>(1);
		invalidRequests = new ArrayBlockingQueue<Request>(1);
		log4j.info("Wyczyszczono kolejki zapytañ oraz zapytañ niewykonanych");
	}
}