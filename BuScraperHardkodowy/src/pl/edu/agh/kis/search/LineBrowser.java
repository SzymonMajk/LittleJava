package pl.edu.agh.kis.search;

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
 * kierunek. Zawiera funkcj� zwracaj�c� wyniki swojej pracy oraz funkcj� przeprowadzaj�c�
 * wyszukiwania po��cze� pomi�dzy dwoma przystankami uwzgl�dniaj�c kierunek.
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
	private ArrayList<String> linesWithDirections;
	
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
	 * Funkcja zwraca tablic� linii wraz z kierunkami. Dane pochodz� z plik�w
	 * z pomocniczego katalogu buStops.
	 * @return lista linii wraz z kierunkami.
	 */
	public ArrayList<String> getLines()
	{
		return linesWithDirections;
	}
	
	/**
	 * Funkcja ma za zadanie wyodr�bni� linie ��cz�ce dwa konkretne przystanki, poprzez
	 * podane w argumentach funkcji �cie�ki do plik�w pomocniczych zawieraj�cych informacj�
	 * o numerach linii oraz kierunkach linii przeje�dzaj�cych przez przestanek o nazwie
	 * to�samej z nazw� pliku. W tym celu uruchamia funkcj� prywatn� kt�rej zadaniem
	 * jest wyodr�bnienie cz�ci wsp�lnej z plik�w pomocniczych, kt�rych �cie�ki
	 * zostaj� podane w argumentach oraz zwr�cenie wiadomo�ci pusto�ci tej cz�ci wsp�lnej.
	 * @param firstBuStopName �cie�ka do pliku pomocniczego dla pierwszego przystanku.
	 * @param secondBuStopName �cie�ka do pliku pomocniczego dla drugiego przystanku.
	 * @return zwraca prawd�, je�eli cz�� wsp�lna zawarto�ci plik�w pomocniczych jest
	 * 		jest r�na od zera, w przeciwnym wypadku zwraca fa�sz.
	 */
	public boolean searchConnectingLines(String firstBuStopName, String secondBuStopName)
	{
		linesWithDirections = takeShared(readLinesFromFile(firstBuStopName),
				readLinesFromFile(secondBuStopName));
		
		return !linesWithDirections.isEmpty();
	}
}