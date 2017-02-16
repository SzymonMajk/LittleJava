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
 * Klasa której zadaniem jest zwrócenie listy wyodrêbnionych godzin odjazdów z przystanku
 * pocz¹tkowego bior¹c pod uwagê wszystkie otrzymane ograniczenia oraz kierunek przejazdu.
 * Zapewnia metodê zwracaj¹c¹ listê wynikowych godzin wraz z minutami oraz metodê pozwalaj¹c¹
 * na uzyskanie takowych.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class HourBrowser {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(HourBrowser.class.getName());
	
	/**
	 * Lista gotowych wyodrêbnionych godzin
	 */
	private ArrayList<String> hours = new ArrayList<String>();
	
	/**
	 * Funkcja ma za zadanie zczytaæ zawartoœæ pliku podanego w argumencie oraz
	 * zwróciæ tablicê z wszystkimi godzinami odjazdów
	 * @param fileName nazwa pliku dla którego wyci¹gamy informacje
	 * @return wszystkie godziny odjazdów z pliku o zadanej nazwie
	 */
	private ArrayList<String> readLinesFromFile(String fileName)
	{
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(fileName);
						
		try (BufferedReader buffReader = new BufferedReader(
				new InputStreamReader ( new FileInputStream(file),"UTF-8"))) {
			String line = "";
				
			log4j.info("Wydobywam linie dla przystanku"+buffReader.readLine());
				while((line = buffReader.readLine()) != null)
				{
					result.add(line);
				}
		} catch (FileNotFoundException e) {
			log4j.warn("Nie znaleziono pliku: "+fileName);
		} catch (IOException e) {
			log4j.warn("Wyst¹pi³ wyj¹tek podczas czytania z pliku: "+fileName);
		}

		return result;
	}
	
	/**
	 * Funkcja ma za zadanie spradziæ poprawnoœæ danych wydobytych z pliku, sprawdza
	 * czy w ka¿dej linii znajduj¹ siê cztery dwukropki, oraz czy na pocz¹tku linni znajduje
	 * siê liczba, a na koñcu znak dwukropka
	 * @param linesToCheckFormat dane wydobyte z pliku, które maj¹ zostaæ sprawdzone
	 * @return informacja o poprawnoœci formatu wydyobytych danych
	 */
	private boolean checkHoursLineFormat(ArrayList<String> linesToCheckFormat)
	{
		for(String l : linesToCheckFormat)
		{
			if(!l.matches("^\\d.*:$"))
			{
				log4j.warn("Niepoprawny format linijki:"+l);
				return false;
			}
			else if(4 != l.length()-l.replace(":", "").length())
			{
				log4j.warn("Niepoprawny format linijki:"+l);
				return false;
			}
			else if(l.charAt(1) != ':' && l.charAt(2) != ':' )
			{
				log4j.warn("Niepoprawny format linijki:"+l);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Funkcja sprawdzaj¹ca poprawnoœæ wszystkich danych z argumentów i zwracaj¹ca
	 * informacjê o ich poprawnoœci
	 * @param hours lista godzin, sprawdzimy czy nie jest pusta
	 * @param startingHour godzina rozpoczynaj¹ca wyszukiwania
	 * @param startingMinutes minuta rozpoczynaj¹ca wyszukiwania
	 * @param maxTime maksymalna iloœæ godzin wyszukiwania
	 * @param typeOfDay rodzaj dnia w którym wyszukujemy
	 * @return informacja czy podane dane by³y poprawne
	 */
	private boolean validateReachOutTime(ArrayList<String> hours, int startingHour,
			int startingMinutes, int maxTime, int typeOfDay)
	{
		if(hours == null || hours.isEmpty())
		{
			log4j.warn("Godziny by³y puste");
			return false;
		}
		else if(startingHour < 0 || startingHour > 23)
		{
			log4j.warn("Podana godzina wykracza poza dozwolone godziny:"+startingHour);
			return false;
		}
		else if(startingMinutes < 0 || startingMinutes > 59)
		{
			log4j.warn("Podana minuta wykracza poza dozwolone minuty:"+startingMinutes);
			return false;
		}
		else if(maxTime < 0 || maxTime > 23)
		{
			log4j.warn("Podany zakres godzin wykracza poza rozk³ad:"+maxTime);
			return false;
		}
		else if(typeOfDay < 0 || typeOfDay > 2)
		{
			log4j.warn("Podano niepoprawny rodzaj dnia:"+typeOfDay);
			return false;	
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Zadaniem funkcji jest zwrócenie pierwszej godziny, w której dla zadanego rodzaju dnia
	 * wystêpuje jakaœ minuta
	 * @param hours lista wszystkich godzin dla z danego przystanku
	 * @param typeOfDay rodzaj dnia
	 * @return pierwsza godzina, w której dla zadanego rodzaju dnia wystêpuje autobus
	 */
	private String firstLineOfStop(ArrayList<String> hours, int typeOfDay)
	{
		String result = "";
		
		for(String l : hours)
		{
			String[] separatedHourLine = l.split(":");
			
			if(separatedHourLine.length >= typeOfDay + 2)
			{
				result = separatedHourLine[0]+":"+separatedHourLine[typeOfDay+1];
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Zadaniem funkcji jest wyci¹gniêcie odpowiednich linii godzin i minut pod warunkami
	 * zadanymi w argumentach
	 * param hours lista godzin, sprawdzimy czy nie jest pusta
	 * @param startingHour godzina rozpoczynaj¹ca wyszukiwania
	 * @param startingMinutes minuta rozpoczynaj¹ca wyszukiwania
	 * @param maxTime maksymalna iloœæ godzin wyszukiwania
	 * @param typeOfDay rodzaj dnia w którym wyszukujemy
	 * @return lista godzin i minut, które spe³ni³y kryteria z argumentów
	 */
	private ArrayList<String> reachOutTime(ArrayList<String> hours, int startingHour,
			int startingMinutes, int maxTime, int typeOfDay)
	{
		if(!validateReachOutTime(hours,startingHour,
				startingMinutes, maxTime,typeOfDay))
		{
			return new ArrayList<String>();
		}
		
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			for(String l : hours)
			{
				String[] separatedHourLine = l.split(":");
				String hour = separatedHourLine[0];
								
				if((Integer.parseInt(hour) >= startingHour &&
					Integer.parseInt(hour) <= startingHour + maxTime) ||
					(startingHour + maxTime > 24 &&
					Integer.parseInt(hour) <= (startingHour + maxTime)%24 &&
					Integer.parseInt(hour) >= 0))
				{
					if(separatedHourLine.length >= typeOfDay+2)
					{
						String minutes = separatedHourLine[typeOfDay+1];
						
						if(startingHour == Integer.parseInt(hour))
						{
							String[] splitedMinutes = minutes.split(",");
							StringBuilder builder = new StringBuilder();
						
							for(String k : splitedMinutes)
							{
								if(startingMinutes <= Integer.parseInt(k.replace(" ","")))
								{
									builder.append(k+",");
								}
							}
							
							if(!builder.toString().equals(""))
							{
								builder.replace(builder.length()-1, builder.length(), "");
							}
							minutes = builder.toString();
						}
						
						if(!minutes.equals(""))
						{
							result.add(hour+":"+minutes);
						}
					}
				}
			}
		
		} catch (Throwable e) {
			log4j.warn("B³¹d przy parsowaniu reachOut"+e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Zadaniem funkcji jest odrzucenie tych wyników, które posiadaj¹ b³êdny kierunek
	 * @param timeOfStartStop lista czasów dla przystanku pocz¹tkowego
	 * @param timeOfEndStop lista czasów dla przystanku koñcowego
	 * @param firstLineOfFirstStop pierwsza linia przystanku pocz¹tkowego
	 * @param firstLineOfSecondStop pierwsza linia przystanku koñcowego
	 * @return informacja o tym czy kierunek by³ w³aœciwy
	 */
	private boolean checkDirection(ArrayList<String> timeOfStartStop, ArrayList<String> timeOfEndStop,
			String firstLineOfFirstStop, String firstLineOfSecondStop)
	{
		if(timeOfStartStop.isEmpty())
		{
			log4j.warn("Podane godziny nie pasuj¹ do godzin przystanku pocz¹tkowego.");
			return false;
		}
		else if(timeOfEndStop.isEmpty())
		{
			log4j.warn("Podane godziny nie pasuj¹ do godzin przystanku koñcowego.");
			return false;
		}
		else if(firstLineOfFirstStop.equals(""))
		{
			log4j.warn("Pierwsza linia pocz¹tkowego pusta!");
			return false;
		}
		else if(firstLineOfSecondStop.equals(""))
		{
			log4j.warn("Pierwsza linia koñcowego pusta!");
			return false;			
		}
		
		int firstFirstHour = 0;
		int secondFirstHour = 0;
		
		int firstFirstMinute = 0;
		int secondFirstMinute = 0;
		
		String[] firstLineInfo = firstLineOfFirstStop.split(":");
		String[] secondLineInfo = firstLineOfSecondStop.split(":");
		
		firstFirstHour = Integer.parseInt(firstLineInfo[0]);
		secondFirstHour = Integer.parseInt(secondLineInfo[0]);
		
		String[] firstLineInfoMinutes = firstLineInfo[1].split(",");
		String[] secondLineInfoMinutes = secondLineInfo[1].split(",");
		
		if(firstLineInfoMinutes.length > 0 && secondLineInfoMinutes.length > 0)
		{
			firstFirstMinute = Integer.parseInt(firstLineInfoMinutes[0]);
			secondFirstMinute = Integer.parseInt(secondLineInfoMinutes[0]);			
		}
		else
		{
			firstFirstMinute = Integer.parseInt(firstLineOfFirstStop);
			secondFirstMinute = Integer.parseInt(firstLineOfFirstStop);					
		}
		
		
		if(firstFirstHour  > secondFirstHour)
		{
			return false;
		}
		else if(firstFirstHour == secondFirstHour && firstFirstMinute > secondFirstMinute)
		{
			return false;
		}

		return true;
	}
	
	/**
	 * Funkcja ma za zadanie zwróciæ prywatn¹ listê wyodrêbnionych godzin. Zak³adamy,
	 * ¿e u¿ytkownik skorzysta³
	 * z metody searchHours.
	 * @return zwraca gotowe wyodrêbnione godziny.
	 */
	public ArrayList<String> getHours()
	{
		return hours;
	}
	
	/**
	 * Funkcja odpowiada za wyodrêbnienie godzin i minut odjazdów z przystanku pierwszego
	 * do przystanku drugiego bez przesiadek. Na pocz¹tku czyœci listê z poprzednimi wynikami
	 * dzia³ania obiektu, nastêpnie wczytuje dane z plików o nazwach równych nazwom
	 * przystanków, nastêpnie sprawdza poprawnoœæ wyci¹gniêtych danych oraz okreœla 
	 * w³aœciwe kierunki, bior¹c pod uwagê pierwsze przejazdy. Kiedy pod uwagê brane s¹ 
	 * ju¿ tylko prawid³owe linie i kierunki, wyci¹ga z nich odpowiednie godziny oraz minuty,
	 * dla zadanych w argumentach ograniczeñ.
	 * @param firstBuStopName nazwa pierwszego przystanku.
	 * @param secondBuStopName nazwa drugiego przystanku.
	 * @param hour godzina od której naliczamy odjazd.
	 * @param minutes minuty od których naliczamy odjazd.
	 * @param maxTime maksymalny czas odjazdu w godzinach.
	 * @param typeOfDay rodzaj dnia. Mo¿liwe s¹ dni powszednie, wtedy wartoœæ to 0,
	 * 		dla sobót wartoœæ to 1, a dla œwi¹t wartoœæ o 2.
	 * @return zwraca prawdê je¿eli lista utworzonych linii z godzin¹ oraz minutami odjazdu
	 * 		jest niepusta, jeœli natomiast bêdzie pusta zostanie zwrócony fa³sz.
	 */
	public boolean searchHours(String firstBuStopName, String secondBuStopName,
			int hour, int minutes, int maxTime, int typeOfDay)
	{
		hours.clear();
		String firstLineOfFirstStop = "";
		String firstLineOfSecondStop = "";
		
		//Wyodrêbniamy surowe dane z plików dla odpowiednich przystanków
		ArrayList<String> timeOfStartStop = readLinesFromFile(firstBuStopName);
		ArrayList<String> timeOfEndStop = readLinesFromFile(secondBuStopName);
		
		if(checkHoursLineFormat(timeOfStartStop) && checkHoursLineFormat(timeOfEndStop))
		{
			firstLineOfFirstStop = firstLineOfStop(timeOfStartStop,typeOfDay);
			firstLineOfSecondStop = firstLineOfStop(timeOfEndStop,typeOfDay);

			//Teraz wyodrêbniamy z surowych danych listê minut, które maj¹ nast¹piæ	
			timeOfStartStop = reachOutTime(timeOfStartStop,hour,minutes,maxTime,typeOfDay);
			timeOfEndStop = reachOutTime(timeOfEndStop,hour,minutes,maxTime,typeOfDay);
			
			if(checkDirection(timeOfStartStop,timeOfEndStop,firstLineOfFirstStop,
					firstLineOfSecondStop))
			{
				hours = timeOfStartStop;
			}			
		}
		
		return !hours.isEmpty();
	}
}