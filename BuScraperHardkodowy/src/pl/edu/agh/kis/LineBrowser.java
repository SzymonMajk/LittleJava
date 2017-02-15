package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za wyszukiwanie linii ³¹cz¹ch podane przystanki bior¹c pod uwagê
 * kierunek.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class LineBrowser {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(LineBrowser.class.getName());
	
	
	/**
	 *  Pole przechowuj¹ce wyodrêbnione linie
	 */
	private ArrayList<String> lines;
	
	/**
	 * Funkcja zwracaj¹ca przechowywane przez klasê linie.
	 * @return lista linii przechowywanych przez klasê
	 */
	public ArrayList<String> getLines()
	{
		return lines;
	}
	
	/**
	 * Funkcja odpowiada za wyci¹gniêcie danych z podanego w argumencie pliku,
	 * danymi s¹ numery linii wraz z okreœlonym kierunkiem
	 * @param fileName nazwa pliku z którego wyodrêbniamy dane
	 * @return lista linii wyodrêbnionych z podanego w argumencie pliku
	 */
	private ArrayList<String> readLinesFromFile(String fileName)
	{
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(fileName);
		
		try (BufferedReader buffReader = new BufferedReader(
				new InputStreamReader ( new FileInputStream(file),"UTF-8"))) {
			String line = "";
	
			while((line = buffReader.readLine()) != null)
			{
				result.add(line);
			}
				log4j.info("Wydoby³em linie dla przystanku"+fileName);
		} catch (FileNotFoundException e) {
			log4j.warn("Nie znaleziono pliku: "+fileName);
		} catch (IOException e) {
			log4j.warn("Wyst¹pi³ wyj¹tek podczas czytania z pliku: "+fileName);
		}
			
		return result;
	}
	
	/**
	 * Funkcja ma za zadanie wybraæ tylko te linie, które wystêpuj¹ w obu listach z argumentów
	 * @param first pierwsza lista linii
	 * @param second druga lista linii
	 * @return lista linii wystêpuj¹cych w obu listach z argumentów
	 */
	private ArrayList<String> takeShared(ArrayList<String> first, ArrayList<String> second)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(String s : first)
		{
			if(second.contains(s))
			{
				result.add(s);
				second.remove(s);
			}
		}
		
		for(String s : second)
		{
			if(first.contains(s))
			{
				result.add(s);
			}
		}
		
		return result;
	}
	
	/**
	 * Funkcja ma za zadanie wyodrêbniæ linie ³¹cz¹ce dwa konkretne przystanki podane
	 * w argumentach funkcji.
	 * @param firstBuStopName przystanek pocz¹tkowy
	 * @param secondBuStopName przystanek koñcowy
	 * @return zwraca informacjê czy uda³o siê wyodrêbniæ przynajmniej jeden przystanek
	 */
	public boolean searchConnectingLines(String firstBuStopName, String secondBuStopName)
	{
		lines = takeShared(readLinesFromFile(firstBuStopName),
				readLinesFromFile(secondBuStopName));
		
		return !lines.isEmpty();
	}
}