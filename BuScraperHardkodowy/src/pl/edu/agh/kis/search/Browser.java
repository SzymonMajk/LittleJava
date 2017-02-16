package pl.edu.agh.kis.search;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;

/**
 * Klasa ma udost�pnia� funkcjonalno�� wyszukiwarki z zesk�adowanych przez FileStoreBusInfo
 * danych wykorzystuje funkcjonalno�ci dw�ch obiekt�w pomocniczych LineBrowser oraz 
 * HourBrowser b�d�cych jego obiektami prywatnymi. Posiada jedn� funkcj� publiczn�
 * umo�liwiaj�c� przeprowadzenie wyszukiwania dla linii podanych w li�cie w argumencie.
 * Posiada dwa konstruktory, sparametryzowany pozwala na okre�lenie katalogu, w kt�rym
 * wyszukiwarka ma odnale�� dane potrzebne do przeprowadzanie wyszukiwania. Wa�niejsze
 * kroki programu s� umieszczane w logach.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class Browser {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(Browser.class.getName());
	
	/**
	 * Przechowuje nazw� katalogu, w kt�rym b�dziemy sk�adowa� katalogi z danymi
	 */
	private String storedDataDirectoryName;
	
	/**
	 * Obiekt umo�liwiaj�cy wyszukiwanie linijek z czasami
	 */
	private HourBrowser hourBrowser = new HourBrowser();
	
	/**
	 * Obiekt umo�liwiaj�cy wyszukiwanie poszczeg�lnych linii
	 */
	private LineBrowser lineBrowser = new LineBrowser();
	
	/**
	 * Funkcja otrzymuje w parametrze list� obiekt�w Search, dla ka�dego z nich
	 * ma przeprowadzi� wyszukiwanie. Wyszukiwarka zak�ada, �e obiekty Search zosta�y
	 * poprawnie wykonane i sprawdzone na przyk�ad w obiekcie Configurator.
	 * @param search lista obiekt�w Search, funkcja ma przeprowadzi� wyszukanie, korzystaj�c
	 * 		z danych zawartych w ka�dym z obiekt�w tej listy.
	 */
	public void search(ArrayList<Search> search)
	{
		log4j.info("Rozpoczynam wyszukiwanie po��cze�.");
		
		for(Search searchLine : search)
		{			
			String s1 = searchLine.getFirstBuStop();
			String s2 = searchLine.getSecondBuStop();
			int typeOfDay = searchLine.getTypeOfDay();
			int hour = searchLine.getHour();
			int minutes = searchLine.getMinutes();
			int maxTime = searchLine.getMaxTime();
		
			ArrayList<String> lines = new ArrayList<String>();
			
			if(lineBrowser.searchConnectingLines(storedDataDirectoryName+"buStops/"+s1,
					storedDataDirectoryName+"buStops/"+s2))
			{
				for(String l : lineBrowser.getLines())	
				{
					lines.add(l);
				}	
				
				for(String l : lines)
				{
					hourBrowser.searchHours(storedDataDirectoryName+l+"/"+s1,
							storedDataDirectoryName+l+"/"+s2, 
							hour, minutes, maxTime, typeOfDay);
					
					StringBuilder answer = new StringBuilder();
					
					for(String s : hourBrowser.getHours())
					{
						answer.append(s+"\n");
					}
										
					if(!answer.toString().equals(""))
					{
						System.out.println("Dla przystank�w: "+s1+" i "+s2);

						if(l.substring(0, 3).matches("^\\d\\d\\d"))
						{
							System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 3));

						}
						else if(l.substring(0, 2).matches("^\\d\\d"))
						{
							System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 2));
						}
						else if(l.substring(0, 1).matches("^\\d"))
						{
							System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 1));
						}
						System.out.println(answer.toString());
					}
				}
			}
			else
			{
				System.out.println("Dla przystank�w: "+s1+" i "+s2);
				System.out.println("Nie znaleziono bezpo�redniego po��czenia pomi�dzy przystankami");
			}
		}
		
		log4j.info("Wyszukiwanie po��cze� zako�czone prawid�owo.");
	}
	
	/**
	 * Konstuktor domy�lny, korzystaj�cy z konstruktora sparametryzowanego
	 * ustalajac jako nazw� katalogu domy�lnego "Data", to jest domy�lny katalog
	 * zapisywanych danych dla obiektu FileStoreBusInfo.
	 */
	public Browser()
	{
		this("Data");
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalajacy na okre�lenie katalogu, w kt�rym maj�
	 * znajdowa� si� katalogi z danymi dotycz�cymi wyszukiwanych informacji. Nazwa katalogu
	 * nie powinna ko�czy� si� znakiem \.
	 * @param storedDataDirectoryName nazwa katalogu, w kt�rym powinny znajdowa� si�
	 * 		dane potrzebne do wyszukiwania.
	 */
	public Browser(String storedDataDirectoryName)
	{
		this.storedDataDirectoryName = storedDataDirectoryName+"/";
	}
}