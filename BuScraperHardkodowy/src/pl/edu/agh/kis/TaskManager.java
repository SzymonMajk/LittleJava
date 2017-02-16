package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za zesk��dowanie oraz obs�ug� obiekt�w Task, przede wszystkim 
 * umo�liwia udost�pnienie kolejnego zadania znajduj�cego si� w kolejsce zada� do wykonania,
 * umo�liwia sprawdzenie czy istniej� jeszcze jakie� zadania do wykonania. Pozwala
 * na bezpieczne zesk�adowanie zadania w pami�ci oraz umo�liwia usuni�cie zadania z bufora.
 * Wa�niejsze kroki programu s� umieszczane w logach.
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
	 * Mapa z zadaniami: klucz = id, warto�� = zadanie
	 */
	private Map<Integer,Task> tasksToDo = new HashMap<Integer,Task>();
	
	/**
	 * Kolejka zada� do wykonania przez program
	 */
	private ArrayDeque<Task> taskOrder = new ArrayDeque<Task>();
	
	/**
	 * W bezpieczny spos�b umieszcza nowe zadanie w mapie, kt�rego kluczem jest numer
	 * identyfikacyjny zadania, oraz w kolejce zada� do wykonania.
	 * @param newTask nowe zadanie, kt�re ma zosta� umieszczone w pami�ci obiektu TaskManager.
	 */
	public void put(Task newTask)
	{
		log4j.info("Dodaj� nowy task o id: "+newTask.getId());
		tasksToDo.put(newTask.getId(), newTask);
		taskOrder.addLast(newTask);
	}
	
	/**
	 * Funkcja umo�liwia umieszczenie podanego zadania na ko�cu kolejki zada� do wykonania.
	 * U�ywaj�c funkcji nale�y zwr�ci� uwag�, aby zadanie zosta�o poprzednio w poprawny
	 * spos�b umieszczone w pami�ci poprzez wywo�anie metody put, na rzecz tego samego
	 * obiektu zadania.
	 * @param newTask obiekt Task, kt�ry mamy zamiar umie�ci� na ko�cu kolejki zada�
	 * 		do wykonania.
	 */
	public void pushBack(Task newTask)
	{
		log4j.info("Przesuwam task na koniec wykonania, o id: "+newTask.getId());
		taskOrder.addLast(newTask);
	}
	
	/**
	 * Funkcja umo�liwia sprawdzenie czy w pami�ci TaskManagera znajduj� si� jeszcze jakie�
	 * obiekty typu Task. Obiekty musia�y by� poprawnie umieszczone w pami�ci obiektu
	 * TaskManager, poprzez wywo�anie metody put.
	 * @return zwr�ci prawd� w przypadku istnienia obiektu Task w pami�ci obiektu TaskManager,
	 * 		w przypadku gdy TaskManager nie posiada w pamieci obiekt�w Task, zwracany jest
	 * 		fa�sz.
	 */
	public boolean hasNextTask()
	{
		return !tasksToDo.isEmpty();
	}
	
	/**
	 * Funkcja zwraca pierwszy obiekt Task z kolejki zada� do wykonania, w przypadku
	 * gdyby kolejka by�a pusta, zwracany jest pusty obiekt Task, utworzony przy pomocy
	 * konstruktora domy�lnego. U�ytkownik jest zobligowany do wywo�ywania funkcji
	 * wy��cznie po uprzednim sprawdzeniu zawarto�ci pami�ci obiektu TaskManager przy pomocy
	 * jego funkcji hasNextTask.
	 * @return pierwszy obiekt Task z kolejki zada� do wykonania lub pusty obiekt Task
	 * 		utworzony przez konstruktor domy�lny, je�eli kolejka okaza�a si� pusta.
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
	 * Funckja ma za zadanie w poprawny spos�b usun�� z kolejki zada� do wykonania
	 * oraz mapy przechowuj�cej obiekty Task, obiekt task o numerze identyfikacyjnym
	 * podanym w argumencie funkcji.
	 * @param i numer identyfikacyjny zadania, kt�re ma zosta� usuni�te z mapy zada�
	 *        do wykonania oraz z kolejki zada� do wykonania.
	 */
	public void removeTask(Integer i)
	{
		log4j.info("Usuwam zadanie o id: "+i);
		taskOrder.remove(tasksToDo.get(i));
		tasksToDo.remove(i);
	}
}
