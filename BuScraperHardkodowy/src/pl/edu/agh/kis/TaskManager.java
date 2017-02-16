package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Klasa odpowiedzialna za zesk³¹dowanie oraz obs³ugê obiektów Task, przede wszystkim 
 * umo¿liwia udostêpnienie kolejnego zadania znajduj¹cego siê w kolejsce zadañ do wykonania,
 * umo¿liwia sprawdzenie czy istniej¹ jeszcze jakieœ zadania do wykonania. Pozwala
 * na bezpieczne zesk³adowanie zadania w pamiêci oraz umo¿liwia usuniêcie zadania z bufora.
 * Wa¿niejsze kroki programu s¹ umieszczane w logach.
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
	 * Mapa z zadaniami: klucz = id, wartoœæ = zadanie
	 */
	private Map<Integer,Task> tasksToDo = new HashMap<Integer,Task>();
	
	/**
	 * Kolejka zadañ do wykonania przez program
	 */
	private ArrayDeque<Task> taskOrder = new ArrayDeque<Task>();
	
	/**
	 * W bezpieczny sposób umieszcza nowe zadanie w mapie, którego kluczem jest numer
	 * identyfikacyjny zadania, oraz w kolejce zadañ do wykonania.
	 * @param newTask nowe zadanie, które ma zostaæ umieszczone w pamiêci obiektu TaskManager.
	 */
	public void put(Task newTask)
	{
		log4j.info("Dodajê nowy task o id: "+newTask.getId());
		tasksToDo.put(newTask.getId(), newTask);
		taskOrder.addLast(newTask);
	}
	
	/**
	 * Funkcja umo¿liwia umieszczenie podanego zadania na koñcu kolejki zadañ do wykonania.
	 * U¿ywaj¹c funkcji nale¿y zwróciæ uwagê, aby zadanie zosta³o poprzednio w poprawny
	 * sposób umieszczone w pamiêci poprzez wywo³anie metody put, na rzecz tego samego
	 * obiektu zadania.
	 * @param newTask obiekt Task, który mamy zamiar umieœciæ na koñcu kolejki zadañ
	 * 		do wykonania.
	 */
	public void pushBack(Task newTask)
	{
		log4j.info("Przesuwam task na koniec wykonania, o id: "+newTask.getId());
		taskOrder.addLast(newTask);
	}
	
	/**
	 * Funkcja umo¿liwia sprawdzenie czy w pamiêci TaskManagera znajduj¹ siê jeszcze jakieœ
	 * obiekty typu Task. Obiekty musia³y byæ poprawnie umieszczone w pamiêci obiektu
	 * TaskManager, poprzez wywo³anie metody put.
	 * @return zwróci prawdê w przypadku istnienia obiektu Task w pamiêci obiektu TaskManager,
	 * 		w przypadku gdy TaskManager nie posiada w pamieci obiektów Task, zwracany jest
	 * 		fa³sz.
	 */
	public boolean hasNextTask()
	{
		return !tasksToDo.isEmpty();
	}
	
	/**
	 * Funkcja zwraca pierwszy obiekt Task z kolejki zadañ do wykonania, w przypadku
	 * gdyby kolejka by³a pusta, zwracany jest pusty obiekt Task, utworzony przy pomocy
	 * konstruktora domyœlnego. U¿ytkownik jest zobligowany do wywo³ywania funkcji
	 * wy³¹cznie po uprzednim sprawdzeniu zawartoœci pamiêci obiektu TaskManager przy pomocy
	 * jego funkcji hasNextTask.
	 * @return pierwszy obiekt Task z kolejki zadañ do wykonania lub pusty obiekt Task
	 * 		utworzony przez konstruktor domyœlny, je¿eli kolejka okaza³a siê pusta.
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
	 * Funckja ma za zadanie w poprawny sposób usun¹æ z kolejki zadañ do wykonania
	 * oraz mapy przechowuj¹cej obiekty Task, obiekt task o numerze identyfikacyjnym
	 * podanym w argumencie funkcji.
	 * @param i numer identyfikacyjny zadania, które ma zostaæ usuniête z mapy zadañ
	 *        do wykonania oraz z kolejki zadañ do wykonania.
	 */
	public void removeTask(Integer i)
	{
		log4j.info("Usuwam zadanie o id: "+i);
		taskOrder.remove(tasksToDo.get(i));
		tasksToDo.remove(i);
	}
}
