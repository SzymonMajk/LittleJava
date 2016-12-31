package pl.edu.agh.kis;

import java.util.Queue;
import java.util.LinkedList;

/**
 * Najłatwiejsza implementacja składowania logów, oparta na kolejce
 * @author Szymon Majkut
 *
 */
public class SimpleLogCash implements LogsCashing {

	private Queue<String> logsQueue = new LinkedList<String>();
	
	/**
	 * Funkcja odpowiada za umieszczenie loga w kolejce
	 * @param s podany do casha log w postaci Stringa
	 */
	public void addLog(String s) {
		logsQueue.add(s);

	}

	/**
	 * Funkcja odpowiada za sprawdzenie stanu zawartości casha
	 * @return informacja o tym czy cash jest pusty
	 */
	public boolean isEmpty() {
		return logsQueue.isEmpty();
	}

	/**
	 * Funkcja odpowiada za pobranie pierwszego elementu z casha
	 * @return pierwszy element w kolejce w postaci Stringa
	 */
	public String pollLog() {
		return logsQueue.poll();
	}

}