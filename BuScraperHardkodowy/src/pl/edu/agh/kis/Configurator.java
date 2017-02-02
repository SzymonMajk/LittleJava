package pl.edu.agh.kis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * G�ownym zadaniem klasy jest odpowiednia analiza plik�w konfiguracyjnych, uzupe�nianych
 * przez u�ytkownika, a nast�pnie wyodr�bnienie wszystkich informacji potrzebnych dla
 * pozosta�ych komponent�w BuScrappera oraz mo�liwo�� prostego udost�pniania ich poprzez
 * funkcje publiczne get
 * Korzysta z plik�w w katalogu Conf
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class Configurator {

	/**
	 * W�asny system log�w
	 */
	private Logger configuratorLogger;
	
	/**
	 * Pole przechowuj�ce nazw� pliku konfiguracyjnego
	 */
	private String configurationFileName;
	
	/**
	 * Adres URL strony pocz�tkowej
	 */
	private String startPageURL;
	
	/**
	 * Pole przechowuj�ce tablic� zapyta� XPath
	 */
	private String[] XPaths;
	
	/**
	 * Kolejka blokuj�ca przechowywuj�ca zadania
	 */
	private LinkedList<Task> tasks;
	
	/**
	 * Lista wyszukiwa� zapisanych przez u�ytkownika
	 */
	private ArrayList<String> toSearch;
	
	/**
	 * Pole informuj�ce o tym czy klasa ma zaktualizowa� dane
	 */
	private boolean updateData = false;
	
	/**
	 * Funkcja ma za zadanie zwr�ci� adres strony URL rozpoczynaj�cej prac�
	 * @return adres URL strony rozpoczynaj�cej prac�
	 */
	public String getStartPageURL()
	{
		return startPageURL;
	}
	
	/**
	 * Zwraca informacj� o tym czy dane powinny by� zaktualizowane otrzyman� z pliku
	 * konfiguracyjnego
	 * @return informacja o tym czy dane powinny by� zaktualizowane
	 */
	public boolean getUpdateData()
	{
		return updateData;
	}
	
	/**
	 * Funkcja ma zwraca� tablic� z XPath'ami pochodz�cymi z pliku konfiguracyjnego
	 * @return tablica zawieraj�ca �cie�ki XPath dla snatchThread�w
	 */
	public String[] getXPaths()
	{
		configuratorLogger.info("Zwracam tablic� XPath!");
		configuratorLogger.execute();
		return XPaths;
	}
	
	/**
	 * Funkcja zwraca list� z zapytaniami, kt�re b�dzie wykonywa� Browser
	 * sprawdzenie poprawno�ci spoczywa na Browser
	 * @return lista z linijkami kt�re nale�y wyszuka� dla Browser
	 */
	public ArrayList<String> getToSerach()
	{
		configuratorLogger.info("Zwracam tablic� toSearch!");
		configuratorLogger.execute();
		return toSearch;
	}
	
	/**
	 * Funkcja ma za zadanie odpowiednio przypisa� z pliku konfiguracjnego wszystkie
	 * linijki z wyra�eniami XPath
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
			configuratorLogger.error("Nie uda�o si� przeczyta� z pliku XPath!");
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("PAGEURL="))
			{
				startPageURL = s.substring(8);
				
				configuratorLogger.info("Znalaz�em URL!",startPageURL);
			}
			else if(s.startsWith("UPDATE="))
			{
				String update = s.substring(7);
				if(update.equals("TRUE"))
				{
					updateData = true;
				}
				configuratorLogger.info("Wiem czy aktualizowa�!",update);
			}
		    else if(s.startsWith("XPATHNAZWA="))
			{
				xPaths[0] = s.substring(11);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� XPath!",xPaths[0]);
			}
			else if(s.startsWith("XPATHNUMER="))
			{
				xPaths[1] = s.substring(11);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� XPath!",xPaths[1]);
			}
			else if(s.startsWith("XPATHCZASY="))
			{
				xPaths[2] = s.substring(11);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� XPath!",xPaths[2]);
			}
			else if(s.startsWith("XPATHDIREC="))
			{
				xPaths[3] = s.substring(11);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� XPath!",xPaths[3]);
			}
			else
			{
				configuratorLogger.warning("W tym kontekcie niezrozumia�a liijka konfiguracyjna",s);
			}
		}
		
		XPaths = xPaths;
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
			configuratorLogger.error("Nie uda�o si� przeczyta� z pliku XPath!");
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("DOWNLOADMETHOD="))
			{
				taskDetails[0] = s.substring(15);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� szczeg� zadania!",taskDetails[0]);
			}
			else if(s.startsWith("HOSTNAME="))
			{
				taskDetails[1] = s.substring(9);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� szczeg� zadania!",taskDetails[1]);
			}
			else if(s.startsWith("ZAKRESLINI="))
			{
				taskDetails[2] = s.substring(11);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� szczeg� zadania!",taskDetails[2]);
			}
			else if(s.startsWith("MAXPRZYSTANKOW="))
			{
				taskDetails[3] = s.substring(15);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� szczeg� zadania!",taskDetails[3]);
			}
			else if(s.startsWith("MAXKIERUNKOW="))
			{
				taskDetails[4] = s.substring(13);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� szczeg� zadania!",taskDetails[4]);
			}
		}
		
		//Wyodr�bniamy zarkes linii
		int separatorIndex = taskDetails[2].indexOf(":");

		int startLine = Integer.parseInt(taskDetails[2].substring(0, separatorIndex));
		int endLine = Integer.parseInt(taskDetails[2].substring(separatorIndex+1, taskDetails[2].length()));
				
		for(int i = startLine; i <= endLine; ++i)
		{
			tasks.add(new Task(""+i,taskDetails[3],taskDetails[4],taskDetails[0],taskDetails[1]));
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
			configuratorLogger.error("Nie uda�o si� przeczyta� z pliku XPath!");
		}
		
		for(String s : toCheck)
		{
			if(s.startsWith("SEARCH="))
			{
				String searchLine = s.substring(7);
				search.add(searchLine);
				configuratorLogger.info("Uda�o mi si� wyodr�bni� szczeg� zadania!",searchLine);
			}
		}
		
		toSearch = search;
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest ustalenie pocz�tkowego stanu
	 * Configurator'a, pozwalaj�cy dodatkowo na ustalenie sposobu wysy�ania log�w
	 * @param configurationFileName nazwa �cie�ki pliku konfiguracyjnego
	 * @param appender obiekt s�u��cy do obs�ugi systemu log�w
	 */
	Configurator(String configurationFileName,LinkedList<Task> tasks, Appends  appender)
	{
		configuratorLogger = new Logger();
		configuratorLogger.changeAppender(appender);
		configuratorLogger.info("Configurator rozpoczyna prac�!");
		
		this.configurationFileName = configurationFileName;
		
		File conf = new File(configurationFileName);
		
		if(!conf.exists())
		{
			configuratorLogger.info("Plik konfiguracyjny nie istnieje!");
			return;
		}
		else if(!conf.canRead())
		{
			configuratorLogger.info("Nie mog� czyta� z pliku konfiguracyjnego!");
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
		
		configuratorLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest ustalenie pocz�tkowego stanu
	 * Configurator'a
	 * @param configurationFileName nazwa �cie�ki pliku konfiguracyjnego
	 */
	Configurator(String configurationFileName, LinkedList<Task> tasks)
	{
		this(configurationFileName,tasks,new NullAppender());
	}
	
}
