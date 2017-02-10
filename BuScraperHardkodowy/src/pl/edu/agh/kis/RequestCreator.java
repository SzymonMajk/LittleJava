package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Istot¹ istnienia klasy jest koniecznoœæ tworzenia zestawów zapytañ dla DownloadThread
 * przy pomocy metody publicznej, która otrzymuje odpowiednie zadanie w argumencie.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class RequestCreator {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(RequestCreator.class.getName());
	
	//TODO pytanie czy nie lepiej niech RequestCreatora ma ka¿dy DownloadThread i po prosu
	//niech to tworzy pojedyncze jedno zapytanie w zale¿noœci do otrzymanych parametrów?
	
	/**
	 * Kolejka poprawnych zapytañ
	 */
	private BlockingQueue<String> requests = new ArrayBlockingQueue<String>(1);
	
	/**
	 * Funkcja pozwala na zwrócenie kolejki z gotowymi zapytaniami. U¿ytkonik jest zobligowany
	 * do wczeœniejszego uruchomienia prepareNewRequests() oraz jest zachêcony do u¿ycia
	 * clear() przed kolejnym u¿yciem tej funkcji.
	 * @return gotowe zapytania przechowywane w polu prywatnym.
	 */
	public BlockingQueue<String> getRequests()
	{
		return requests;
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
	public void prepareNewRequests(Task newTask)
	{
		String lineNumber = newTask.getLineNumber();
		int maxBuStop = Integer.parseInt(newTask.getMaxBuStop());
		int maxDirection = Integer.parseInt(newTask.getMaxDirection());
		String method = newTask.getMethod();
		String host = newTask.getHost();
				
		BlockingQueue<String> requests = new ArrayBlockingQueue<String>(maxBuStop*maxDirection);
		
		if(method.equals("GET"))
		{
			for(int i = 0; i < maxDirection; ++i)
			{
				for(int j = 0; j < maxBuStop; ++j)
				{
					StringBuilder builder = new StringBuilder();
					builder.append(method);
					builder.append(" /?lang=PL&rozklad=20170120&linia=");
					builder.append(lineNumber);
					builder.append("__");
					builder.append(i);
					builder.append("__");
					builder.append(j);
					builder.append(" HTTP/1.1\r\n");
					builder.append("Host: ");
					builder.append(host);
					builder.append("\r\n");
					builder.append("Connection: close\r\n\r\n");
					requests.add(builder.toString());
					//log4j.info("Utworzy³em zapytanie:"+builder.toString());
				}
			}
		}
		else
		{
			//POST nie gotowy jeszcze...
			//TODO koniecznie trzeba zrobiæ, bo grozi œmieræ!
		}
		
	this.requests = requests;
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie kolejki, przypisuj¹c do pola prywatnego j¹
	 * przechowuj¹cego nowej pustej kolejki o maksymalnym rozmiarze równym jeden.
	 */
	public void clear()
	{
		requests = new ArrayBlockingQueue<String>(1);
	}
}