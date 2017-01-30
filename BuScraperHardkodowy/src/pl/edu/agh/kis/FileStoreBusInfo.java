package pl.edu.agh.kis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
	 * Pole przechowuje nazw� aktualnie przetwarzanego przystanku
	 */
	private String direction;
	
	/**
	 * Pole przechowuje list� wierszy, z kt�rych b�dzie si� sk�ada� tabela odjazd�w 
	 */
	private ArrayList<String> hours;
	
	/**
	 * Zadaniem funkcji jest przygotowanie nazwy przystanku z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoBuStopName nazwa przystanku wyodr�bniona przez XPath
	 * @return informacja czy uda�o si� przygotowa� poprawn� nazw� przystanku
	 */
	private boolean prepareBuStopName(String infoBuStopName)
	{
		if(infoBuStopName != null && !infoBuStopName.equals("") )//&& infoBuStopName.matches("\\w+"))
		{
			storeLogger.info("Znalaz�em nazw� linii: ",infoBuStopName);
			buStopName = infoBuStopName;
			return true;
		}
		else 
		{
			storeLogger.warning("Nazwa linii zawiera b��dy!: ",infoBuStopName);
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie nazwy przystanku z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoLineNumber numer linii wyodr�bniony przez XPath
	 * @return informacja czy uda�o si� przygotowa� poprawny numer linii
	 */
	private boolean prepareLineNumber(String infoLineNumber)
	{
		if(infoLineNumber != null && !infoLineNumber.equals("") && infoLineNumber.matches("^\\d{1,3}$"))
		{
			storeLogger.info("Znalaz�em numer linii: ",infoLineNumber);
			lineNumber = infoLineNumber;
			return true;
		}
		else
		{
			storeLogger.warning("Numer linii zawiera b��dy!: ",infoLineNumber);
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie kierunku przystanku z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoDirection surowe dane dotycz�ce kierunku linii
	 * @return informacja czy uda�o si� przygotowa� poprawny kierunek linii
	 */
	private boolean prepareDirection(String infoDirection)
	{	
		infoDirection = infoDirection.replace(":", "");
		
		if(infoDirection != null && !infoDirection.equals("") && infoDirection.contains("Do"))
		{
			storeLogger.info("Znalaz�em kierunek linii: ",infoDirection);
			direction = infoDirection;
			return true;
		}
		else 
		{
			storeLogger.warning("Kierunek linii zawiera b��dy!: ",infoDirection);
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przyst�pne przygotowanie listy godzin z surowych danych
	 * @param infoHours surowe dane wyci�gni�te z XPath
	 * @return lista godzina przygotowana z surowych danych
	 */
	private boolean prepareHours(String infoHours)
	{
		//Odcinamy od g�ry wszystko do s�owa kluczowego godzina

		String timeLine;
		int hoursNumber = 24;
		
		
		if(infoHours.contains("Godzina"))
		{
			timeLine = infoHours.substring(infoHours.lastIndexOf("Godzina"));
		}
		else
		{
			return false;
		}
		
		if(!infoHours.contains("15\n"))
		{
			hoursNumber = 8;
		}
		
		timeLine = timeLine.replaceAll(" ", ",").replaceAll(",,", "");
		String[] splitedTimeLine = timeLine.split("\n");
		ArrayList<String> hoursWithMinutes = new ArrayList<String>();
		//W tym momencie mamy 4 linijki danych ( mog� by� puste ), nast�pnie dwie linijki przerwy
		
		int starter = 7;
		int paragraph = starter;
		int numberOfHourLines = 0;
		String readyLine = "";
		
		for(int i = starter; i < splitedTimeLine.length; ++i)
		{
			if(paragraph == 12)
			{
				++numberOfHourLines;
				hoursWithMinutes.add(readyLine+"\r\n");
				readyLine = "";
				paragraph = starter;
				
				if(numberOfHourLines == hoursNumber)
				{
					break;
				}
			}
			else if(paragraph < 11)
			{
				readyLine += splitedTimeLine[i]+":";
				++paragraph;
			}
			else
			{
				++paragraph;
			}
		
		}
		
		//Teraz hoursWithMinutes zawiera w ka�dym elemencie godzin� i minuty dla poszczeg�lnych sekji
		
		hours = hoursWithMinutes;
		
		return true;
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie kierunku przystanku z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoHour surowe dane dotycz�ce kierunku linii
	 * @return informacja czy uda�o si� przygotowa� poprawn� godzin� i minuty
	 */
	private boolean prepareHour(String infoHour)
	{
		//Do naprawy infoHour.matches......
		if(infoHour != null && !infoHour.equals(""))//&& infoHour.matches("\\d"))
		{

			storeLogger.info("Znalazlem godzin� odjazdu: ",infoHour);	
			return true;
		}
		else
		{
			storeLogger.warning("Godzina zawiera b��dy!: ",infoHour);
			return false;
		}
	}
	
	/**
	 * Funkcja ma za zadanie wys�a� przygotowane wcze�niej dane do okre�lonego pliku
	 * w okre�lonym folderze, nie k�opocz�c si� o rozdzielanie oraz segregacj� danych
	 */
	private void sendInfos()
	{
		String fileName = lineNumber+direction+"/"+buStopName;
		
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

			for(String s : hours)
			{
				input.write(s);
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
		storeLogger.execute();
		lineNumber = null;
		buStopName = null;
	}

	/**
	 * Funkcja otrzymuje paczk� informacji zesk�adowanych w stringu, jest to rozk�ad
	 * jednego przystanku dla jednej linii, przy czym te dane r�wnie� musz� si� tam
	 * znajdowa� w odpowiedniej konwencji
	 * @param allInformations paczka informacji, kt�re musimy zapisa� w jednym z plik�w
	 * 			opr�cz rozk�adu musi si� tam znajdowa� nazwa przystanku oraz numer linii i kierunek
	 */
	@Override
	public void storeInfo(Map<String,String> allInformations) {
		
		boolean wellPrepared = true;
		
		//Uruchamiamy funkcje, kt�rych zadaniem jest odpowiednie przygotowanie danych
		
		if(!prepareBuStopName(allInformations.get("buStopName")))
		{
			wellPrepared = false;
		}
		if(!prepareLineNumber(allInformations.get("lineNumber")))
		{
			wellPrepared = false;
		}
		if(!prepareDirection(allInformations.get("direction")))
		{
			wellPrepared = false;
		}
		if(!prepareHours(allInformations.get("hours")))
		{
			wellPrepared = false;
		}
		
		if(hours != null)
		{
			for(String s : hours)
			{
				if(!prepareHour(s))
				{
					wellPrepared = false;
				}
			}
		}
		//Uruchamiamy funkcj� wysy�aj�c�, je�eli dane zosta�y przygotowane prawid�owo
		if(wellPrepared)
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