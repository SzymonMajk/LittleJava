package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * G³ownym zadaniem klasy jest odpowiednia analiza plików konfiguracyjnych, uzupe³nianych
 * przez u¿ytkownika, a nastêpnie przygotowanie do prostego udostêpnienia dla DownloadThread
 * oraz SnatchThread tych danych, które je interesuj¹. Musi sam utworzyæ kolejkê kolejnych
 * zapytañ GET/POST, które bêdzie udostêpnia³ dla DownloadThread, wyodrêbniæ grupê wyra¿eñ
 * XPath, dla SnatchThread, w zwi¹zku z mo¿liwoœci¹ istnienia wiêcej ni¿ jednego w¹tku
 * ka¿dego rodzaju, operacje pobrania nowego zapytania powinny byæ synchronizowane
 * Korzysta z plików w katalogu Conf
 * @author Szymon Majkut
 * @version 1.1b
 *
 */
public class Configurator {

	/**
	 * W³asny system logów
	 */
	private Logger configuratorLogger;
	
	/**
	 * Pole przechowuj¹ce tablicê zapytañ XPath
	 */
	private String[] XPaths;
	
	/**
	 * Kolejka blokuj¹ca przechowywuj¹ca zadania
	 */
	private LinkedList<Task> tasks;
	
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
	 * Funkcja ma za zadanie odpowiednio przypisaæ dla Configuratora tablicê XPath
	 * @param configurationFileName nazwa pliku konfiguracyjnego
	 */
	private void parseXPath(String configurationFileName)
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
			if(s.startsWith("XPATHNAZWA="))
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
	 * Funkcja przygotowania zadania z pliku konfiguracyjnego
	 * @param configurationFileName nazwa pliku konfiguracyjnego
	 */
	private void prepareTasks(String configurationFileName)
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
			else
			{
				configuratorLogger.warning("W tym kontekcie niezrozumia³a liijka konfiguracyjna",s);
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
		parseXPath(configurationFileName);
		//Przygotowujemy zapytania dla downloaderów
		prepareTasks(configurationFileName);
		
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
