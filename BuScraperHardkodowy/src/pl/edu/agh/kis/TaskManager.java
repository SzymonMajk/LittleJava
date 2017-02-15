package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za obs³ugê zadañ, przede wszystkim oddelegowane s¹ do niej
 * wszelkie zmiany stanów zadañ, czyszczenie z bufora zadañ ju¿ wykonanych oraz
 * ustalanie kolejnoœci wykonywania zadañ, udostêpnia metody pozwalaj¹ca na operowanie
 * na ob³ugiwanych zadaniach.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class TaskManager {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(TaskManager.class.getName());
	
	/**
	 * Kolejka zadañ do wykonania przez program
	 */
	private Map<Integer,Task> tasksToDo = new HashMap<Integer,Task>();
	
	private ArrayDeque<Task> taskOrder = new ArrayDeque<Task>();
	
	/**
	 * Umieszcza nowe zadanie w mapie zadañ do wykonania.
	 * @param newTask nowe zadanie do wykonania
	 */
	public void put(Task newTask)
	{
		log4j.info("Dodajê nowy task o id: "+newTask.getId());
		tasksToDo.put(newTask.getId(), newTask);
		taskOrder.addLast(newTask);
	}
	
	public void pushBack(Task newTask)
	{
		log4j.info("Przesuwam task na koniec wykonania, o id: "+newTask.getId());
		taskOrder.addLast(newTask);
	}
	
	/**
	 * Sprawdza czy istniej¹ jeszcze jakieœ zadania do wykonania.
	 * @return stan mapy zadañ do wykonania
	 */
	public boolean hasNextTask()
	{
		return !tasksToDo.isEmpty();
	}
	
	/**
	 * Zadaniem funkcji jest zwrócenie pierwszego zadania, które posiada status 0, to znaczy
	 * nie zosta³o jeszcze wykonane oraz nikt siê nim jeszcze nie zajmuje
	 * @return pierwsze zadanie o statusie równym 0 z mapy zadañ do wykonania
	 */
	public Task getNextTask()
	{
		Task result;
		
		if((result = taskOrder.pollFirst()) != null)
		{
			log4j.info("Pobieram nowy Task o id: "+result.getId());
			return tasksToDo.get(result.getId());
		}
		
		return new Task();
	}
	
	/**
	 * Funckja ma za zadanie usun¹æ z mapy zadañ do wykonania, zadanie o numerze
	 * identyfikacyjnym równym temu podanemu w argumencie funkcji.
	 * @param i numer identyfikacyjny zadania, które ma zostaæ usuniête z mapy zadañ
	 *        do wykonania
	 */
	public void removeTask(Integer i)
	{
		log4j.info("Usuwam zadanie o id: "+i);
		tasksToDo.remove(i);
	}
}
