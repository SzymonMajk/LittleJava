package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za obs�ug� zada�, przede wszystkim oddelegowane s� do niej
 * wszelkie zmiany stan�w zada�, czyszczenie z bufora zada� ju� wykonanych oraz
 * ustalanie kolejno�ci wykonywania zada�, udost�pnia metody pozwalaj�ca na operowanie
 * na ob�ugiwanych zadaniach.
 * @author Szymon Majkut
 * @version 1.4
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
	
	/**
	 * Umieszcza nowe zadanie w mapie zada� do wykonania.
	 * @param newTask nowe zadanie do wykonania
	 */
	public void put(Task newTask)
	{
		tasksToDo.put(newTask.getId(), newTask);
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
		for(Integer i : tasksToDo.keySet())
		{
			//Bierzemy pierwszy do zrobienia
			if(tasksToDo.get(i).getStatus() == 0)
			{
				return tasksToDo.get(i);
			}
		}
		
		//TODO Oj, tutaj trzeba co� wymy�le�!
		return new Task(9999,"","","","","");
	}
	
	/**
	 * Funckja ma za zadanie usun�� z mapy zada� do wykonania, zadanie o numerze
	 * identyfikacyjnym r�wnym temu podanemu w argumencie funkcji.
	 * @param i numer identyfikacyjny zadania, kt�re ma zosta� usuni�te z mapy zada�
	 *        do wykonania
	 */
	public void removeTask(Integer i)
	{
		System.out.println("Usuwam zadanie o id: "+i);
		tasksToDo.remove(i);
	}
	
	//TODO, wracamy do innej koncepcji... No c� po pierwsze zadania niewykonane musz�
	//trafia� na koniec KOLEJKI, no i teraz jeszcze sprawa, �e musimy mie� mo�liwo��
	//utworzenia i przypisania na koniec kolejki nowych zada�, tych z b��dnymi requestami!
}
