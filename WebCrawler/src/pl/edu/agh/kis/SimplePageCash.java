package pl.edu.agh.kis;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SimplePageCash implements PageCash {

	private Set<String> alreadyVisited = new HashSet<String>();
	
	private Queue<String> readyToDownload = new PriorityQueue<String>();
	
	
	public boolean pageAlreadyVisited(String pageString) {
		return alreadyVisited.contains(pageString);
	}

	public void addVisitedPage(String pageString) {
		alreadyVisited.add(pageString);
	}

	public void addPage(String pageString) {
		readyToDownload.add(pageString);
	}

	public boolean isEmpty() {
		return readyToDownload.isEmpty();
	}

	public String getNextPage() {
		return readyToDownload.poll();
	}

	//do testów przydatne

	public String listVisited() {
		String result = "no to dupa";
		
		for(String s : alreadyVisited)
		{
			result += s + "\n";
		}
		
		return result;
	}

	public String listQueue() {
		String result = null;
		
		for(String s : readyToDownload)
		{
			result += s + "\n";
		}
		
		return result;
	}

}
