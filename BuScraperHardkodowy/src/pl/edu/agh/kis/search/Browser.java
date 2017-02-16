package pl.edu.agh.kis.search;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;

/**
 * Klasa ma udostêpniaæ funkcjonalnoœæ wyszukiwarki z zesk³adowanych przez FileStoreBusInfo
 * danych wykorzystuje funkcjonalnoœci dwóch obiektów pomocniczych LineBrowser oraz 
 * HourBrowser bêd¹cych jego obiektami prywatnymi. Posiada jedn¹ funkcjê publiczn¹
 * umo¿liwiaj¹c¹ przeprowadzenie wyszukiwania dla linii podanych w liœcie w argumencie.
 * Posiada dwa konstruktory, sparametryzowany pozwala na okreœlenie katalogu, w którym
 * wyszukiwarka ma odnaleŸæ dane potrzebne do przeprowadzanie wyszukiwania. Wa¿niejsze
 * kroki programu s¹ umieszczane w logach.
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
	 * Przechowuje nazwê katalogu, w którym bêdziemy sk³adowaæ katalogi z danymi
	 */
	private String storedDataDirectoryName;
	
	/**
	 * Obiekt umo¿liwiaj¹cy wyszukiwanie linijek z czasami
	 */
	private HourBrowser hourBrowser = new HourBrowser();
	
	/**
	 * Obiekt umo¿liwiaj¹cy wyszukiwanie poszczególnych linii
	 */
	private LineBrowser lineBrowser = new LineBrowser();
	
	/**
	 * Funkcja otrzymuje w parametrze listê obiektów Search, dla ka¿dego z nich
	 * ma przeprowadziæ wyszukiwanie. Wyszukiwarka zak³ada, ¿e obiekty Search zosta³y
	 * poprawnie wykonane i sprawdzone na przyk³ad w obiekcie Configurator.
	 * @param search lista obiektów Search, funkcja ma przeprowadziæ wyszukanie, korzystaj¹c
	 * 		z danych zawartych w ka¿dym z obiektów tej listy.
	 */
	public void search(ArrayList<Search> search)
	{
		log4j.info("Rozpoczynam wyszukiwanie po³¹czeñ.");
		
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
						System.out.println("Dla przystanków: "+s1+" i "+s2);

						if(l.substring(0, 3).matches("^\\d\\d\\d"))
						{
							System.out.println("Mo¿esz skorzystaæ z linii: "+l.substring(0, 3));

						}
						else if(l.substring(0, 2).matches("^\\d\\d"))
						{
							System.out.println("Mo¿esz skorzystaæ z linii: "+l.substring(0, 2));
						}
						else if(l.substring(0, 1).matches("^\\d"))
						{
							System.out.println("Mo¿esz skorzystaæ z linii: "+l.substring(0, 1));
						}
						System.out.println(answer.toString());
					}
				}
			}
			else
			{
				System.out.println("Dla przystanków: "+s1+" i "+s2);
				System.out.println("Nie znaleziono bezpoœredniego po³¹czenia pomiêdzy przystankami");
			}
		}
		
		log4j.info("Wyszukiwanie po³¹czeñ zakoñczone prawid³owo.");
	}
	
	/**
	 * Konstuktor domyœlny, korzystaj¹cy z konstruktora sparametryzowanego
	 * ustalajac jako nazwê katalogu domyœlnego "Data", to jest domyœlny katalog
	 * zapisywanych danych dla obiektu FileStoreBusInfo.
	 */
	public Browser()
	{
		this("Data");
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalajacy na okreœlenie katalogu, w którym maj¹
	 * znajdowaæ siê katalogi z danymi dotycz¹cymi wyszukiwanych informacji. Nazwa katalogu
	 * nie powinna koñczyæ siê znakiem \.
	 * @param storedDataDirectoryName nazwa katalogu, w którym powinny znajdowaæ siê
	 * 		dane potrzebne do wyszukiwania.
	 */
	public Browser(String storedDataDirectoryName)
	{
		this.storedDataDirectoryName = storedDataDirectoryName+"/";
	}
}