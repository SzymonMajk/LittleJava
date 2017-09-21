package pl.edu.agh.kis.logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Najłatwiejsza implementacja składowania logów, oparta na kolejce
 * @author Szymon Majkut
 *
 */
public class SimpleLogCash implements LogsCashing {

	private Queue<String> logsQueue = new LinkedList<String>();
	
	public void addLog(String s) {
		logsQueue.add(s);

	}

	public boolean isEmpty() {
		return logsQueue.isEmpty();
	}

	public String pollLog() {
		return logsQueue.poll();
	}

}