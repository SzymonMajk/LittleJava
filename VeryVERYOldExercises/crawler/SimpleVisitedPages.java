package pl.edu.agh.kis.crawler;

import java.util.Set;
import java.util.HashSet;

public class SimpleVisitedPages implements VisitedPages {

	Set<String> alreadyVisited = new HashSet<String>();
	
	public boolean pageAlreadyVisited(String pageString) {
		return alreadyVisited.contains(pageString);
	}

	public void addVisitedPage(String pageString) {
		alreadyVisited.add(pageString);
	}

}
