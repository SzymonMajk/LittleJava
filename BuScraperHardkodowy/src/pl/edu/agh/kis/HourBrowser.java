package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasa kt�rej zadaniem jest zwr�cenie listy wyodr�bnionych godzin odjazd�w bior�c pod uwag�
 * wszystkie otrzymane ograniczenia oraz kierunek przejazdu
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class HourBrowser {

	/**
	 * W�asny system log�w
	 */
	private Logger browserLogger;
	
	/**
	 * Lista gotowych wyodr�bnionych godzin
	 */
	private ArrayList<String> hours = new ArrayList<String>();
	
	/**
	 * Funkcja ma za zadanie zwr�ci� prywatn� list� wyodr�bnionych godzin
	 * @return zwraca gotowe wyodr�bnione godziny
	 */
	public ArrayList<String> getHours()
	{
		return hours;
	}
	
	/**
	 * Funkcja ma za zadanie zczyta� zawarto�� pliku podanego w argumencie oraz
	 * zwr�ci� tablic� z wszystkimi godzinami odjazd�w
	 * @param fileName nazwa pliku dla kt�rego wyci�gamy informacje
	 * @return wszystkie godziny odjazd�w z pliku o zadanej nazwie
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
	 * Funkcja sprawdzaj�ca poprawno�� wszystkich danych z argument�w i zwracaj�ca
	 * informacj� o ich poprawno�ci
	 * @param hours lista godzin, sprawdzimy czy nie jest pusta
	 * @param startingHour godzina rozpoczynaj�ca wyszukiwania
	 * @param startingMinutes minuta rozpoczynaj�ca wyszukiwania
	 * @param maxTime maksymalna ilo�� godzin wyszukiwania
	 * @param typeOfDay rodzaj dnia w kt�rym wyszukujemy
	 * @return informacja czy podane dane by�y poprawne
	 */
	private boolean validateReachOutTime(ArrayList<String> hours, int startingHour,
			int startingMinutes, int maxTime, int typeOfDay)
	{
		if(hours == null || hours.isEmpty())
		{
			browserLogger.warning("Godziny by�y puste");
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
			browserLogger.warning("Podany zakres godzin wykracza poza rozk�ad");
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
	 * Zadaniem funkcji jest zwr�cenie pierwszej godziny, w kt�rej dla zadanego rodzaju dnia
	 * wyst�puje jaka� minuta
	 * @param hours lista wszystkich godzin dla z danego przystanku
	 * @param typeOfDay rodzaj dnia
	 * @return pierwsza godzina, w kt�rej dla zadanego rodzaju dnia wyst�puje autobus
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
	 * Zadaniem funkcji jest wyci�gni�cie odpowiednich linii godzin i minut pod warunkami
	 * zadanymi w argumentach
	 * param hours lista godzin, sprawdzimy czy nie jest pusta
	 * @param startingHour godzina rozpoczynaj�ca wyszukiwania
	 * @param startingMinutes minuta rozpoczynaj�ca wyszukiwania
	 * @param maxTime maksymalna ilo�� godzin wyszukiwania
	 * @param typeOfDay rodzaj dnia w kt�rym wyszukujemy
	 * @return lista godzin i minut, kt�re spe�ni�y kryteria z argument�w
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
		
		//Z ka�dej linijki wyci�gamy tylko te minuty kt�re pasuj� ( z pierwszej tylko te
		//wi�ksze od zadanych w argumencie, i tylko tyle linijek ile maxTime,
		ArrayList<String> result = new ArrayList<String>();
		
		/*System.out.println();
		for(String l : hours)
		{
			System.out.println(l);
		}		*/
		
		try {
		//wyci�gamy odpowiedni czas
			for(String l : hours)
			{
				String[] separatedHourLine = l.split(":");
				String hour = separatedHourLine[0];
				
				
				//wyci�gamy odpowiedni czas
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
			browserLogger.warning("B��d przy parsowaniu reachOut"+e.getMessage());
		}
		/*
		for(String l : result)
		{
			System.out.println(l);
		}*/
		
		return result;
	}
	
	/**
	 * Zadaniem funkcji jest odrzucenie tych wynik�w, kt�re posiadaj� b��dny kierunek
	 * @param timeOfStartStop lista czas�w dla przystanku pocz�tkowego
	 * @param timeOfEndStop lista czas�w dla przystanku ko�cowego
	 * @param firstLineOfFirstStop pierwsza linia przystanku pocz�tkowego
	 * @param firstLineOfSecondStop pierwsza linia przystanku ko�cowego
	 * @return informacja o tym czy kierunek by� w�a�ciwy
	 */
	private boolean checkDirection(ArrayList<String> timeOfStartStop, ArrayList<String> timeOfEndStop,
			String firstLineOfFirstStop, String firstLineOfSecondStop)
	{
		//sprawdzamy czy nie otrzymali�my pustych list
		if(timeOfStartStop.isEmpty())
		{
			browserLogger.warning("Podane godziny nie pasuj� do godzin przystanku pocz�tkowego.");
			return false;
		}
		else if(timeOfEndStop.isEmpty())
		{
			browserLogger.warning("Podane godziny nie pasuj� do godzin przystanku ko�cowego.");
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
		
		//sprawdzamy czy kierunek by� odpowiedni czyli czy pierwsza minuta w pierwszym mniejsza
		//ni� w drugim
		
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
	 * Funkcja odpowiada za wyodr�bnienie godzin i minut odjazd�w z przystanku pierwszego
	 * do przystanku drugiego przez przesiadek
	 * @param firstBuStopName nazwa pierwszego przystanku
	 * @param secondBuStopName nazwa drugiego przystanku
	 * @param line numer linii ��cz�cej przystanki
	 * @param hour godzina od kt�rej naliczamy odjazd
	 * @param minutes minuty od kt�rych naliczamy odjazd
	 * @param maxTime maksymalny czas odjazdu
	 * @param typeOfDay rodzaj dnia
	 * @return godziny i minuty ��cz�ce przystanek ko�cowy z pocz�tkowym dla okre�lonych
	 * kryteri�w
	 */
	public boolean searchHours(String firstBuStopName, String secondBuStopName, String line,
			int hour, int minutes, int maxTime, int typeOfDay)
	{
		hours.clear();
		String firstLineOfFirstStop = "";
		String firstLineOfSecondStop = "";
		
		//Wyodr�bniamy surowe dane z plik�w dla odpowiednich przystank�w
		ArrayList<String> timeOfStartStop = readLinesFromFile(line+"/"+firstBuStopName);
		ArrayList<String> timeOfEndStop = readLinesFromFile(line+"/"+secondBuStopName);
		firstLineOfFirstStop = firstLineOfStop(timeOfStartStop,typeOfDay);
		firstLineOfSecondStop = firstLineOfStop(timeOfEndStop,typeOfDay);

		//Teraz wyodr�bniamy z surowych danych list� minut, kt�re maj� nast�pi�	
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
	 * Konstruktor kt�rego zadaniem jest przypisanie domy�lnego systemu log�w
	 */
	public HourBrowser()
	{
		browserLogger = new Logger();
		browserLogger.changeAppender(new FileAppender("HourBrowser"));
	}
}
