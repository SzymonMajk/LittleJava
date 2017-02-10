package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za obs³ugê zadañ, przede wszystkim oddelegowane s¹ do niej
 * wszelkie zmiany stanów zadañ, czyszczenie z bufora zadañ ju¿ wykonanych oraz
 * ustalanie kolejnoœci wykonywania zadañ, udostêpnia metody pozwalaj¹ca na operowanie
 * na ob³ugiwanych zadaniach.
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
	 * Kolejka zadañ do wykonania przez program
	 */
	private Map<Integer,Task> tasksToDo = new HashMap<Integer,Task>();
	
	/**
	 * Umieszcza nowe zadanie w mapie zadañ do wykonania.
	 * @param newTask nowe zadanie do wykonania
	 */
	public void put(Task newTask)
	{
		tasksToDo.put(newTask.getId(), newTask);
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
		for(Integer i : tasksToDo.keySet())
		{
			//Bierzemy pierwszy do zrobienia
			if(tasksToDo.get(i).getStatus() == 0)
			{
				return tasksToDo.get(i);
			}
		}
		
		//TODO Oj, tutaj trzeba coœ wymyœleæ!
		return new Task(9999,"","","","","");
	}
	
	/**
	 * Funckja ma za zadanie usun¹æ z mapy zadañ do wykonania, zadanie o numerze
	 * identyfikacyjnym równym temu podanemu w argumencie funkcji.
	 * @param i numer identyfikacyjny zadania, które ma zostaæ usuniête z mapy zadañ
	 *        do wykonania
	 */
	public void removeTask(Integer i)
	{
		System.out.println("Usuwam zadanie o id: "+i);
		tasksToDo.remove(i);
	}
	
	//TODO, wracamy do innej koncepcji... No có¿ po pierwsze zadania niewykonane musz¹
	//trafiaæ na koniec KOLEJKI, no i teraz jeszcze sprawa, ¿e musimy mieæ mo¿liwoœæ
	//utworzenia i przypisania na koniec kolejki nowych zadañ, tych z b³êdnymi requestami!
}
