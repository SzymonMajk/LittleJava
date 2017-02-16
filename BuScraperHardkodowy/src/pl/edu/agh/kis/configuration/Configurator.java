package pl.edu.agh.kis.configuration;

import pl.edu.agh.kis.Task;
import pl.edu.agh.kis.TaskManager;
import pl.edu.agh.kis.search.Search;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * G�ownym zadaniem klasy jest odpowiednia analiza pliku konfiguracyjnego wype�nionego
 * przez u�ytkownika, a nast�pnie wyodr�bnienie wszystkich informacji potrzebnych dla
 * pozosta�ych komponent�w BuScrappera. Zapewnia r�wnie� mo�liwo�� udost�pnienie mapy
 * wyra�e� XPath, kolejki wyszukiwa� oraz informacji czy powinna nast�pi� aktualizacja.
 * Posiada konstruktor sparametryzowany pozwalaj�cy na ustalenie innego ni� domy�lny
 * pliku konfiguracyjnego. Zapewnia r�wnie� funkcj� przeprowadzaj�c� oraz sprwadzaj�c�
 * poprawno�� samego procesu konfiguracji. B��dy oraz wa�niejsze kroki programu
 * s� umieszczane w logach.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class Configurator {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(Configurator.class.getName());
	
	/**
	 * Obiekt zajmuj�cy si� obs�ug� zada�
	 */
	private TaskManager tasksManager;
	
	/**
	 * Mapa obiekt�w s�u��cych do przypisywania danych z pliku konfiguracyjnego
	 */
	private Map<String,LineDecision> saveLinesImplementations;
	
	/**
	 * Pole przechowuj�ce map� z zapytaniami XPath
	 */
	private HashMap<String,String> xPaths;
	
	/**
	 * Pole przechowuj�ce map� ze szczeg�ami zada�
	 */
	private HashMap<String,String> tasksDetails;
	
	/**
	 * 
	 */
	private ArrayList<String> searchDetails;
	
	/**
	 * Lista wyszukiwa� zapisanych przez u�ytkownika
	 */
	private ArrayList<Search> toSearch;
	
	/**
	 * Pole przechowuj�ce nazw� pliku konfiguracyjnego
	 */
	private String configurationFileName;
	
	/**
	 * Pole informuj�ce o tym czy klasa ma zaktualizowa� dane
	 */
	private boolean updateData;
	
	/**
	 * 
	 * @return
	 */
	private boolean parseInfo()
	{
		log4j.info("Konfigurator rozpoczyna czytanie pliku konfiguracyjnego.");
		File conf = new File(configurationFileName);
		
		ArrayList<String> toCheck = new ArrayList<String>();
		
		if(!conf.exists())
		{
			log4j.error("Plik konfiguracyjny nie istnieje.");
			return false;
		}
		else if(!conf.canRead())
		{
			log4j.error("Plik konfiguracyjny jest niemo�liwy do przeczytania.");
			return false;
		}
		
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(conf),"UTF-8"));) {
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
		} catch (IOException e) {
			log4j.error("Wyj�tek podczas czytania z pliku konfiguracyjnego!"+e.getMessage());
			return false;
		}
		
		for(String s : toCheck)
		{
			int firstEqualSignIndex = s.indexOf("=");
			
			if(firstEqualSignIndex <= 0 || firstEqualSignIndex >= s.length())
			{
				log4j.warn("B��dny format linijski pliku konfiguracyjnego!"+s);
				continue;
			}
			
			String onset = s.substring(0,firstEqualSignIndex);
			String lineContent = s.substring(firstEqualSignIndex+1);
			
			LineDecision lineDecision = saveLinesImplementations.get(onset);
			
			if(lineDecision != null)
			{
				lineDecision.saveConfigurationLineContent(onset, lineContent, this);
			}
		}
		
		log4j.info("Konfigurator poprawnie zako�czy� czytanie pliku konfiguracyjnego.");
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean prepareTasks()
	{
		log4j.info("Konfigurator rozpoczyna przygotowywanie zada�.");

		String busLinesRange = "";
		int maxBuStops = 0;
		int maxDirections = 0;
		String downloadMethod = "";
		String startUrl = "";
		
		int separatorIndex = 0;
		int startLine = 0;
		int endLine = 0;
		
		try {
			busLinesRange = tasksDetails.get("BusLinesRange");
			maxBuStops = Integer.parseInt(tasksDetails.get("MaxBusStops"));
			maxDirections = Integer.parseInt(tasksDetails.get("MaxDirections"));
			downloadMethod = tasksDetails.get("DownloadMethod");
			startUrl = tasksDetails.get("StartUrl");
			separatorIndex = busLinesRange.indexOf(":");
			startLine = Integer.parseInt(busLinesRange.
					substring(0, separatorIndex));
			endLine = Integer.parseInt(busLinesRange.
					substring(separatorIndex+1, busLinesRange.length()));

			for(int i = startLine; i <= endLine; ++i)
			{
				tasksManager.put(new Task(i,""+i,maxBuStops,maxDirections,downloadMethod,
						startUrl));
				log4j.info("Utworzono task o id: "+i);
			}
		} catch (NumberFormatException e) {
			log4j.error("Z�y format zakresu linii:"+e.getMessage());
			return false;
		} catch (NullPointerException e) {
			log4j.error("Wyj�tek przy pr�bie utworzenia zada�:"+e.getMessage());
			return false;
		}
		
		log4j.info("Konfigurator poprawnie zako�czy� przygotowywanie zada�.");
		return true;
	}
	
	/**
	 * Funkcja ma za zadanie wyodr�bni� z pliku konfiguracyjnego wszystkie wyszukiwania
	 * i wstawi� je do listy wyszukiwa� dla Browser
	 */
	private boolean preapareSearch()
	{
		log4j.info("Konfigurator rozpoczyna przygotowywanie linijek z wyszukaniami.");
		
		String firstBuStop = "";
		String secondBuStop = "";
		int typeOfDay = 0;
		int hour = 0;
		int minutes = 0;
		int maxTime = 0;
		
		try {
			for(String l : searchDetails)
			{
			
			String[] splited = l.split(":");
			
			firstBuStop = splited[0];
			secondBuStop = splited[1];
			typeOfDay = Integer.parseInt(splited[2]);
			hour = Integer.parseInt(splited[3]);
			minutes = Integer.parseInt(splited[4]);
			maxTime = Integer.parseInt(splited[5]);
				
			toSearch.add(new Search(firstBuStop,secondBuStop,typeOfDay,hour,
					minutes,maxTime));
			log4j.info("Doda�em zapytanie z przystanku: "+firstBuStop);
			}
		} catch (NumberFormatException e) {
			log4j.error("Z�y format liczby:"+e.getMessage());
			return false;
		} catch (NullPointerException e) {
			log4j.error("Wyj�tek przy pr�bie utworzenia linijki z wyszukaniem:"
					+e.getMessage());
			return false;
		}
		
		log4j.info("Konfigurator zako�czy� przygotowywanie linijek z wyszukaniami.");
		return true;
	}
	
	/**
	 * Pozwala na zmian� warto�ci przechowuj�cego warto�� logiczn� decyzji
	 * o przeprowadzaniu aktualizacji. Powinna by� ustalana przez obiekt UpdateDecision.
	 * @param update warto�� logiczna decyzji o przeprowadzaniu aktualizacji.
	 */
	void setUpdateData(boolean update)
	{
		updateData = update;
	}
	
	/**
	 * Pozwala na dodanie linijki wyra�enia XPath. Zak�adamy, �e dodawane wyra�enie
	 * zosta�o sprawdzone pod k�tem poprawno�ci. Funkcja powinna by� uruchamiana
	 * przez obiekt XPathDecision.
	 * @param xPathName nazwa wyra�enia XPath.
	 * @param xPathContent wyra�enie XPath dla zadanej nazwy XPath.
	 */
	void addXPath(String xPathName, String xPathContent)
	{
		xPaths.put(xPathName, xPathContent);
	}
	
	/**
	 * Pozwala na dodanie szczeg�u potrzebnego przy tworzeniu nowych zada�. Zak�adamy, 
	 * �e dodawane dane zosta�y sprawdzone pod k�tem swojej poprawno�ci. Funkcja powinna
	 * by� uruchamiana przez obiekt TaskDetailDecision.
	 * @param detailOnset nazwa szczeg�u zapytania.
	 * @param detailContent warto�� szczeg�u zapytania.
	 */
	void addTasksDetail(String detailOnset, String detailContent)
	{
		tasksDetails.put(detailOnset, detailContent);
	}
	
	/**
	 * Pozwala na dodanie linii z danymi wyszukania z pliku konfiguracyjnego. Ka�da
	 * z linii zostaje sprawdzona pod wzdl�dem poprawno�ci formatu. Funkcja powinna
	 * by� uruchamiana przez obiekt SearchLineDecision.
	 * @param searchContent poprawna linia zapytania, gotowa do przetworzenia na obiekt
	 * 		typu Search.
	 */
	void addLineToSearch(String searchContent)
	{
		searchDetails.add(searchContent);
	}
	
	/**
	 * Funkcja zwraca map� z zebranymi wyra�eniami XPath dla w�tk�w wy�uskuj�cych informacje.
	 * Kluczem mapy jest nazwa wyra�enia, natomiast warto�ci� wyra�enie XPath dla danej nazwy.
	 * Nazwy wyra�e� s� umieszczone w pliku konfiguracyjnym.
	 * @return mapa zawieraj�ca wyra�enia XPath. Warunkiem wyodr�bnienia poprawnych
	 * 		wyra�e� jest wcze�niejsze uruchomienie funkcji configure.
	 */
	public HashMap<String,String> getXPaths()
	{
		log4j.info("Zwracam tablic� XPath!");
		return xPaths;
	}
	
	/**
	 * Funkcja zwraca list� z obiektami Search utworzonymi na podstawie informacji podanych
	 * przez u�ytkownika do pliku konfiguracyjnego. Wyszukiwania s� przeznaczone dla obiektu
	 * Browser.  Pobierane z tej kolejki wyszukania maj� poprawny format pod warunkiem 
	 * wcze�niejszego uruchomienia funkcji configure.
	 * @return lista z zebranymi wyszukiwaniami. Warunkiem wyodr�bnienia poprawnych
	 * 		wyra�e� jest wcze�niejsze uruchomienie funkcji configure.
	 */
	public ArrayList<Search> getToSerach()
	{
		log4j.info("Zwracam tablic� toSearch!");
		return toSearch;
	}
	
	/**
	 * Zwraca informacj� logiczn� okre�laj�c� czy dane powinny zosta� zaktualizowane.
	 * @return zwraca prawd� je�eli u�ytkownik zdecydowa� si� na aktualizacj� danych oraz
	 * 		fa�sz je�eli u�ytkowni zdecydowa� si� na nieaktualizowanie danych. Warunkiem
	 * 		posiadania prawido�owej decyzji u�ytkonika jest wcze�niejsze uruchomienie
	 * 		funkcji configure.
	 */
	public boolean getUpdateData()
	{
		return updateData;
	}
	
	
	/**
	 * Funkcja odpowiada za przeprowadzenie odebrania oraz sprawdzenia poprawno�ci
	 * danych wyodr�bnionychc z pliku konfiguracyjnego. W przypadku nieistnienia lub
	 * niemo�liwo�ci przeczytania pliku konfiguracyjnego podanego w konstruktorze, zwr�ci
	 * warto�� fa�szu. W przypadku nie istnienia wymaganych danych zwr�ci warto�� fa�szu. 
	 * Nast�pnie przeprowadza przygotowanie obiekt�w Task i umieszczenie ich w pami�ci 
	 * obiektu TaskManager, w przypadku wyst�pienie problem�w zwraca warto�c fa�szu. 
	 * Na ko�cu przeprowadza przygotowanie obiekt�w typu Search, w przypadku wyst�pienia 
	 * problem�w zwr�ci warto�� fa�szu. Je�eli zar�wno odebranie jak i sprawdzenie danych 
	 * z pliku konfiguracyjnego ich kompletno�� b�d� spe�nione oraz zostan� poprawnie 
	 * utworzone wszystkie potrzebne obiekty typu Task, jak i wszystkie mo�liwe do utworzenia 
	 * obiekty typu Search, funkcja zwraca prawd�, informuj�c o poprawno�ci danych pliku 
	 * konfiguracyjnego.
	 * @return zwraca warto�� prawdy je�eli w pliku konfiguracyjnym znajdowa�y si� poprawne
	 * 		dane, je�eli zastane tam dane uniemo�liwia�y przeprowadzenie kt�rego� z zada�
	 * 		konfiguracji, zwracana jest warto�� fa�szu.
	 */
	public boolean configure()
	{
		return(parseInfo() && prepareTasks() && preapareSearch());
	}
	
	/**
	 * Konstruktor sparametryzowany, otrzymuj�cy nazw� pliku konfiguracyjnego
	 * oraz obiekt TaskManager. Przypisuje otrzymane argumenty, zak�adaj�c ich
	 * poprawno��, do prywatnych p�l. Nast�pnie inicjalizuje pust� map� wyra�e�
	 * XPath, pust� map� szczeg��w zadania, pust� list� linijek do wyszukania oraz
	 * pust� list� obiekt�w typu Search. Nast�pnie tworzy map� zawieraj�c� jako klucze
	 * wszelkie mo�liwe s�owa kluczowe pliku konfiguracyjnego, natomiast jako warto��
	 * odpowiedni obiekt implementuj�cy interfejs LineDecision, pozwalaj�cy na odpowiednie
	 * sprawdzenie oraz zesk�adowanie zawarto�ci dla danych po danym s�owie kluczowym w pliku
	 * konfiguracyjnym.
	 * @param configurationFileName �cie�ka dost�pu do pliku konfiguracyjnego, w przypadku
	 * 		nieistnienia pliku, funkcja configure zwr�ci warto�� false.
	 * @param taskManager obiekt odpowiedzialny za tworzenie oraz sk�adowanie nowoutrzowonych
	 * 		zada� w swojej pami�ci.
	 */
	public Configurator(String configurationFileName,TaskManager taskManager)
	{
		log4j.info("Konfigurator rozpoczyna prac�.");
		this.tasksManager = taskManager;
		this.configurationFileName = configurationFileName;
		xPaths = new HashMap<String,String>();
		tasksDetails = new HashMap<String,String>();
		searchDetails = new ArrayList<String>();
		toSearch = new ArrayList<Search>();
		
		saveLinesImplementations = new HashMap<String,LineDecision>();
		XPathDecision xPathdecision = new XPathDecision();
		saveLinesImplementations.put("XPathBusStopName", xPathdecision);
		saveLinesImplementations.put("XPathLineNumber", xPathdecision);
		saveLinesImplementations.put("XPathLineDirection", xPathdecision);
		saveLinesImplementations.put("XPathHours", xPathdecision);
		saveLinesImplementations.put("XPathMinutesOrdinary", xPathdecision);
		saveLinesImplementations.put("XPathMinutesSaturday", xPathdecision);
		saveLinesImplementations.put("XPathMinutesSunday", xPathdecision);
		TaskDetailDecision taskDetailDecision = new TaskDetailDecision();
		saveLinesImplementations.put("DownloadMethod", taskDetailDecision);
		saveLinesImplementations.put("StartUrl", taskDetailDecision);
		saveLinesImplementations.put("MaxBusStops", taskDetailDecision);
		saveLinesImplementations.put("MaxDirections", taskDetailDecision);
		saveLinesImplementations.put("BusLinesRange", taskDetailDecision);
		saveLinesImplementations.put("Search", new SearchLineDecision());
		saveLinesImplementations.put("Update", new UpdateDecision());
	}
}
