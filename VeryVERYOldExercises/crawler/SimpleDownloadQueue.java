package pl.edu.agh.kis.crawler;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Najłatwiejsza implementacja na początek
 * @author Szymon Majkut
 *
 */
public class SimpleDownloadQueue implements DownloadQueue{

	private Queue<String> readyToDownload = new PriorityQueue<String>();
	
	public void addPage(String pageString) {
		readyToDownload.add(pageString);
	}

	public boolean isEmpty() {
		return readyToDownload.isEmpty();
	}

	public String getNextPage() {
		return readyToDownload.poll();
	}

}
