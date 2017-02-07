package pl.edu.agh.kis;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;

/**
 * Klasa ma za zadanie implementowa� metod� interfejsu StoreBusInfo, zapisuj�c otrzymywane
 * dane w katalogach, kt�rych nazwy sugeruj� numer linii, w plikach kt�rych nazwy b�d�
 * odpowiada�y nazwom przystank�w oraz tworz�c katalog plik�w o nazwach przystank�w, 
 * w kt�rych znajd� si� numery linii przeje�dzaj�cych przez przystanek wraz z kierunkiem.
 * @author Szymon Majkut
 * @version 1.4
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
		if(infoBuStopName != null && !infoBuStopName.equals("") )
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
	 * Zadaniem funkcji jest przygotowanie numeru linii z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoLineNumber numer linii wyodr�bniony przez XPath
	 * @return informacja czy uda�o si� przygotowa� poprawny numer linii
	 */
	private boolean prepareLineNumber(String infoLineNumber)
	{
		if(infoLineNumber != null && !infoLineNumber.equals("") 
				&& infoLineNumber.matches("^\\d{1,3}$"))
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
	 * Zadaniem funkcji jest przygotowanie kierunku linii z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoDirection surowe dane dotycz�ce kierunku linii
	 * @return informacja czy uda�o si� przygotowa� poprawny kierunek linii
	 */
	private boolean prepareDirection(String infoDirection)
	{	
		infoDirection = infoDirection.replace(":", "");
		
		if(!infoDirection.equals("") && infoDirection.contains("Do"))
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
		
		//Sprwadzamy czy mamy do czynienia z lini� nocn�
		if(!infoHours.contains("15\n"))
		{
			hoursNumber = 8;
		}
		
		timeLine = timeLine.replaceAll(" ", ",").replaceAll(",,", "");
	
		String[] splitedTimeLine = timeLine.split("\n");
		
		ArrayList<String> hoursWithMinutes = new ArrayList<String>();
		//W tym momencie mamy 4 linijki danych ( mog� by� puste ), 
		//nast�pnie dwie linijki przerwy
		
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
		
		//Teraz hoursWithMinutes zawiera w ka�dym elemencie godzin� 
		//i minuty dla poszczeg�lnych sekji	
		hours = hoursWithMinutes;
		
		return true;
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie pojedynczej linijki z danymi czasowymi z surowych 
	 * danych otrzymanych w parametrze oraz poinformowanie o poprawno�ci tych danych
	 * @param infoHour linijka, w kt�rej b�dziemy poszukiwa� informacji dotycz�cych czasu
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
	 * Funkcja ma za zadanie wys�a� przygotowane i sprawdzone wcze�niej dane do odpowiednich
	 * katalog�w, pami�taj�c o tym, �eby zapisa� w�a�ciwe godziny odjazd�w oraz zale�no�ci
	 * pomi�dzy poszczeg�lnymi przestankami, a liniami
	 */
	private void sendInfos()
	{	
		String fileName = lineNumber+direction+"/"+buStopName;
		BufferedReader output;
		FileWriter input;
		String line;
		StringBuilder builder;
		
		File toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			storeLogger.error("Nie uda�o mi si� utworzy� folderu!",toSend.getParent());
		}
		
		try {
			if(!toSend.createNewFile())
			{
				storeLogger.error("Nie uda�o si� utworzy� pliku!",fileName);
			}
			storeLogger.info("Utworzy�em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Wyj�tek przy tworzeniu pliku!",fileName,e.getMessage());
		}

		try {
			input = new FileWriter(toSend);

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
		
		//Dodanie nazwy linii do linii danego przystanku
		
		fileName = "buStops/"+buStopName;
		toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			storeLogger.error("Nie uda�o si� utworzy� pliku!",fileName);
		}
		
		try {
			if(!toSend.exists())
			{
				if(!toSend.createNewFile())
				{
					storeLogger.error("Nie uda�o si� utworzy� pliku!",fileName);
				}
			}
			
			storeLogger.info("Utworzy�em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Rzuci�em wyj�tek przy tworzeniu pliku!",fileName,
					e.getMessage());
		}

		try {
			
			output = new BufferedReader(new FileReader(toSend));
			
			line = "";
			builder = new StringBuilder(line);
			
			while((line = output.readLine()) != null)
			{
				builder.append(line);
			}
			
			if(!builder.toString().contains(lineNumber+direction))
			{
				input = new FileWriter(toSend,true); //chcemy nadpisywa�
				input.write(lineNumber+direction+"\n");
				input.close();
			}
			
			output.close();
			storeLogger.info("Nadpisa�em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Problem z zapisem do pliku!",fileName);
			e.printStackTrace();
		}
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie p�l, przygotowuj�c je na przyj�cie oraz
	 * przetworzenie kolejnej porcji informacji
	 */
	private void clear()
	{
		storeLogger.execute();
		lineNumber = null;
		buStopName = null;
	}

	/**
	 * Funkcja otrzymuje paczk� danych wyodr�bnionych przez w�tki wy�uskuj�ce, ma za
	 * zadanie sprawdzi� poprawno�� tych danych, a je�eli uda si� z nich wyci�gn�� 
	 * przydatne informacje, zapisa� je w katalogach z nazwami linii wraz z kierunkiem,
	 * w plikach o nazwach przystank�w, dodatkowo tworz�c katalog pomocniczy dla wyszukiwania
	 * zawieraj�cy pliki z wszystkimi przystankami, w kt�rych znajduj� si� numery linii wraz
	 * z kierunkiem przeje�dzaj�ce przez dany przystanek.
	 * @param allInformations paczka danych, kt�rych poprawno�� musimy sprawdzi� oraz
	 *        wyci�gn�� z nich przydatne informacje
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
	 * Konstruktor sparametryzowany, przypisuj�cy obiekt do sk�adowania log�w.
	 * @param appender okre�la spos�b sk�adowania log�w
	 */
	FileStoreBusInfo(Appends appender)
	{
		storeLogger = new Logger();
		storeLogger.changeAppender(appender);
		storeLogger.info("Czas rozpocz�� prac�!");
		storeLogger.execute();
	}
	
	/**
	 * Konstruktor domy�lny, kt�ry ustala domy�lny spos�b sk�adowania log�w.
	 */
	FileStoreBusInfo()
	{
		this(new FileAppender("FileStore"));
	}
}