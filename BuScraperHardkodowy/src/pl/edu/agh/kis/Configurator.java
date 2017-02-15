package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * G�ownym zadaniem klasy jest odpowiednia analiza pliku konfiguracyjnego wype�nionego
 * przez u�ytkownika, a nast�pnie wyodr�bnienie wszystkich informacji potrzebnych dla
 * pozosta�ych komponent�w BuScrappera. Zapewnia r�wnie� mo�liwo�� prostego udost�pniania
 * wszystkich zebranych informacji poprzez swoje gettery. Korzysta z pliku w katalogu Conf.
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
	 * Pole przechowuj�ce nazw� pliku konfiguracyjnego
	 */
	private String configurationFileName;
	
	/**
	 * Adres URL strony pocz�tkowej
	 */
	private String startPageURL;
	
	/**
	 * Pole przechowuj�ce map� z zapytaniami XPath
	 */
	private HashMap<String,String> XPaths = new HashMap<String,String>();
	
	/**
	 * Obiekt zajmuj�cy si� obs�ug� zada�
	 */
	private TaskManager tasks;
	
	/**
	 * Lista wyszukiwa� zapisanych przez u�ytkownika
	 */
	private ArrayList<String> toSearch;
	
	/**
	 * Pole informuj�ce o tym czy klasa ma zaktualizowa� dane
	 */
	private boolean updateData = false;
	
	/**
	 * Funkcja ma za zadanie zwr�ci� adres strony URL rozpoczynaj�cej prac� BuScrappera.
	 * @return adres URL strony rozpoczynaj�cej prac� BuSrappera.
	 */
	public String getStartPageURL()
	{
		return startPageURL;
	}
	
	/**
	 * Zwraca informacj� logiczn� okre�laj�c� czy dane powinny zosta� zaktualizowane.
	 * @return informacja logiczna okre�laj�ca czy dane powinny zosta� zaktualizowane
	 */
	public boolean getUpdateData()
	{
		return updateData;
	}
	
	/**
	 * Funkcja zwraca map� z zebranymi wyra�eniami XPath dla w�tk�w wy�uskuj�cych informacje.
	 * @return mapa zawieraj�ca zebrane wyra�enia XPath.
	 */
	public HashMap<String,String> getXPaths()
	{
		log4j.info("Zwracam tablic� XPath!");
		return XPaths;
	}
	
	/**
	 * Funkcja zwraca list� z wyszukiwaniami, kt�re b�dzie wykonywa� Browser, sprawdzenie
	 * poprawno�ci spada na implementacj� obiektu Browser.
	 * @return lista z zebranymi wyszukiwaniami
	 */
	public ArrayList<String> getToSerach()
	{
		log4j.info("Zwracam tablic� toSearch!");
		return toSearch;
	}
	
	/**
	 * Funkcja ma za zadanie odpowiednio przypisa� z pliku konfiguracjnego wszystkie
	 * linijki konfiguracyjne
	 */
	private void parseInfo()
	{
		//Zmienimy tutaj plik na form� XML i b�dziemy czyta� XPathem po prostu
		File conf = new File(configurationFileName);
		
		ArrayList<String> toCheck = new ArrayList<String>();
		HashMap<String,String> xPaths = new HashMap<String,String>();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(conf),"UTF-8"));
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
		} catch (IOException e) {
			log4j.error("Nie uda�o si� przeczyta� z pliku XPath!"+e.getMessage());
		} finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log4j.error("Niepoprawnie zamkni�ty strumie�:"+e.getMessage());
				}
			}
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("PAGEURL="))
			{
				startPageURL = s.substring(8);
				
				log4j.info("Znalaz�em URL!"+startPageURL);
			}
			else if(s.startsWith("UPDATE="))
			{
				String update = s.substring(7);
				if(update.equals("TRUE"))
				{
					updateData = true;
				}
				log4j.info("Wiem czy aktualizowa�!"+update);
			}
		    else if(s.startsWith("XPATHNAZWA="))
			{
				xPaths.put("buStopName", s.substring(11));
				log4j.info("Uda�o mi si� wyodr�bni� XPath!"+xPaths.get("buStopName"));
			}
			else if(s.startsWith("XPATHNUMER="))
			{
				xPaths.put("lineNumber", s.substring(11));
				log4j.info("Uda�o mi si� wyodr�bni� XPath!"+xPaths.get("lineNumber"));
			}
			else if(s.startsWith("XPATHCZASY="))
			{
				xPaths.put("hours", s.substring(11));
				log4j.info("Uda�o mi si� wyodr�bni� XPath!"+xPaths.get("hours"));
			}
			else if(s.startsWith("XPATHDIREC="))
			{
				xPaths.put("direction", s.substring(11));
				log4j.info("Uda�o mi si� wyodr�bni� XPath!"+xPaths.get("direction"));
			}
		}
		
		this.XPaths = xPaths;
	}
	
	/**
	 * Funkcja ma za zadanie wyci�gn�� z pliku konfiguracyjnego odpowiednie dane oraz na
	 * ich podstawie utworzy� kolejk� zada�
	 */
	private void prepareTasks()
	{
		File conf = new File(configurationFileName);
		
		ArrayList<String> toCheck = new ArrayList<String>();
		String[] taskDetails = {"","","","",""};
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(conf),"UTF-8"));
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
			
		} catch (IOException e) {
			log4j.error("Nie uda�o si� przeczyta� z pliku XPath!"+e.getMessage());
		} finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log4j.error("Niepoprawnie zamkni�ty strumie�:"+e.getMessage());
				}
			}
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("DOWNLOADMETHOD="))
			{
				taskDetails[0] = s.substring(15);
				log4j.
				info("Uda�o mi si� wyodr�bni� szczeg� zadania - metod� protoko�u!"+
						taskDetails[0]);
			}
			else if(s.startsWith("STARTURL="))
			{
				taskDetails[1] = s.substring(9);
				log4j.
				info("Uda�o mi si� wyodr�bni� szczeg� zadania - adres url!"+
						taskDetails[1]);
			}
			else if(s.startsWith("ZAKRESLINI="))
			{
				taskDetails[2] = s.substring(11);
				log4j.
				info("Uda�o mi si� wyodr�bni� szczeg� zadania - zakres linii!"+
						taskDetails[2]);
			}
			else if(s.startsWith("MAXPRZYSTANKOW="))
			{
				taskDetails[3] = s.substring(15);
				log4j.
				info("Uda�o mi si� wyodr�bni� szczeg� zadania - maksymalna liczba przystank�w!"+
						taskDetails[3]);
			}
			else if(s.startsWith("MAXKIERUNKOW="))
			{
				taskDetails[4] = s.substring(13);
				log4j.
				info("Uda�o mi si� wyodr�bni� szczeg� zadania - maksymalna liczba kierunk�w!"+
						taskDetails[4]);
			}
		}

		//Wyodr�bniamy zarkes linii
		int separatorIndex = taskDetails[2].indexOf(":");

		int startLine = Integer.parseInt(taskDetails[2].
				substring(0, separatorIndex));
		int endLine = Integer.parseInt(taskDetails[2].
				substring(separatorIndex+1, taskDetails[2].length()));
				
		for(int i = startLine; i <= endLine; ++i)
		{
			tasks.put(new Task(i,""+i,taskDetails[3],taskDetails[4],taskDetails[0],
					taskDetails[1]));
		}
		

	}
	
	/**
	 * Funkcja ma za zadanie wyodr�bni� z pliku konfiguracyjnego wszystkie wyszukiwania
	 * i wstawi� je do listy wyszukiwa� dla Browser
	 */
	private void preapareSearch()
	{
		File conf = new File(configurationFileName);
		
		ArrayList<String> search = new ArrayList<String>();
		ArrayList<String> toCheck = new ArrayList<String>();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(conf),"UTF-8"));
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
			
		} catch (IOException e) {
			log4j.error("Nie uda�o si� przeczyta� z pliku XPath!");
		} finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log4j.error("Niepoprawnie zamkni�ty strumie�:"+e.getMessage());
				}
			}
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("SEARCH="))
			{
				String searchLine = s.substring(7);
				search.add(searchLine);
				log4j.
				info("Uda�o mi si� wyodr�bni� szczeg� zadania -- linijka wyszukiwa�!"+
						searchLine);
			}
		}
		
		toSearch = search;
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest przypisanie nazwy pliku
	 * konfiguracyjnego, referencji do obiektu TaskManager.
	 * @param configurationFileName nazwa pliku konfiguracyjnego
	 * @param tasks obiekt odpowiedzialny za obs�ug� zada�
	 */
	Configurator(String configurationFileName,TaskManager tasks)
	{
		log4j.info("Konfigurator rozpoczyna prac�!");
		
		this.configurationFileName = configurationFileName;
		
		File conf = new File(configurationFileName);
		
		if(!conf.exists())
		{
			log4j.info("Plik konfiguracyjny nie istnieje!");
			return;
		}
		else if(!conf.canRead())
		{
			log4j.info("Nie mog� czyta� z pliku konfiguracyjnego!");
			return;
		}
		//Przygotowanie wst�pne zapyta�
		this.tasks = tasks;

		//Przygotowujemy XPath
		parseInfo();
		//Przygotowujemy zapytania dla downloader�w
		prepareTasks();
		//przygotowujemy zapytania do wyszukiwania
		preapareSearch();
	}
}
