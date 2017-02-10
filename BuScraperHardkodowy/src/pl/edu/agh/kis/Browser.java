package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;

/**
 * Klasa ma udost�pnia� funkcjonalno�� wyszukiwarki z zesk�adowanych przez BuScrappera danych
 * wykorzystuje funkcjonalno�ci dw�ch obiekt�w pomocniczych LineBrowser oraz HourBrowser.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class Browser {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(Browser.class.getName());
	
	/**
	 * Obiekt umo�liwiaj�cy wyszukiwanie linijek z czasami
	 */
	private HourBrowser hourBrowser = new HourBrowser();
	
	/**
	 * Obiekt umo�liwiaj�cy wyszukiwanie poszczeg�lnych linii
	 */
	private LineBrowser lineBrowser = new LineBrowser();
	
	/**
	 * Zadaniem funkcji jest sprawdzenie poprawno�ci formatu zadanego wyszukania
	 * @param searchLine wyszukanie, kt�re ma zosta� sprawdzone
	 * @return informacja o tym czy wyszukanie ma poprawny format
	 */
	private boolean validateSerachLine(String searchLine)
	{
		String[] splited = searchLine.split(":");
		
		if(splited.length == 6)
		{
			return true;
		}
		else
		{
			log4j.warn("Linia nie przesz�a walidacji:"+searchLine);
			return false;
		}
	}
	
	/**
	 * Funkcja ma za zadanie zada� wyszukania, kt�re zosta�y podane przez u�ytkownika
	 * sprawdzi� poprawno�� ka�dego z nich, nast�pnie przy u�yciu LineBrowser znale��
	 * wszystkie linie ��cz�ce przystanki podane w wyszukaniu, a przy pomocy HourBrowser
	 * wybra� odpowiednie godziny i wypisa� je na standardowe wyj�cie.
	 * @param search Lista wyrzukiwa� otrzymana od obiektu Configurator
	 */
	public void serch(ArrayList<String> search)
	{
		//TODO tutaj musimy zamieni�, �eby zamiast po prostu wypisywania, to nam przygotowywa�o
		//odpowiedz stringBuilderem i myk
		for(String u : search)
		{
			if(!validateSerachLine(u))
			{
				break;
			}
			
			String[] splited = u.split(":");
			
			String s1 = splited[0];
			String s2 = splited[1];
			int typeOfDay = Integer.parseInt(splited[2]); // 0 - powszedni
			int hour = Integer.parseInt(splited[3]);
			int minutes = Integer.parseInt(splited[4]);
			int maxTime = Integer.parseInt(splited[5]);
		
			ArrayList<String> lines = new ArrayList<String>();
			
			if(lineBrowser.searchConnectingLines("buStops/"+s1, "buStops/"+s2))
			{
				for(String l : lineBrowser.getLines())	
				{
					lines.add(l);
				}	
				
				for(String l : lines)
				{
					hourBrowser.searchHours(l+"/"+s1, l+"/"+s2, 
							hour, minutes, maxTime, typeOfDay);
					
					StringBuilder answer = new StringBuilder();
					
					for(String s : hourBrowser.getHours())
					{
						answer.append(s+"\n");
					}
										
					//Wypisanie wynik�w
					if(!answer.toString().equals(""))
					{
						System.out.println("Dla przystank�w: "+s1+" i "+s2);

						if(l.substring(0, 3).matches("^\\d\\d\\d"))
						{
							System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 3));

						}
						else if(l.substring(0, 2).matches("^\\d\\d"))
						{
							System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 2));
						}
						else if(l.substring(0, 1).matches("^\\d"))
						{
							System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 1));
						}
						System.out.println(answer.toString());
					}
				}
			}
			else
			{
				System.out.println("Dla przystank�w: "+s1+" i "+s2);
				System.out.println("Nie znaleziono bezpo�redniego po��czenia pomi�dzy przystankami");
			}
		}
	}
}