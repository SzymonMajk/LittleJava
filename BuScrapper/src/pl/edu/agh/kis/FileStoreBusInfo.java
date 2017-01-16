package pl.edu.agh.kis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Klasa ma za zadanie implementowa� metod� interfejsu StoreBusInfo, zapisuj�c
 * otrzymywane dane w katalogach, kt�rych nazwy sugeruj� numer linii, w plikach
 * kt�rych nazwy b�d� odpowiada�y nazwom przystank�w
 * @author Szymon Majkut
 * @version 1.1b
 */
public class FileStoreBusInfo implements StoreBusInfo {

	/**
	 * W�asny system log�w
	 */
	private Logger storeLogger;
	
	/**
	 * Pole przechowuje numer aktualnie przetwarzanej linii
	 */
	private String lineNumber;
	
	/**
	 * Pole przechowuje nazw� aktualnie przetwarzanego przystanku
	 */
	private String buStopName;
	
	/**
	 * Pole przechowuje list� wierszy, z kt�rych b�dzie si� sk�ada� tabela odjazd�w 
	 */
	private Queue<HourLine> departHours = new PriorityQueue<HourLine>();
		
	/**
	 * Zmienna okre�laj�ca mo�liwo�� wys�ania danych je�eli s� poprawne
	 */
	private boolean readyToSend;
	
	/**
	 * Funkcja otrzymuje paczk� informacji do przetworzenia, po czym analizuje ka�dy
	 * wiersz w poszukiwaniu odpowiednich informacji, zapami�tuje informacje i zapisuje
	 * do log�w wszelkie niezgodno�ci z za�o�eniami, je�eli otrzyma wszystkie niezb�dne
	 * informacje dopu�ci do zesk�adowania danych w przeciwnym wypadku poprzez logi
	 * powiadomi o b��dach
	 * @param allInformations paczka informacji do przetworzenia
	 */
	private void prepareInfos(String allInformations)
	{
		boolean alreadyNamed = false;
		boolean alreadySetLine = false;
		boolean atLeastOneHour = false;
		
		String[] infos = allInformations.replace("\r", "").split("\n");
		
		for(String s : infos)
		{
			if(s.startsWith("name="))
			{
				String name = s.substring(5);

				if(name == null || name.matches("\\s"))
				{
					continue;
				}
				else if(alreadyNamed)
				{
					storeLogger.warning("Zdublowana nazwa linii: ",name);
					continue;
				}
				else if(name == null || name.matches("\\w+"))
				{
					storeLogger.info("Znalaz�em nazw� linii: ",name);
					buStopName = name;
					alreadyNamed = true;				}
				else 
				{
					storeLogger.warning("Nazwa linii zawiera b��dy!: ",name);
				}
			}
			else if(s.startsWith("number="))
			{
				String number = s.substring(7);
				
				if(number == null)
				{
					continue;
				}
				else if(alreadySetLine)
				{
					storeLogger.warning("Zdublowany numer linii: ",number);
					continue;
				}
				else if(number.matches("^\\d{1,3}$"))
				{
					storeLogger.info("Znalaz�em numer linii: ",number);
					lineNumber = number;
					alreadySetLine = true;				}
				else
				{
					storeLogger.warning("Numer linii zawiera b��dy!: ",number);
				}
			}
			else if(s.startsWith("hour="))
			{
				String hour = s.substring(5);
				
				if(hour == null || hour == "")
				{
					continue;
				}
				else if(hour.matches("^[0-2]\\d\\d(,\\d\\d)+"))
				{
					storeLogger.info("Znalazlem godzin� odjazdu: ",hour);
					departHours.add(new HourLine(hour));
					atLeastOneHour = true;				}
				else
				{
					storeLogger.warning("Godzina zawiera b��dy zawiera b��dy!: ",hour);
				}
			}
			else
			{
				storeLogger.warning("Niepoprawne dane wej�ciowe: ",s);
			}
		}
		
		if((alreadyNamed && alreadySetLine && atLeastOneHour))
		{
			readyToSend = true;
		}
		else
		{
			storeLogger.error("Niedopuszczam do wys�ania danych: ",allInformations);
		}
		
		storeLogger.execute();
	}
	
	/**
	 * Funkcja ma za zadanie wys�a� przygotowane wcze�niej dane okre�laj�c ich po�o�enia
	 * poprzez numer linii oraz nazw� przystanku, zabezpieczaj�c si� zmienn� readyToSend
	 * mamy pewno��, �e lineNumber oraz buStopName s� prawid�owymi obiektami typu String
	 */
	private void sendInfos()
	{
		String fileName = lineNumber+"/"+buStopName;
		File toSend = new File(fileName);
		new File(toSend.getParent()).mkdir();
		try {
			toSend.createNewFile();
			storeLogger.info("Utworzy�em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Nie uda�o si� utworzy� pliku!",fileName);
			e.printStackTrace();
		}

		try {
			FileWriter input = new FileWriter(toSend);

			input.write("Dni powszednie:\n");
			for(HourLine h : departHours)
			{
				if(h.getTypeOfDay() == '0')
				{
					input.write(h.printHour());
				}
			}
			
			input.write("Soboty:\n");
			for(HourLine h : departHours)
			{
				if(h.getTypeOfDay() == '1')
				{
					input.write(h.printHour());
				}
			}
			
			input.write("Niedziele:\n");
			for(HourLine h : departHours)
			{
				if(h.getTypeOfDay() == '2')
				{
					input.write(h.printHour());
				}
			}
			
			input.close();
			storeLogger.info("Nadpisa�em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Problem z zapisem do pliku!",fileName);
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie p�l, przygotowuj�c je na przyj�cie kolejnej
	 * porcji informacji
	 */
	private void clear()
	{
		readyToSend = false;
		lineNumber = null;
		buStopName = null;
		departHours.clear();
	}

	/**
	 * Funkcja otrzymuje paczk� informacji zesk�adowanych w stringu, jest to rozk�ad
	 * jednego przystanku dla jednej linii, przy czym te dane r�wnie� musz� si� tam
	 * znajdowa� w odpowiedniej konwencji
	 * @param allInformations paczka informacji, kt�re musimy zapisa� w jednym z plik�w
	 * 			opr�cz rozk�adu musi si� tam znajdowa� nazwa przystanku oraz numer linii
	 */
	@Override
	public void storeInfo(String allInformations) {
		//Uruchamiamy funkcj�, kt�rej zadaniem jest odpowiednie przygotowanie danych
		prepareInfos(allInformations);
		
		//Uruchamiamy funkcj� wysy�aj�c�, je�eli dane zosta�y przygotowane prawid�owo
		if(readyToSend)
		{
			sendInfos();
		}
		
		//Sprz�tamy dla kolejnego u�ycia
		clear();
	}
	
	/**
	 * Konstruktor sparametryzowany, pozwalaj�cy na okre�lenie sposoby sk�adowana
	 * log�w, stworzony dla u�atwienia sprz�tania po testach
	 */
	FileStoreBusInfo(Appends appender)
	{
		storeLogger = new Logger();
		storeLogger.changeAppender(appender);
		storeLogger.info("Czas rozpocz�� prac�!");
		storeLogger.execute();
	}
	
	/**
	 * Konstruktor domy�lny, kt�ry obs�uguje domy�lne przygotowania pod system log�w
	 */
	FileStoreBusInfo()
	{
		this(new FileAppender("FileStore"));
	}
}

/**
 * Pomocnicza klasa u�atwiaj�ca przetrzymywanie linii danych dotycz�cych
 * godzin odjazdu, uwzgl�dniaj�c to w kt�rym typie dnia si� znajduj�
 * @author Szymon Majkut
 * @version 1.0
 *
 */
class HourLine implements Comparable<HourLine> {
	
	private char typeOfDay;
	
	private String hour;
	
	private String minutes;
	
	/**
	 * Prosty getter pobieraj�cy zararo�� pola typeOfDay
	 * @return zawarto�� pola typeOfDay
	 */
	public int getTypeOfDay()
	{
		return typeOfDay;
	}
	
	/**
	 * Funkcja zwracaj�ca godzin� oraz minuty odjazdu odzielane przecinkami
	 * @return godzina odjazdu oraz odpowiadaj�ce jej minuty
	 */
	public String printHour()
	{
		return hour + ": " + minutes+"\n";
	}
	
	/**
	 * Konstruktor sparametryzowany, pozwalaj�cy na przypisanie odpowiednich
	 * danych z otrzymanej linii godzin
	 * @param line linia minut dla danej godziny z uwzgl�dnieniem typu dnia
	 */
	HourLine(String line)
	{
		typeOfDay = line.charAt(0);
		hour = line.substring(1,3);
		minutes = line.substring(4);
	}

	/**
	 * Implementacja comparatora, por�wnuje linie godzin odjazd�w zgodnie
	 * z warto�ci� godzin
	 * @param o drugi obiekt HourLine do por�wnania
	 * @return warto�� okre�laj�ca wzajemn� relacj� obiekt�w w sferze por�wnania
	 */
	@Override
	public int compareTo(HourLine o) {
		return this.hour.compareTo(o.hour);
	}
}
