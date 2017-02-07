package pl.edu.agh.kis;

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
	 * Kolejka zada� do wykonania przez program
	 */
	private Map<Integer,Task> tasksToDo = new HashMap<Integer,Task>();
	
	/**
	 * Kolejka b��dnych zada�
	 */
	private Map<Integer,Task> tasksIncorrect = new HashMap<Integer,Task>();
	
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
		
		return new Task(9999,"","","","","");
	}
	
	/**
	 * Funkcja obs�uguj� sytuacj� wyst�pienia niepoprawnego zadania, takowe jest
	 * umieszczane w mapie zada� niepoprawnych oraz usuwany z mapy zada� do wykonania.
	 * @param i
	 */
	public void incorrectTask(Integer i)
	{
		System.out.println("Niepoprawne zadanie o id: "+i);
		tasksIncorrect.put(i,tasksToDo.get(i));
		tasksToDo.remove(i);
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
}
