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
public class SimpleRobotV2 implements CrawlerV2{
	
	//można np. rozszerzyć o ustalanie ich w konstruktorze z parametrów
		private String linkRegex = "<[aA]\\s+[^>]*[hH][rR][eE][fF]\\s*=\\s*\"([^\"]+)\" ";
		
		private String elementRegex = "(AGH)";
	
	//tutaj jeszcze wkleimy naszego buffered casha
		private Buffer buffer;
		
		/**
		 * Zbiór na linki, które już sprawdzaliśmy, aby nie trafiły ponownie
		 * do bufora
		 */
		private Set<String> visitedLinks = new HashSet<String>();
	
		public void useFindedElement(String link) {
			System.out.println("Wyciągam obrazki z linku: " + link);
			//jakaś fajna koncepcja na wyciąganie obrazków
		}
		
		
		public void searchPage(String url)
		{
			//od teraz ustalamy, że konkretny link jest już odwiedzony
			visitedLinks.add(url);
			
			if(!url.contains("http"))
			{
				//W przyszłości zajmiemy się doklejeniem http do elementów ze strony
				return;
			}
			
			
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
			
			//teraz w result znajduje się zawartość strony i możemy ją przeszukiwać
			
			//Wyszukiwanie kolejnych linków
			Pattern p = Pattern.compile(linkRegex);		
			Matcher m = p.matcher(result);
			
			boolean found = false;
            while (m.find()) {
            	System.out.println("Znalazłem link!: " +
            			m.group(1));
            	//Dodajemy do linków do odwiedzenia pod warunkiem że jeszcze nieodwiedzone
            	
            	if(!visitedLinks.contains(m.group(1)))
            	{
            		buffer.put(m.group(1));
                    found = true;
            	}
            	
            }
            if(!found){
                System.out.println("Nie znaleziono nowych linków!");
            }
			
            //Teraz wyszukiwanie ciekawy treści ze strony:
            p = Pattern.compile(elementRegex);		
			m = p.matcher(result);
			
			found = false;
            while (m.find()) {
            	System.out.println("Znalazłem treść");
            	//Dodajemy do kolejki wywołania lub robimy tak jak poniżej od razu
            	useFindedElement(m.group(1));
            	
                found = true;
            }
            if(!found){
                System.out.println("Nie znaleziono elementów!");
            }
		}
		
		public void startGetting(String site)
		{
			/*Przeszukujemy pierwszą ze stron, żeby pozyskać kolejne*/
			searchPage(site);
			
			while(!buffer.isEmpty())
            {
				searchPage(buffer.take());
            	
            	System.out.println("W kolejce jest: "+buffer.size());
            	System.out.println("A tyle odwiedzonych: "+visitedLinks.size());
            	
            }
		
		}
	
		SimpleRobotV2(Buffer b)
		{
			buffer = b;
		}
		
		public static void main(String[] args) {
			
			SimpleRobotV2 r = new SimpleRobotV2(new QueueBuffer());
			r.startGetting("http://kis.agh.edu.pl/");
			//r.startGetting("localhost/");
			System.out.println("Koniec!");
		}
	
	
}