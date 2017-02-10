package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Istot� istnienia klasy jest konieczno�� tworzenia zestaw�w zapyta� dla DownloadThread
 * przy pomocy metody publicznej, kt�ra otrzymuje odpowiednie zadanie w argumencie.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class RequestCreator {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(RequestCreator.class.getName());
	
	//TODO pytanie czy nie lepiej niech RequestCreatora ma ka�dy DownloadThread i po prosu
	//niech to tworzy pojedyncze jedno zapytanie w zale�no�ci do otrzymanych parametr�w?
	
	/**
	 * Kolejka poprawnych zapyta�
	 */
	private BlockingQueue<String> requests = new ArrayBlockingQueue<String>(1);
	
	/**
	 * Funkcja pozwala na zwr�cenie kolejki z gotowymi zapytaniami. U�ytkonik jest zobligowany
	 * do wcze�niejszego uruchomienia prepareNewRequests() oraz jest zach�cony do u�ycia
	 * clear() przed kolejnym u�yciem tej funkcji.
	 * @return gotowe zapytania przechowywane w polu prywatnym.
	 */
	public BlockingQueue<String> getRequests()
	{
		return requests;
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
					//log4j.info("Utworzy�em zapytanie:"+builder.toString());
				}
			}
		}
		else
		{
			//POST nie gotowy jeszcze...
			//TODO koniecznie trzeba zrobi�, bo grozi �mier�!
		}
		
	this.requests = requests;
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie kolejki, przypisuj�c do pola prywatnego j�
	 * przechowuj�cego nowej pustej kolejki o maksymalnym rozmiarze r�wnym jeden.
	 */
	public void clear()
	{
		requests = new ArrayBlockingQueue<String>(1);
	}
}