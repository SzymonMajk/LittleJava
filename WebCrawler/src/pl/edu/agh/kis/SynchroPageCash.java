package pl.edu.agh.kis;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SynchroPageCash implements PageCash {

	private Set<String> alreadyVisited = new HashSet<String>();
	
	private Queue<String> readyToDownload = new PriorityQueue<String>();
	
	
	synchronized public boolean pageAlreadyVisited(String pageString) {
		return alreadyVisited.contains(pageString);
	}

	synchronized public void addVisitedPage(String pageString) {
		alreadyVisited.add(pageString);
	}

	synchronized public void addPage(String pageString) {
		readyToDownload.add(pageString);
	}

	synchronized public boolean isEmpty() {
		return readyToDownload.isEmpty();
	}

	synchronized public String getNextPage() {
		return readyToDownload.poll();
	}

	//do testów przydatne

	synchronized public String listVisited() {
		String result = "no to dupa";
		
		for(String s : alreadyVisited)
		{
			result += s + "\n";
		}
		
		return result;
	}

	synchronized public String listQueue() {
		String result = null;
		
		for(String s : readyToDownload)
		{
			result += s + "\n";
		}
		
		return result;
	}

}
