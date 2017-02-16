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
 * G³ownym zadaniem klasy jest odpowiednia analiza pliku konfiguracyjnego wype³nionego
 * przez u¿ytkownika, a nastêpnie wyodrêbnienie wszystkich informacji potrzebnych dla
 * pozosta³ych komponentów BuScrappera. Zapewnia równie¿ mo¿liwoœæ udostêpnienie mapy
 * wyra¿eñ XPath, kolejki wyszukiwañ oraz informacji czy powinna nast¹piæ aktualizacja.
 * Posiada konstruktor sparametryzowany pozwalaj¹cy na ustalenie innego ni¿ domyœlny
 * pliku konfiguracyjnego. Zapewnia równie¿ funkcjê przeprowadzaj¹c¹ oraz sprwadzaj¹c¹
 * poprawnoœæ samego procesu konfiguracji. B³êdy oraz wa¿niejsze kroki programu
 * s¹ umieszczane w logach.
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
	 * Obiekt zajmuj¹cy siê obs³ug¹ zadañ
	 */
	private TaskManager tasksManager;
	
	/**
	 * Mapa obiektów s³u¿¹cych do przypisywania danych z pliku konfiguracyjnego
	 */
	private Map<String,LineDecision> saveLinesImplementations;
	
	/**
	 * Pole przechowuj¹ce mapê z zapytaniami XPath
	 */
	private HashMap<String,String> xPaths;
	
	/**
	 * Pole przechowuj¹ce mapê ze szczegó³ami zadañ
	 */
	private HashMap<String,String> tasksDetails;
	
	/**
	 * 
	 */
	private ArrayList<String> searchDetails;
	
	/**
	 * Lista wyszukiwañ zapisanych przez u¿ytkownika
	 */
	private ArrayList<Search> toSearch;
	
	/**
	 * Pole przechowuj¹ce nazwê pliku konfiguracyjnego
	 */
	private String configurationFileName;
	
	/**
	 * Pole informuj¹ce o tym czy klasa ma zaktualizowaæ dane
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
			log4j.error("Plik konfiguracyjny jest niemo¿liwy do przeczytania.");
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
			log4j.error("Wyj¹tek podczas czytania z pliku konfiguracyjnego!"+e.getMessage());
			return false;
		}
		
		for(String s : toCheck)
		{
			int firstEqualSignIndex = s.indexOf("=");
			
			if(firstEqualSignIndex <= 0 || firstEqualSignIndex >= s.length())
			{
				log4j.warn("B³êdny format linijski pliku konfiguracyjnego!"+s);
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
		
		log4j.info("Konfigurator poprawnie zakoñczy³ czytanie pliku konfiguracyjnego.");
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean prepareTasks()
	{
		log4j.info("Konfigurator rozpoczyna przygotowywanie zadañ.");

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
			log4j.error("Z³y format zakresu linii:"+e.getMessage());
			return false;
		} catch (NullPointerException e) {
			log4j.error("Wyj¹tek przy próbie utworzenia zadañ:"+e.getMessage());
			return false;
		}
		
		log4j.info("Konfigurator poprawnie zakoñczy³ przygotowywanie zadañ.");
		return true;
	}
	
	/**
	 * Funkcja ma za zadanie wyodrêbniæ z pliku konfiguracyjnego wszystkie wyszukiwania
	 * i wstawiæ je do listy wyszukiwañ dla Browser
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
			log4j.info("Doda³em zapytanie z przystanku: "+firstBuStop);
			}
		} catch (NumberFormatException e) {
			log4j.error("Z³y format liczby:"+e.getMessage());
			return false;
		} catch (NullPointerException e) {
			log4j.error("Wyj¹tek przy próbie utworzenia linijki z wyszukaniem:"
					+e.getMessage());
			return false;
		}
		
		log4j.info("Konfigurator zakoñczy³ przygotowywanie linijek z wyszukaniami.");
		return true;
	}
	
	/**
	 * Pozwala na zmianê wartoœci przechowuj¹cego wartoœæ logiczn¹ decyzji
	 * o przeprowadzaniu aktualizacji. Powinna byæ ustalana przez obiekt UpdateDecision.
	 * @param update wartoœæ logiczna decyzji o przeprowadzaniu aktualizacji.
	 */
	void setUpdateData(boolean update)
	{
		updateData = update;
	}
	
	/**
	 * Pozwala na dodanie linijki wyra¿enia XPath. Zak³adamy, ¿e dodawane wyra¿enie
	 * zosta³o sprawdzone pod k¹tem poprawnoœci. Funkcja powinna byæ uruchamiana
	 * przez obiekt XPathDecision.
	 * @param xPathName nazwa wyra¿enia XPath.
	 * @param xPathContent wyra¿enie XPath dla zadanej nazwy XPath.
	 */
	void addXPath(String xPathName, String xPathContent)
	{
		xPaths.put(xPathName, xPathContent);
	}
	
	/**
	 * Pozwala na dodanie szczegó³u potrzebnego przy tworzeniu nowych zadañ. Zak³adamy, 
	 * ¿e dodawane dane zosta³y sprawdzone pod k¹tem swojej poprawnoœci. Funkcja powinna
	 * byæ uruchamiana przez obiekt TaskDetailDecision.
	 * @param detailOnset nazwa szczegó³u zapytania.
	 * @param detailContent wartoœæ szczegó³u zapytania.
	 */
	void addTasksDetail(String detailOnset, String detailContent)
	{
		tasksDetails.put(detailOnset, detailContent);
	}
	
	/**
	 * Pozwala na dodanie linii z danymi wyszukania z pliku konfiguracyjnego. Ka¿da
	 * z linii zostaje sprawdzona pod wzdlêdem poprawnoœci formatu. Funkcja powinna
	 * byæ uruchamiana przez obiekt SearchLineDecision.
	 * @param searchContent poprawna linia zapytania, gotowa do przetworzenia na obiekt
	 * 		typu Search.
	 */
	void addLineToSearch(String searchContent)
	{
		searchDetails.add(searchContent);
	}
	
	/**
	 * Funkcja zwraca mapê z zebranymi wyra¿eniami XPath dla w¹tków wy³uskuj¹cych informacje.
	 * Kluczem mapy jest nazwa wyra¿enia, natomiast wartoœci¹ wyra¿enie XPath dla danej nazwy.
	 * Nazwy wyra¿eñ s¹ umieszczone w pliku konfiguracyjnym.
	 * @return mapa zawieraj¹ca wyra¿enia XPath. Warunkiem wyodrêbnienia poprawnych
	 * 		wyra¿eñ jest wczeœniejsze uruchomienie funkcji configure.
	 */
	public HashMap<String,String> getXPaths()
	{
		log4j.info("Zwracam tablicê XPath!");
		return xPaths;
	}
	
	/**
	 * Funkcja zwraca listê z obiektami Search utworzonymi na podstawie informacji podanych
	 * przez u¿ytkownika do pliku konfiguracyjnego. Wyszukiwania s¹ przeznaczone dla obiektu
	 * Browser.  Pobierane z tej kolejki wyszukania maj¹ poprawny format pod warunkiem 
	 * wczeœniejszego uruchomienia funkcji configure.
	 * @return lista z zebranymi wyszukiwaniami. Warunkiem wyodrêbnienia poprawnych
	 * 		wyra¿eñ jest wczeœniejsze uruchomienie funkcji configure.
	 */
	public ArrayList<Search> getToSerach()
	{
		log4j.info("Zwracam tablicê toSearch!");
		return toSearch;
	}
	
	/**
	 * Zwraca informacjê logiczn¹ okreœlaj¹c¹ czy dane powinny zostaæ zaktualizowane.
	 * @return zwraca prawdê je¿eli u¿ytkownik zdecydowa³ siê na aktualizacjê danych oraz
	 * 		fa³sz je¿eli u¿ytkowni zdecydowa³ siê na nieaktualizowanie danych. Warunkiem
	 * 		posiadania prawido³owej decyzji u¿ytkonika jest wczeœniejsze uruchomienie
	 * 		funkcji configure.
	 */
	public boolean getUpdateData()
	{
		return updateData;
	}
	
	
	/**
	 * Funkcja odpowiada za przeprowadzenie odebrania oraz sprawdzenia poprawnoœci
	 * danych wyodrêbnionychc z pliku konfiguracyjnego. W przypadku nieistnienia lub
	 * niemo¿liwoœci przeczytania pliku konfiguracyjnego podanego w konstruktorze, zwróci
	 * wartoœæ fa³szu. W przypadku nie istnienia wymaganych danych zwróci wartoœæ fa³szu. 
	 * Nastêpnie przeprowadza przygotowanie obiektów Task i umieszczenie ich w pamiêci 
	 * obiektu TaskManager, w przypadku wyst¹pienie problemów zwraca wartoœc fa³szu. 
	 * Na koñcu przeprowadza przygotowanie obiektów typu Search, w przypadku wyst¹pienia 
	 * problemów zwróci wartoœæ fa³szu. Je¿eli zarówno odebranie jak i sprawdzenie danych 
	 * z pliku konfiguracyjnego ich kompletnoœæ bêd¹ spe³nione oraz zostan¹ poprawnie 
	 * utworzone wszystkie potrzebne obiekty typu Task, jak i wszystkie mo¿liwe do utworzenia 
	 * obiekty typu Search, funkcja zwraca prawdê, informuj¹c o poprawnoœci danych pliku 
	 * konfiguracyjnego.
	 * @return zwraca wartoœæ prawdy je¿eli w pliku konfiguracyjnym znajdowa³y siê poprawne
	 * 		dane, je¿eli zastane tam dane uniemo¿liwia³y przeprowadzenie któregoœ z zadañ
	 * 		konfiguracji, zwracana jest wartoœæ fa³szu.
	 */
	public boolean configure()
	{
		return(parseInfo() && prepareTasks() && preapareSearch());
	}
	
	/**
	 * Konstruktor sparametryzowany, otrzymuj¹cy nazwê pliku konfiguracyjnego
	 * oraz obiekt TaskManager. Przypisuje otrzymane argumenty, zak³adaj¹c ich
	 * poprawnoœæ, do prywatnych pól. Nastêpnie inicjalizuje pust¹ mapê wyra¿eñ
	 * XPath, pust¹ mapê szczegó³ów zadania, pust¹ listê linijek do wyszukania oraz
	 * pust¹ listê obiektów typu Search. Nastêpnie tworzy mapê zawieraj¹c¹ jako klucze
	 * wszelkie mo¿liwe s³owa kluczowe pliku konfiguracyjnego, natomiast jako wartoœæ
	 * odpowiedni obiekt implementuj¹cy interfejs LineDecision, pozwalaj¹cy na odpowiednie
	 * sprawdzenie oraz zesk³adowanie zawartoœci dla danych po danym s³owie kluczowym w pliku
	 * konfiguracyjnym.
	 * @param configurationFileName œcie¿ka dostêpu do pliku konfiguracyjnego, w przypadku
	 * 		nieistnienia pliku, funkcja configure zwróci wartoœæ false.
	 * @param taskManager obiekt odpowiedzialny za tworzenie oraz sk³adowanie nowoutrzowonych
	 * 		zadañ w swojej pamiêci.
	 */
	public Configurator(String configurationFileName,TaskManager taskManager)
	{
		log4j.info("Konfigurator rozpoczyna pracê.");
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
