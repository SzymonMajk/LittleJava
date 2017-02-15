package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za obs�ug� zada�, przede wszystkim oddelegowane s� do niej
 * wszelkie zmiany stan�w zada�, czyszczenie z bufora zada� ju� wykonanych oraz
 * ustalanie kolejno�ci wykonywania zada�, udost�pnia metody pozwalaj�ca na operowanie
 * na ob�ugiwanych zadaniach.
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
	 * Kolejka zada� do wykonania przez program
	 */
	private Map<Integer,Task> tasksToDo = new HashMap<Integer,Task>();
	
	private ArrayDeque<Task> taskOrder = new ArrayDeque<Task>();
	
	/**
	 * Umieszcza nowe zadanie w mapie zada� do wykonania.
	 * @param newTask nowe zadanie do wykonania
	 */
	public void put(Task newTask)
	{
		log4j.info("Dodaj� nowy task o id: "+newTask.getId());
		tasksToDo.put(newTask.getId(), newTask);
		taskOrder.addLast(newTask);
	}
	
	public void pushBack(Task newTask)
	{
		log4j.info("Przesuwam task na koniec wykonania, o id: "+newTask.getId());
		taskOrder.addLast(newTask);
	}
	
	/**
	 * Sprawdza czy istniej� jeszcze jakie� zadania do wykonania.
	 * @return stan mapy zada� do wykonania
	 */
	public boolean hasNextTask()
	{
		return !tasksToDo.isEmpty();
	}
	
	/**
	 * Zadaniem funkcji jest zwr�cenie pierwszego zadania, kt�re posiada status 0, to znaczy
	 * nie zosta�o jeszcze wykonane oraz nikt si� nim jeszcze nie zajmuje
	 * @return pierwsze zadanie o statusie r�wnym 0 z mapy zada� do wykonania
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
	 * Funckja ma za zadanie usun�� z mapy zada� do wykonania, zadanie o numerze
	 * identyfikacyjnym r�wnym temu podanemu w argumencie funkcji.
	 * @param i numer identyfikacyjny zadania, kt�re ma zosta� usuni�te z mapy zada�
	 *        do wykonania
	 */
	public void removeTask(Integer i)
	{
		log4j.info("Usuwam zadanie o id: "+i);
		tasksToDo.remove(i);
	}
}
