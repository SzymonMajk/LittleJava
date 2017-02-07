package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za wyszukiwanie linii ��cz�ch podane przystanki bior�c pod uwag�
 * kierunek.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class LineBrowser {

	/**
	 * W�asny system log�w
	 */
	private Logger browserLogger;
	
	
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
		
		if(file.exists())
		{
			BufferedReader buffReader;
				
			try {
				buffReader = new BufferedReader(new FileReader(file));
				String line = "";
				
				while((line = buffReader.readLine()) != null)
				{
					result.add(line);
				}
				
				buffReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			browserLogger.warning("Nie ma pliku "+fileName);
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
		
		browserLogger.execute();
		return !lines.isEmpty();
	}
	
	/**
	 * Konstruktor kt�rego zadaniem jest przypisanie domy�lnego systemu sposobu sk�adowania
	 * log�w.
	 */
	public LineBrowser()
	{
		browserLogger = new Logger();
		browserLogger.changeAppender(new FileAppender("LineBrowser"));
	}
}