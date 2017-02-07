package pl.edu.agh.kis;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;

/**
 * Klasa ma za zadanie implementowaæ metodê interfejsu StoreBusInfo, zapisuj¹c otrzymywane
 * dane w katalogach, których nazwy sugeruj¹ numer linii, w plikach których nazwy bêd¹
 * odpowiada³y nazwom przystanków oraz tworz¹c katalog plików o nazwach przystanków, 
 * w których znajd¹ siê numery linii przeje¿dzaj¹cych przez przystanek wraz z kierunkiem.
 * @author Szymon Majkut
 * @version 1.4
 */
public class FileStoreBusInfo implements StoreBusInfo {

	/**
	 * W³asny system logów
	 */
	private Logger storeLogger;
	
	/**
	 * Pole przechowuje numer aktualnie przetwarzanej linii
	 */
	private String lineNumber;
	
	/**
	 * Pole przechowuje nazwê aktualnie przetwarzanego przystanku
	 */
	private String buStopName;
	
	/**
	 * Pole przechowuje nazwê aktualnie przetwarzanego przystanku
	 */
	private String direction;
	
	/**
	 * Pole przechowuje listê wierszy, z których bêdzie siê sk³adaæ tabela odjazdów 
	 */
	private ArrayList<String> hours;
	
	/**
	 * Zadaniem funkcji jest przygotowanie nazwy przystanku z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawnoœci tych danych
	 * @param infoBuStopName nazwa przystanku wyodrêbniona przez XPath
	 * @return informacja czy uda³o siê przygotowaæ poprawn¹ nazwê przystanku
	 */
	private boolean prepareBuStopName(String infoBuStopName)
	{
		if(infoBuStopName != null && !infoBuStopName.equals("") )
		{
			storeLogger.info("Znalaz³em nazwê linii: ",infoBuStopName);
			buStopName = infoBuStopName;
			return true;
		}
		else 
		{
			storeLogger.warning("Nazwa linii zawiera b³êdy!: ",infoBuStopName);
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie numeru linii z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawnoœci tych danych
	 * @param infoLineNumber numer linii wyodrêbniony przez XPath
	 * @return informacja czy uda³o siê przygotowaæ poprawny numer linii
	 */
	private boolean prepareLineNumber(String infoLineNumber)
	{
		if(infoLineNumber != null && !infoLineNumber.equals("") 
				&& infoLineNumber.matches("^\\d{1,3}$"))
		{
			storeLogger.info("Znalaz³em numer linii: ",infoLineNumber);
			lineNumber = infoLineNumber;
			return true;
		}
		else
		{
			storeLogger.warning("Numer linii zawiera b³êdy!: ",infoLineNumber);
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie kierunku linii z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawnoœci tych danych
	 * @param infoDirection surowe dane dotycz¹ce kierunku linii
	 * @return informacja czy uda³o siê przygotowaæ poprawny kierunek linii
	 */
	private boolean prepareDirection(String infoDirection)
	{	
		infoDirection = infoDirection.replace(":", "");
		
		if(!infoDirection.equals("") && infoDirection.contains("Do"))
		{
			storeLogger.info("Znalaz³em kierunek linii: ",infoDirection);
			direction = infoDirection;
			return true;
		}
		else 
		{
			storeLogger.warning("Kierunek linii zawiera b³êdy!: ",infoDirection);
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przystêpne przygotowanie listy godzin z surowych danych
	 * @param infoHours surowe dane wyci¹gniête z XPath
	 * @return lista godzina przygotowana z surowych danych
	 */
	private boolean prepareHours(String infoHours)
	{
		//Odcinamy od góry wszystko do s³owa kluczowego godzina
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
		
		//Sprwadzamy czy mamy do czynienia z lini¹ nocn¹
		if(!infoHours.contains("15\n"))
		{
			hoursNumber = 8;
		}
		
		timeLine = timeLine.replaceAll(" ", ",").replaceAll(",,", "");
	
		String[] splitedTimeLine = timeLine.split("\n");
		
		ArrayList<String> hoursWithMinutes = new ArrayList<String>();
		//W tym momencie mamy 4 linijki danych ( mog¹ byæ puste ), 
		//nastêpnie dwie linijki przerwy
		
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
		
		//Teraz hoursWithMinutes zawiera w ka¿dym elemencie godzinê 
		//i minuty dla poszczególnych sekji	
		hours = hoursWithMinutes;
		
		return true;
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie pojedynczej linijki z danymi czasowymi z surowych 
	 * danych otrzymanych w parametrze oraz poinformowanie o poprawnoœci tych danych
	 * @param infoHour linijka, w której bêdziemy poszukiwaæ informacji dotycz¹cych czasu
	 * @return informacja czy uda³o siê przygotowaæ poprawn¹ godzinê i minuty
	 */
	private boolean prepareHour(String infoHour)
	{
		//Do naprawy infoHour.matches......
		if(infoHour != null && !infoHour.equals(""))//&& infoHour.matches("\\d"))
		{

			storeLogger.info("Znalazlem godzinê odjazdu: ",infoHour);	
			return true;
		}
		else
		{
			storeLogger.warning("Godzina zawiera b³êdy!: ",infoHour);
			return false;
		}
	}
	
	/**
	 * Funkcja ma za zadanie wys³aæ przygotowane i sprawdzone wczeœniej dane do odpowiednich
	 * katalogów, pamiêtaj¹c o tym, ¿eby zapisaæ w³aœciwe godziny odjazdów oraz zale¿noœci
	 * pomiêdzy poszczególnymi przestankami, a liniami
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
			storeLogger.error("Nie uda³o mi siê utworzyæ folderu!",toSend.getParent());
		}
		
		try {
			if(!toSend.createNewFile())
			{
				storeLogger.error("Nie uda³o siê utworzyæ pliku!",fileName);
			}
			storeLogger.info("Utworzy³em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Wyj¹tek przy tworzeniu pliku!",fileName,e.getMessage());
		}

		try {
			input = new FileWriter(toSend);

			for(String s : hours)
			{
				input.write(s);
			}
			
			input.close();
			storeLogger.info("Nadpisa³em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Problem z zapisem do pliku!",fileName);
			e.printStackTrace();
		} 
		
		//Dodanie nazwy linii do linii danego przystanku
		
		fileName = "buStops/"+buStopName;
		toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			storeLogger.error("Nie uda³o siê utworzyæ pliku!",fileName);
		}
		
		try {
			if(!toSend.exists())
			{
				if(!toSend.createNewFile())
				{
					storeLogger.error("Nie uda³o siê utworzyæ pliku!",fileName);
				}
			}
			
			storeLogger.info("Utworzy³em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Rzuci³em wyj¹tek przy tworzeniu pliku!",fileName,
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
				input = new FileWriter(toSend,true); //chcemy nadpisywaæ
				input.write(lineNumber+direction+"\n");
				input.close();
			}
			
			output.close();
			storeLogger.info("Nadpisa³em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Problem z zapisem do pliku!",fileName);
			e.printStackTrace();
		}
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie pól, przygotowuj¹c je na przyjêcie oraz
	 * przetworzenie kolejnej porcji informacji
	 */
	private void clear()
	{
		storeLogger.execute();
		lineNumber = null;
		buStopName = null;
	}

	/**
	 * Funkcja otrzymuje paczkê danych wyodrêbnionych przez w¹tki wy³uskuj¹ce, ma za
	 * zadanie sprawdziæ poprawnoœæ tych danych, a je¿eli uda siê z nich wyci¹gn¹æ 
	 * przydatne informacje, zapisaæ je w katalogach z nazwami linii wraz z kierunkiem,
	 * w plikach o nazwach przystanków, dodatkowo tworz¹c katalog pomocniczy dla wyszukiwania
	 * zawieraj¹cy pliki z wszystkimi przystankami, w których znajduj¹ siê numery linii wraz
	 * z kierunkiem przeje¿dzaj¹ce przez dany przystanek.
	 * @param allInformations paczka danych, których poprawnoœæ musimy sprawdziæ oraz
	 *        wyci¹gn¹æ z nich przydatne informacje
	 */
	@Override
	public void storeInfo(Map<String,String> allInformations) {
		
		boolean wellPrepared = true;
		
		//Uruchamiamy funkcje, których zadaniem jest odpowiednie przygotowanie danych
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
		
		//Uruchamiamy funkcjê wysy³aj¹c¹, je¿eli dane zosta³y przygotowane prawid³owo
		if(wellPrepared)
		{
			sendInfos();
		}
		
		//Sprz¹tamy dla kolejnego u¿ycia
		clear();
	}
	
	/**
	 * Konstruktor sparametryzowany, przypisuj¹cy obiekt do sk³adowania logów.
	 * @param appender okreœla sposób sk³adowania logów
	 */
	FileStoreBusInfo(Appends appender)
	{
		storeLogger = new Logger();
		storeLogger.changeAppender(appender);
		storeLogger.info("Czas rozpocz¹æ pracê!");
		storeLogger.execute();
	}
	
	/**
	 * Konstruktor domyœlny, który ustala domyœlny sposób sk³adowania logów.
	 */
	FileStoreBusInfo()
	{
		this(new FileAppender("FileStore"));
	}
}