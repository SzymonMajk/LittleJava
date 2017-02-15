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
 * Klasa odpowiedzialna za wyszukiwanie linii ��cz�ch podane przystanki bior�c pod uwag�
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
	 *  Pole przechowuj�ce wyodr�bnione linie
	 */
	private ArrayList<String> lines;
	
	/**
	 * Funkcja zwracaj�ca przechowywane przez klas� linie.
	 * @return lista linii przechowywanych przez klas�
	 */
	public ArrayList<String> getLines()
	{
		return lines;
	}
	
	/**
	 * Funkcja odpowiada za wyci�gni�cie danych z podanego w argumencie pliku,
	 * danymi s� numery linii wraz z okre�lonym kierunkiem
	 * @param fileName nazwa pliku z kt�rego wyodr�bniamy dane
	 * @return lista linii wyodr�bnionych z podanego w argumencie pliku
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
				log4j.info("Wydoby�em linie dla przystanku"+fileName);
		} catch (FileNotFoundException e) {
			log4j.warn("Nie znaleziono pliku: "+fileName);
		} catch (IOException e) {
			log4j.warn("Wyst�pi� wyj�tek podczas czytania z pliku: "+fileName);
		}
			
		return result;
	}
	
	/**
	 * Funkcja ma za zadanie wybra� tylko te linie, kt�re wyst�puj� w obu listach z argument�w
	 * @param first pierwsza lista linii
	 * @param second druga lista linii
	 * @return lista linii wyst�puj�cych w obu listach z argument�w
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
	 * Funkcja ma za zadanie wyodr�bni� linie ��cz�ce dwa konkretne przystanki podane
	 * w argumentach funkcji.
	 * @param firstBuStopName przystanek pocz�tkowy
	 * @param secondBuStopName przystanek ko�cowy
	 * @return zwraca informacj� czy uda�o si� wyodr�bni� przynajmniej jeden przystanek
	 */
	public boolean searchConnectingLines(String firstBuStopName, String secondBuStopName)
	{
		lines = takeShared(readLinesFromFile(firstBuStopName),
				readLinesFromFile(secondBuStopName));
		
		return !lines.isEmpty();
	}
}