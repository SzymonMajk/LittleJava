package pl.edu.agh.kis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * G³ownym zadaniem klasy jest odpowiednia analiza plików konfiguracyjnych, uzupe³nianych
 * przez u¿ytkownika, a nastêpnie wyodrêbnienie wszystkich informacji potrzebnych dla
 * pozosta³ych komponentów BuScrappera oraz mo¿liwoœæ prostego udostêpniania ich poprzez
 * funkcje publiczne get
 * Korzysta z plików w katalogu Conf
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class Configurator {

	/**
	 * W³asny system logów
	 */
	private Logger configuratorLogger;
	
	/**
	 * Pole przechowuj¹ce nazwê pliku konfiguracyjnego
	 */
	private String configurationFileName;
	
	/**
	 * Adres URL strony pocz¹tkowej
	 */
	private String startPageURL;
	
	/**
	 * Pole przechowuj¹ce tablicê zapytañ XPath
	 */
	private String[] XPaths;
	
	/**
	 * Kolejka blokuj¹ca przechowywuj¹ca zadania
	 */
	private LinkedList<Task> tasks;
	
	/**
	 * Lista wyszukiwañ zapisanych przez u¿ytkownika
	 */
	private ArrayList<String> toSearch;
	
	/**
	 * Pole informuj¹ce o tym czy klasa ma zaktualizowaæ dane
	 */
	private boolean updateData = false;
	
	/**
	 * Funkcja ma za zadanie zwróciæ adres strony URL rozpoczynaj¹cej pracê
	 * @return adres URL strony rozpoczynaj¹cej pracê
	 */
	public String getStartPageURL()
	{
		return startPageURL;
	}
	
	/**
	 * Zwraca informacjê o tym czy dane powinny byæ zaktualizowane otrzyman¹ z pliku
	 * konfiguracyjnego
	 * @return informacja o tym czy dane powinny byæ zaktualizowane
	 */
	public boolean getUpdateData()
	{
		return updateData;
	}
	
	/**
	 * Funkcja ma zwracaæ tablicê z XPath'ami pochodz¹cymi z pliku konfiguracyjnego
	 * @return tablica zawieraj¹ca œcie¿ki XPath dla snatchThreadów
	 */
	public String[] getXPaths()
	{
		configuratorLogger.info("Zwracam tablicê XPath!");
		configuratorLogger.execute();
		return XPaths;
	}
	
	/**
	 * Funkcja zwraca listê z zapytaniami, które bêdzie wykonywa³ Browser
	 * sprawdzenie poprawnoœci spoczywa na Browser
	 * @return lista z linijkami które nale¿y wyszukaæ dla Browser
	 */
	public ArrayList<String> getToSerach()
	{
		configuratorLogger.info("Zwracam tablicê toSearch!");
		configuratorLogger.execute();
		return toSearch;
	}
	
	/**
	 * Funkcja ma za zadanie odpowiednio przypisaæ z pliku konfiguracjnego wszystkie
	 * linijki z wyra¿eniami XPath
	 */
	private void parseInfo()
	{
		File conf = new File(configurationFileName);
		
		ArrayList<String> toCheck = new ArrayList<String>();
		String[] xPaths = {"","","",""};
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(conf)));
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
			
		} catch (IOException e) {
			configuratorLogger.error("Nie uda³o siê przeczytaæ z pliku XPath!");
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("PAGEURL="))
			{
				startPageURL = s.substring(8);
				
				configuratorLogger.info("Znalaz³em URL!",startPageURL);
			}
			else if(s.startsWith("UPDATE="))
			{
				String update = s.substring(7);
				if(update.equals("TRUE"))
				{
					updateData = true;
				}
				configuratorLogger.info("Wiem czy aktualizowaæ!",update);
			}
		    else if(s.startsWith("XPATHNAZWA="))
			{
				xPaths[0] = s.substring(11);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ XPath!",xPaths[0]);
			}
			else if(s.startsWith("XPATHNUMER="))
			{
				xPaths[1] = s.substring(11);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ XPath!",xPaths[1]);
			}
			else if(s.startsWith("XPATHCZASY="))
			{
				xPaths[2] = s.substring(11);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ XPath!",xPaths[2]);
			}
			else if(s.startsWith("XPATHDIREC="))
			{
				xPaths[3] = s.substring(11);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ XPath!",xPaths[3]);
			}
			else
			{
				configuratorLogger.warning("W tym kontekcie niezrozumia³a liijka konfiguracyjna",s);
			}
		}
		
		XPaths = xPaths;
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
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(conf)));
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
			
		} catch (IOException e) {
			configuratorLogger.error("Nie uda³o siê przeczytaæ z pliku XPath!");
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("DOWNLOADMETHOD="))
			{
				taskDetails[0] = s.substring(15);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ szczegó³ zadania!",taskDetails[0]);
			}
			else if(s.startsWith("HOSTNAME="))
			{
				taskDetails[1] = s.substring(9);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ szczegó³ zadania!",taskDetails[1]);
			}
			else if(s.startsWith("ZAKRESLINI="))
			{
				taskDetails[2] = s.substring(11);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ szczegó³ zadania!",taskDetails[2]);
			}
			else if(s.startsWith("MAXPRZYSTANKOW="))
			{
				taskDetails[3] = s.substring(15);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ szczegó³ zadania!",taskDetails[3]);
			}
			else if(s.startsWith("MAXKIERUNKOW="))
			{
				taskDetails[4] = s.substring(13);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ szczegó³ zadania!",taskDetails[4]);
			}
		}
		
		//Wyodrêbniamy zarkes linii
		int separatorIndex = taskDetails[2].indexOf(":");

		int startLine = Integer.parseInt(taskDetails[2].substring(0, separatorIndex));
		int endLine = Integer.parseInt(taskDetails[2].substring(separatorIndex+1, taskDetails[2].length()));
				
		for(int i = startLine; i <= endLine; ++i)
		{
			tasks.add(new Task(""+i,taskDetails[3],taskDetails[4],taskDetails[0],taskDetails[1]));
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
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(conf)));
			
			String line = "";
			
			while((line = reader.readLine()) != null)
			{
				toCheck.add(line);
			}
			
		} catch (IOException e) {
			configuratorLogger.error("Nie uda³o siê przeczytaæ z pliku XPath!");
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("SEARCH="))
			{
				String searchLine = s.substring(7);
				search.add(searchLine);
				configuratorLogger.info("Uda³o mi siê wyodrêbniæ szczegó³ zadania!",searchLine);
			}
		}
		
		toSearch = search;
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest ustalenie pocz¹tkowego stanu
	 * Configurator'a, pozwalaj¹cy dodatkowo na ustalenie sposobu wysy³ania logów
	 * @param configurationFileName nazwa œcie¿ki pliku konfiguracyjnego
	 * @param appender obiekt s³u¿¹cy do obs³ugi systemu logów
	 */
	Configurator(String configurationFileName,LinkedList<Task> tasks, Appends  appender)
	{
		configuratorLogger = new Logger();
		configuratorLogger.changeAppender(appender);
		configuratorLogger.info("Configurator rozpoczyna pracê!");
		
		this.configurationFileName = configurationFileName;
		
		File conf = new File(configurationFileName);
		
		if(!conf.exists())
		{
			configuratorLogger.info("Plik konfiguracyjny nie istnieje!");
			return;
		}
		else if(!conf.canRead())
		{
			configuratorLogger.info("Nie mogê czytaæ z pliku konfiguracyjnego!");
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
		
		configuratorLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest ustalenie pocz¹tkowego stanu
	 * Configurator'a
	 * @param configurationFileName nazwa œcie¿ki pliku konfiguracyjnego
	 */
	Configurator(String configurationFileName, LinkedList<Task> tasks)
	{
		this(configurationFileName,tasks,new NullAppender());
	}
	
}
