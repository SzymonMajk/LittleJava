package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasa której zadaniem jest zwrócenie listy wyodrêbnionych godzin odjazdów bior¹c pod uwagê
 * wszystkie otrzymane ograniczenia oraz kierunek przejazdu
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class HourBrowser {

	/**
	 * W³asny system logów
	 */
	private Logger browserLogger;
	
	/**
	 * Lista gotowych wyodrêbnionych godzin
	 */
	private ArrayList<String> hours = new ArrayList<String>();
	
	/**
	 * Funkcja ma za zadanie zwróciæ prywatn¹ listê wyodrêbnionych godzin
	 * @return zwraca gotowe wyodrêbnione godziny
	 */
	public ArrayList<String> getHours()
	{
		return hours;
	}
	
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
			browserLogger.warning("Godziny by³y puste");
			return false;
		}
		else if(startingHour < 0 || startingHour > 23)
		{
			browserLogger.warning("Podana godzina wykracza poza dozwolone godziny");
			return false;
		}
		else if(startingMinutes < 0 || startingMinutes > 59)
		{
			browserLogger.warning("Podana minuta wykracza poza dozwolone minuty");
			return false;
		}
		else if(maxTime < 0 || startingHour+maxTime > 23)
		{
			browserLogger.warning("Podany zakres godzin wykracza poza rozk³ad");
			return false;
		}
		else if(typeOfDay < 0 || typeOfDay > 2)
		{
			browserLogger.warning("Podano niepoprawny rodzaj dnia");
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
		//walidacja otrzymanych danych
		if(!validateReachOutTime(hours,startingHour,
				startingMinutes, maxTime,typeOfDay))
		{
			return new ArrayList<String>();
		}
		
		//Z ka¿dej linijki wyci¹gamy tylko te minuty które pasuj¹ ( z pierwszej tylko te
		//wiêksze od zadanych w argumencie, i tylko tyle linijek ile maxTime,
		ArrayList<String> result = new ArrayList<String>();
		
		/*System.out.println();
		for(String l : hours)
		{
			System.out.println(l);
		}		*/
		
		try {
		//wyci¹gamy odpowiedni czas
			for(String l : hours)
			{
				String[] separatedHourLine = l.split(":");
				String hour = separatedHourLine[0];
				
				
				//wyci¹gamy odpowiedni czas
				if(Integer.parseInt(hour) >= startingHour &&
						Integer.parseInt(hour) <= startingHour + maxTime)
				{
					if(separatedHourLine.length >= typeOfDay+2)
					{
						String minutes = separatedHourLine[typeOfDay+1];
						//dla pierwszej godziny sprawdzamy jeszcze minuty
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
							
							if(builder.equals(""))
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
			browserLogger.warning("B³¹d przy parsowaniu reachOut"+e.getMessage());
		}
		/*
		for(String l : result)
		{
			System.out.println(l);
		}*/
		
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
		//sprawdzamy czy nie otrzymaliœmy pustych list
		if(timeOfStartStop.isEmpty())
		{
			browserLogger.warning("Podane godziny nie pasuj¹ do godzin przystanku pocz¹tkowego.");
			return false;
		}
		else if(timeOfEndStop.isEmpty())
		{
			browserLogger.warning("Podane godziny nie pasuj¹ do godzin przystanku koñcowego.");
			return false;
		}
		else if(firstLineOfFirstStop.equals(""))
		{
			return false;
		}
		else if(firstLineOfSecondStop.equals(""))
		{
			return false;			
		}
		
		//sprawdzamy czy kierunek by³ odpowiedni czyli czy pierwsza minuta w pierwszym mniejsza
		//ni¿ w drugim
		
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
		

		if(firstFirstHour  > secondFirstHour || firstFirstMinute > 
			secondFirstMinute)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Funkcja odpowiada za wyodrêbnienie godzin i minut odjazdów z przystanku pierwszego
	 * do przystanku drugiego przez przesiadek
	 * @param firstBuStopName nazwa pierwszego przystanku
	 * @param secondBuStopName nazwa drugiego przystanku
	 * @param line numer linii ³¹cz¹cej przystanki
	 * @param hour godzina od której naliczamy odjazd
	 * @param minutes minuty od których naliczamy odjazd
	 * @param maxTime maksymalny czas odjazdu
	 * @param typeOfDay rodzaj dnia
	 * @return godziny i minuty ³¹cz¹ce przystanek koñcowy z pocz¹tkowym dla okreœlonych
	 * kryteriów
	 */
	public boolean searchHours(String firstBuStopName, String secondBuStopName, String line,
			int hour, int minutes, int maxTime, int typeOfDay)
	{
		hours.clear();
		String firstLineOfFirstStop = "";
		String firstLineOfSecondStop = "";
		
		//Wyodrêbniamy surowe dane z plików dla odpowiednich przystanków
		ArrayList<String> timeOfStartStop = readLinesFromFile(line+"/"+firstBuStopName);
		ArrayList<String> timeOfEndStop = readLinesFromFile(line+"/"+secondBuStopName);
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
		
		browserLogger.execute();
		return !hours.isEmpty();
	}
	
	/**
	 * Konstruktor którego zadaniem jest przypisanie domyœlnego systemu logów
	 */
	public HourBrowser()
	{
		browserLogger = new Logger();
		browserLogger.changeAppender(new FileAppender("HourBrowser"));
	}
}
