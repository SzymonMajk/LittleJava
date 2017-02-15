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
 * G³ownym zadaniem klasy jest odpowiednia analiza pliku konfiguracyjnego wype³nionego
 * przez u¿ytkownika, a nastêpnie wyodrêbnienie wszystkich informacji potrzebnych dla
 * pozosta³ych komponentów BuScrappera. Zapewnia równie¿ mo¿liwoœæ prostego udostêpniania
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
	 * Pole przechowuj¹ce nazwê pliku konfiguracyjnego
	 */
	private String configurationFileName;
	
	/**
	 * Adres URL strony pocz¹tkowej
	 */
	private String startPageURL;
	
	/**
	 * Pole przechowuj¹ce mapê z zapytaniami XPath
	 */
	private HashMap<String,String> XPaths = new HashMap<String,String>();
	
	/**
	 * Obiekt zajmuj¹cy siê obs³ug¹ zadañ
	 */
	private TaskManager tasks;
	
	/**
	 * Lista wyszukiwañ zapisanych przez u¿ytkownika
	 */
	private ArrayList<String> toSearch;
	
	/**
	 * Pole informuj¹ce o tym czy klasa ma zaktualizowaæ dane
	 */
	private boolean updateData = false;
	
	/**
	 * Funkcja ma za zadanie zwróciæ adres strony URL rozpoczynaj¹cej pracê BuScrappera.
	 * @return adres URL strony rozpoczynaj¹cej pracê BuSrappera.
	 */
	public String getStartPageURL()
	{
		return startPageURL;
	}
	
	/**
	 * Zwraca informacjê logiczn¹ okreœlaj¹c¹ czy dane powinny zostaæ zaktualizowane.
	 * @return informacja logiczna okreœlaj¹ca czy dane powinny zostaæ zaktualizowane
	 */
	public boolean getUpdateData()
	{
		return updateData;
	}
	
	/**
	 * Funkcja zwraca mapê z zebranymi wyra¿eniami XPath dla w¹tków wy³uskuj¹cych informacje.
	 * @return mapa zawieraj¹ca zebrane wyra¿enia XPath.
	 */
	public HashMap<String,String> getXPaths()
	{
		log4j.info("Zwracam tablicê XPath!");
		return XPaths;
	}
	
	/**
	 * Funkcja zwraca listê z wyszukiwaniami, które bêdzie wykonywa³ Browser, sprawdzenie
	 * poprawnoœci spada na implementacjê obiektu Browser.
	 * @return lista z zebranymi wyszukiwaniami
	 */
	public ArrayList<String> getToSerach()
	{
		log4j.info("Zwracam tablicê toSearch!");
		return toSearch;
	}
	
	/**
	 * Funkcja ma za zadanie odpowiednio przypisaæ z pliku konfiguracjnego wszystkie
	 * linijki konfiguracyjne
	 */
	private void parseInfo()
	{
		//Zmienimy tutaj plik na formê XML i bêdziemy czytaæ XPathem po prostu
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
			log4j.error("Nie uda³o siê przeczytaæ z pliku XPath!"+e.getMessage());
		} finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log4j.error("Niepoprawnie zamkniêty strumieñ:"+e.getMessage());
				}
			}
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("PAGEURL="))
			{
				startPageURL = s.substring(8);
				
				log4j.info("Znalaz³em URL!"+startPageURL);
			}
			else if(s.startsWith("UPDATE="))
			{
				String update = s.substring(7);
				if(update.equals("TRUE"))
				{
					updateData = true;
				}
				log4j.info("Wiem czy aktualizowaæ!"+update);
			}
		    else if(s.startsWith("XPATHNAZWA="))
			{
				xPaths.put("buStopName", s.substring(11));
				log4j.info("Uda³o mi siê wyodrêbniæ XPath!"+xPaths.get("buStopName"));
			}
			else if(s.startsWith("XPATHNUMER="))
			{
				xPaths.put("lineNumber", s.substring(11));
				log4j.info("Uda³o mi siê wyodrêbniæ XPath!"+xPaths.get("lineNumber"));
			}
			else if(s.startsWith("XPATHCZASY="))
			{
				xPaths.put("hours", s.substring(11));
				log4j.info("Uda³o mi siê wyodrêbniæ XPath!"+xPaths.get("hours"));
			}
			else if(s.startsWith("XPATHDIREC="))
			{
				xPaths.put("direction", s.substring(11));
				log4j.info("Uda³o mi siê wyodrêbniæ XPath!"+xPaths.get("direction"));
			}
		}
		
		this.XPaths = xPaths;
	}
	
	/**
	 * Funkcja ma za zadanie wyci¹gn¹æ z pliku konfiguracyjnego odpowiednie dane oraz na
	 * ich podstawie utworzyæ kolejkê zadañ
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
			log4j.error("Nie uda³o siê przeczytaæ z pliku XPath!"+e.getMessage());
		} finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log4j.error("Niepoprawnie zamkniêty strumieñ:"+e.getMessage());
				}
			}
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("DOWNLOADMETHOD="))
			{
				taskDetails[0] = s.substring(15);
				log4j.
				info("Uda³o mi siê wyodrêbniæ szczegó³ zadania - metodê protoko³u!"+
						taskDetails[0]);
			}
			else if(s.startsWith("STARTURL="))
			{
				taskDetails[1] = s.substring(9);
				log4j.
				info("Uda³o mi siê wyodrêbniæ szczegó³ zadania - adres url!"+
						taskDetails[1]);
			}
			else if(s.startsWith("ZAKRESLINI="))
			{
				taskDetails[2] = s.substring(11);
				log4j.
				info("Uda³o mi siê wyodrêbniæ szczegó³ zadania - zakres linii!"+
						taskDetails[2]);
			}
			else if(s.startsWith("MAXPRZYSTANKOW="))
			{
				taskDetails[3] = s.substring(15);
				log4j.
				info("Uda³o mi siê wyodrêbniæ szczegó³ zadania - maksymalna liczba przystanków!"+
						taskDetails[3]);
			}
			else if(s.startsWith("MAXKIERUNKOW="))
			{
				taskDetails[4] = s.substring(13);
				log4j.
				info("Uda³o mi siê wyodrêbniæ szczegó³ zadania - maksymalna liczba kierunków!"+
						taskDetails[4]);
			}
		}

		//Wyodrêbniamy zarkes linii
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
	 * Funkcja ma za zadanie wyodrêbniæ z pliku konfiguracyjnego wszystkie wyszukiwania
	 * i wstawiæ je do listy wyszukiwañ dla Browser
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
			log4j.error("Nie uda³o siê przeczytaæ z pliku XPath!");
		} finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					log4j.error("Niepoprawnie zamkniêty strumieñ:"+e.getMessage());
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
				info("Uda³o mi siê wyodrêbniæ szczegó³ zadania -- linijka wyszukiwañ!"+
						searchLine);
			}
		}
		
		toSearch = search;
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest przypisanie nazwy pliku
	 * konfiguracyjnego, referencji do obiektu TaskManager.
	 * @param configurationFileName nazwa pliku konfiguracyjnego
	 * @param tasks obiekt odpowiedzialny za obs³ugê zadañ
	 */
	Configurator(String configurationFileName,TaskManager tasks)
	{
		log4j.info("Konfigurator rozpoczyna pracê!");
		
		this.configurationFileName = configurationFileName;
		
		File conf = new File(configurationFileName);
		
		if(!conf.exists())
		{
			log4j.info("Plik konfiguracyjny nie istnieje!");
			return;
		}
		else if(!conf.canRead())
		{
			log4j.info("Nie mogê czytaæ z pliku konfiguracyjnego!");
			return;
		}
		//Przygotowanie wstêpne zapytañ
		this.tasks = tasks;

		//Przygotowujemy XPath
		parseInfo();
		//Przygotowujemy zapytania dla downloaderów
		prepareTasks();
		//przygotowujemy zapytania do wyszukiwania
		preapareSearch();
	}
}
