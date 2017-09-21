package pl.edu.agh.kis.crawler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Prosta implementacja na kolejce
 * @author Szymon Majkut
 *
 */
public class QueueBuffer implements Buffer {

	private Queue<String> linksReadyToVisit = new LinkedList<String>();
	
	final int MAX_SIZE = 10000;
	
	public boolean isFull() {
		return linksReadyToVisit.size() == MAX_SIZE;
	}

	public void put(String s) {
		linksReadyToVisit.add(s);
	}

	public boolean isEmpty() {
		return linksReadyToVisit.isEmpty();
	}

	public String take() {
		return linksReadyToVisit.poll();
	}

	public int size()
	{
		return linksReadyToVisit.size();
	}
}
