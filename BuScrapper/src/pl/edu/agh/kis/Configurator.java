package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * G�ownym zadaniem klasy jest odpowiednia analiza plik�w konfiguracyjnych, uzupe�nianych
 * przez u�ytkownika, a nast�pnie przygotowanie do prostego udost�pnienia dla DownloadThread
 * oraz SnatchThread tych danych, kt�re je interesuj�. Musi sam utworzy� kolejk� kolejnych
 * zapyta� GET/POST, kt�re b�dzie udost�pnia� dla DownloadThread, wyodr�bni� grup� wyra�e�
 * XPath, dla SnatchThread, w zwi�zku z mo�liwo�ci� istnienia wi�cej ni� jednego w�tku
 * ka�dego rodzaju, operacje pobrania nowego zapytania powinny by� synchronizowane
 * Korzysta z plik�w w katalogu Conf
 * @author Szymon Majkut
 * @version 1.1b
 *
 */
public class Configurator {

	/**
	 * W�asny system log�w
	 */
	private Logger configuratorLogger;
	
	/**
	 * Pole przechowuj�ce tablic� zapyta� XPath
	 */
	private String[] XPaths;
	
	/**
	 * Kolejka blokuj�ca przechowywuj�ca zapytania
	 */
	private BlockingQueue<String> requests;
	
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
	 * Funkcja ma zwraca� referencj� z zapytaniami, utworzonymi wed�ug wytycznych
	 * pliku konfiguracyjnego
	 * @return referencja do kolejki z zapytaniami
	 */
	public BlockingQueue<String> getRequests()
	{
		return requests;
	}
	
	/**
	 * Funkcja ma za zadanie odpowiednio przypisa� dla Configuratora tablic� XPath
	 * @param configurationFileName nazwa pliku konfiguracyjnego
	 */
	private void parseXPath(String configurationFileName)
	{
		File conf = new File(configurationFileName);
		
		ArrayList<String> toCheck = new ArrayList<String>();
		String[] xPaths = {"","",""};
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
			if(s.startsWith("XPATHNAZWA="))
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
			else
			{
				configuratorLogger.warning("W tym kontekcie niezrozumia�a liijka konfiguracyjna",s);
			}
		}
		
		XPaths = xPaths;
	}
	
	/**
	 * Funkcja ma za zadanie utworzy� zapytania zgodnie z wytycznymi z pliku konfiguracyjnego,
	 * a nast�pnie zesk�adowa� je w kolejce blokuj�cej z zapytaniami
	 * @param configurationFileName nazwa pliku konfiguracyjnego
	 */
	private void  prepareRequests(String configurationFileName)
	{
		configuratorLogger.info("Utworzy�em zapytania dla linii...");
		//a jak co� p�jdzie nie tak
		configuratorLogger.warning("Nie uda�o mi si� utworzy� zapyta� dla linii!");

	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest ustalenie pocz�tkowego stanu
	 * Configurator'a, pozwalaj�cy dodatkowo na ustalenie sposobu wysy�ania log�w
	 * @param configurationFileName nazwa �cie�ki pliku konfiguracyjnego
	 * @param appender obiekt s�u��cy do obs�ugi systemu log�w
	 */
	Configurator(String configurationFileName, Appends  appender)
	{
		configuratorLogger = new Logger();
		configuratorLogger.changeAppender(appender);
		configuratorLogger.info("Configurator rozpoczyna prac�!");
		
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
			
		//Przygotowujemy XPath
		parseXPath(configurationFileName);
		//Przygotowujemy zapytania dla downloader�w
		prepareRequests(configurationFileName);
		
		configuratorLogger.execute();
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest ustalenie pocz�tkowego stanu
	 * Configurator'a
	 * @param configurationFileName nazwa �cie�ki pliku konfiguracyjnego
	 */
	Configurator(String configurationFileName)
	{
		//this(configurationFileName,new FileAppender("Configurator"));
		this(configurationFileName,new NullAppender());
		//dop�ki nie wyczaje, dlaczego w logach w�tk�w mi go wywala...
	}
	
}
