package pl.edu.agh.kis.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

	
	
/**
 * Robot - implementacja Crawlera, przechwytujący adresy odnośników z podanej strony, 
 * jego rozwinięcia mogą wykorzystywać pobieraną ze stron treść
 * Robot posiada referencję do klasy implementującej cash z zachowaniem typu
 * producent - konsument, w tym przypadku producentem jest funkcja odpowiedzialna
 * za pobranie kolejnego linku, natomiast konsumentem funkcja odpowiedzialna za
 * wyciągnięcie czegoś ze strony
 * @author Szymon Majkut
 *
 */
public class SimpleRobot implements Crawler{
	
	//to trafi do cash
		private Queue<String> linksReadyToVisit = new LinkedList<String>();
		
		/**
		 * Zbiór na linki, które już sprawdzaliśmy, aby nie trafiły ponownie
		 * do bufora
		 */
		private Set<String> visitedLinks = new HashSet<String>();
	
		public void useLinkContent(String link) {
			System.out.println("Wyciągam obrazki z linku: " + link);
			//jakaś fajna koncepcja na wyciąganie obrazków
		}
		
		
		public String getAdditionalLinks(String url)
		{
			
			if(!url.contains("http"))
			{
				return "";
			}
			
			//od teraz ustalamy, że konkretny link jest już odwiedzony
			visitedLinks.add(url);
			
			String result = "";
			
			try {
				URL u = new URL(url);
				
				InputStream in = u.openStream();
				
				InputStreamReader iSR = new InputStreamReader(in);
				
				BufferedReader bR = new BufferedReader(iSR);
				
				String line = "";
				
				while((line = bR.readLine()) != null)
				{
					result += line+"\n";
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		public void startGetting(String site)
		{
			String additionalLinks = getAdditionalLinks(site);
			
			Pattern p = Pattern.compile("<[aA]\\s+[^>]*[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+)\" ");
			
			Matcher m = p.matcher(additionalLinks); //p.matcher("<a href=\"Lala\">strona</a>");
			
			boolean found = false;
            while (m.find()) {
            	System.out.println("Link: " +
            			m.group(1));
            	//Dodajemy do listy wykonania
            	
            	linksReadyToVisit.add(m.group(1));
            	
                found = true;
            }
            if(!found){
                System.out.println("No match found");
            }
            
            while(!linksReadyToVisit.isEmpty())
            {
            	additionalLinks = getAdditionalLinks(linksReadyToVisit.poll());
            	if(additionalLinks == "")
            	{
            		continue;
            	}
            	
            	System.out.println("W kolejce jest: "+linksReadyToVisit.size());
            	System.out.println("A tyle odwiedzonych: "+visitedLinks.size());
            	
            	p = Pattern.compile("<[aA]\\s+[^>]*[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+)\" ");
            	m = p.matcher(additionalLinks); //p.matcher("<a href=\"Lala\">strona</a>");
            	
            	found = false;
                while (m.find()) {
                	//System.out.println("Link: " +
                	//		m.group(1));
                	//Dodajemy do listy wykonania
                	linksReadyToVisit.add(m.group(1));
                	
                    found = true;
                }
                if(!found){
                    System.out.println("No match found");
                }
            }
		}
	
		public static void main(String[] args) {
			
			SimpleRobot r = new SimpleRobot();
			r.startGetting("http://kis.agh.edu.pl/");

		}
	
	
}