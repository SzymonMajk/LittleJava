package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(FileStoreBusInfo.class.getName());
	
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
			log4j.info("Znalaz³em nazwê linii:"+infoBuStopName);
			buStopName = infoBuStopName;
			return true;
		}
		else 
		{
			log4j.warn("Nazwa linii zawiera b³êdy!:"+infoBuStopName);
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
			log4j.info("Znalaz³em numer linii:"+infoLineNumber);
			lineNumber = infoLineNumber;
			return true;
		}
		else
		{
			log4j.warn("Numer linii zawiera b³êdy!:"+infoLineNumber);
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
			log4j.info("Znalaz³em kierunek linii:"+infoDirection);
			direction = infoDirection;
			return true;
		}
		else 
		{
			log4j.warn("Kierunek linii zawiera b³êdy!:"+infoDirection);
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

			log4j.info("Znalazlem godzinê odjazdu:"+infoHour);	
			return true;
		}
		else
		{
			log4j.warn("Godzina zawiera b³êdy!:"+infoHour);
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
		BufferedReader output = null;
		Writer input = null;
		String line;
		StringBuilder builder = new StringBuilder();
		
		fileName = fileName.replace("\"", "");
		
		File toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			log4j.error("Nie uda³o mi siê utworzyæ folderu!"+toSend.getParent());
		}
		
		try {
			if(!toSend.createNewFile())
			{
				log4j.error("Nie uda³o siê utworzyæ pliku!",fileName);
			}
			log4j.info("Utworzy³em plik!"+fileName);
		} catch (IOException e) {
			log4j.error("Wyj¹tek przy tworzeniu pliku!"+fileName,e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa¿ny wyj¹tek przy tworzeniu pliku!"+fileName,t.getMessage());
		} 

		try {
			input = new OutputStreamWriter(new FileOutputStream(toSend),"UTF-8");

			for(String s : hours)
			{
				input.write(s);
			}
			
			log4j.info("Nadpisa³em plik!"+fileName);
		} catch (IOException e) {
			log4j.error("Problem z zapisem do pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa¿ny problem z zapisaniem do pliku!"+fileName+t.getMessage());
		} finally {
			if(input != null)
			{
				try {
					input.close();
				} catch (IOException e) {
					log4j.warn("Niepoprawnie zamkniêty strumieñ!"+e.getMessage());
				}
			}
		} 
		
		//Dodanie nazwy linii do linii danego przystanku
		
		fileName = "buStops/"+buStopName;
		fileName = fileName.replace("\"", "");

		toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			log4j.error("Nie uda³o siê utworzyæ pliku!"+fileName);
		}
		
		try {
			if(!toSend.exists())
			{
				if(!toSend.createNewFile())
				{
					log4j.error("Nie uda³o siê utworzyæ pliku!"+fileName);
				}
			}
			
			log4j.info("Utworzy³em plik!"+fileName);
		} catch (IOException e) {
			log4j.error("Wyj¹tek przy tworzeniu pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa¿ny wyj¹tek przy tworzeniu pliku!"+fileName+t.getMessage());
		} 

		try {
			
			output = new BufferedReader(
					new InputStreamReader(new FileInputStream(toSend),"UTF-8"));
			
			line = "";
			builder = new StringBuilder(line);
			
			while((line = output.readLine()) != null)
			{
				builder.append(line);
			}
			
		} catch (IOException e) {
			log4j.error("Problem z zapisem do pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa¿ny problem z zapisaniem do pliku!"+fileName+t.getMessage());
		} finally {
			if(output != null)
			{
				try {
					output.close();
				} catch (IOException e) {
					log4j.warn("Niepoprawnie zamkniêty strumieñ!");
				}
			}
		}
		
		try {
			
			input = new FileWriter(toSend,true); //chcemy nadpisywaæ

			if(!builder.toString().contains(lineNumber+direction))
			{
				input.write(lineNumber+direction+"\n");
			}
			
			log4j.info("Nadpisa³em plik!",fileName);
		} catch (IOException e) {
			log4j.error("Problem z zapisem do pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa¿ny problem z zapisaniem do pliku!"+fileName+t.getMessage());
		} finally {
			if(input != null)
			{
				try {
					input.close();
				} catch (IOException e) {
					log4j.warn("Niepoprawnie zamkniêty strumieñ!");
				}
			}
		}
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie pól, przygotowuj¹c je na przyjêcie oraz
	 * przetworzenie kolejnej porcji informacji
	 */
	private void clear()
	{
		lineNumber = "";
		buStopName = "";
		direction = "";
		hours = new ArrayList<String>();
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
}