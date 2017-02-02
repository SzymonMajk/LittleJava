package pl.edu.agh.kis;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Istot¹ istnienia klasy jest koniecznoœæ tworzenia zestawów zapytañ dla DownloadThread
 * bior¹ jako dane zawartoœæ kolejki zadañ tasks
 * @author Szymon Majkut
 * @version 1.2
 *
 */
public class RequestCreator {

	/**
	 * Kolejka zadañ, z których przygotujemy zapytania
	 */
	private LinkedList<Task> tasks = new LinkedList<Task>();
	
	/**
	 * Funkcja przypisuje dla kolejki zapytañ zapytania z nowego zadania
	 */
	private BlockingQueue<String> prepareNewRequests(BlockingQueue<String> requests)
	{
		if(!tasks.isEmpty())
		{
			Task newTask = tasks.poll();
			
			String lineNumber = newTask.getLineNumber();
			int maxBuStop = Integer.parseInt(newTask.getMaxBuStop());
			int maxDirection = Integer.parseInt(newTask.getMaxDirection());
			String method = newTask.getmethod();
			String host = newTask.getHost();
			
			requests = new ArrayBlockingQueue<String>(maxBuStop*maxDirection);
			
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
					}
				}
			}
			else
			{
				//POST nie gotowy jeszcze...
			}
		}
		
		return requests;
	}
	
	/**
	 * Funkcja ma za zadanie zwróciæ kolejkê z zapytaniami dla DownloadThread
	 * @return gotowa kolejka z nowymi requestami dla DownloadThread
	 */
	public BlockingQueue<String> getRequests()
	{
		BlockingQueue<String> requests = null;
		return prepareNewRequests(requests);
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest pod³¹czenie do obiektu
	 * kolejki zadañ
	 * @param tasks kolejka zadañ, z których przygotujemy zapytania dla DownloadThread
	 */
	RequestCreator(LinkedList<Task> tasks)
	{
		this.tasks = tasks;
	}
	
}
