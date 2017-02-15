package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Istot� istnienia klasy jest konieczno�� tworzenia zestaw�w zapyta� dla DownloadThread
 * przy pomocy metody publicznej, kt�ra otrzymuje odpowiednie zadanie w argumencie.
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
	 * Kolejka poprawnych zapyta�
	 */
	private BlockingQueue<Request> requests = new ArrayBlockingQueue<Request>(1);
	
	/**
	 * Kolejka poprawnych zapyta�
	 */
	private BlockingQueue<Request> invalidRequests = new ArrayBlockingQueue<Request>(1);
	
	
	/**
	 * Funkcja pozwala na zwr�cenie kolejki z gotowymi zapytaniami. U�ytkonik jest zobligowany
	 * do wcze�niejszego uruchomienia prepareNewRequests() oraz jest zach�cony do u�ycia
	 * clear() przed kolejnym u�yciem tej funkcji.
	 * @return gotowe zapytania przechowywane w polu prywatnym.
	 */
	public BlockingQueue<Request> getRequests()
	{
		log4j.info("Zwracam kolejk� zapyta�");
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
		log4j.info("Zwracam kolejk� niewykonanych zapyta�");
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
			log4j.info("Dodaj� niewykonane zapytanie o parametrach:"
					+request.getParameters());
		} catch (InterruptedException e) {
			log4j.info("Niew�a�ciwie wybudzony przy pr�bie w�o�enia do invalidRequests:"
					+request.getParameters()+e.getMessage());
		}
		
	}
	
	/**
	 * Zadaniem funkcji jest sprawdzenie poprawno�ci zadania.
	 * @param taskToValidate zadanie kt�rego poprawno�� musimy sprawdzi�
	 * @return informacja o poprawno�ci sprawdzenia zadania
	 */
	public boolean isGoodTask(Task taskToValidate)
	{
		return true;
	}
	
	/**
	 * Funkcja otrzymuje w argumencie zadanie, z za�o�eniem, �e u�ytkownik ju� wcze�niej
	 * sprawdzi� jego poprawno�� za pomoc� metody isGoodTask(), tworzy na jego podstawie
	 * kolejk� blokuj�c� z zapytaniami dla obiekt�w DownloadThread.
	 * @param newTask nowe zadanie, na podstawie kt�rego tworzymy kolejk� zapyta�
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
					log4j.info("Doda�em nowe zapytanie o parametrach:"+builder.toString());	
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
	 * Funkcja odpowiada za wyczyszczenie kolejki, przypisuj�c do pola prywatnego j�
	 * przechowuj�cego nowej pustej kolejki o maksymalnym rozmiarze r�wnym jeden.
	 */
	public void clear()
	{
		requests = new ArrayBlockingQueue<Request>(1);
		invalidRequests = new ArrayBlockingQueue<Request>(1);
		log4j.info("Wyczyszczono kolejki zapyta� oraz zapyta� niewykonanych");
	}
}