package pl.edu.agh.kis;

import java.util.ArrayList;

/**
 * Klasa ma udost�pnia� funkcjonalno�� wyszukiwarki z zesk�adowanych przez BuScrappera danych
 * wykorzystuje funkcjonalno�ci dw�ch obiekt�w pomocniczych
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class Browser {

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
			return false;
		}
	}
	
	/**
	 * Funkcja ma za zadanie zada� wyszukania, kt�re zosta�y podane przez u�ytkownika
	 * oraz posiadaj� poprawn� form�
	 * @param search Lista wyrzukiwa� zadanych przez u�ytkownika
	 */
	public void serch(ArrayList<String> search)
	{
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
					
					if(!answer.toString().equals(""))
					{
						System.out.println("Mo�esz skorzysta� z linii: "+l.substring(0, 3));
						System.out.println(answer.toString()+"\n");
	
					}
				}
			}
			else
			{
				System.out.println("Nie znaleziono bezpo�redniego po��czenia pomi�dzy przystankami");
			}
		
		}
	}
}
