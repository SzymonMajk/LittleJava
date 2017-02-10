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
 * Klasa ma za zadanie implementowa� metod� interfejsu StoreBusInfo, zapisuj�c otrzymywane
 * dane w katalogach, kt�rych nazwy sugeruj� numer linii, w plikach kt�rych nazwy b�d�
 * odpowiada�y nazwom przystank�w oraz tworz�c katalog plik�w o nazwach przystank�w, 
 * w kt�rych znajd� si� numery linii przeje�dzaj�cych przez przystanek wraz z kierunkiem.
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
			log4j.info("Znalaz�em nazw� linii:"+infoBuStopName);
			buStopName = infoBuStopName;
			return true;
		}
		else 
		{
			log4j.warn("Nazwa linii zawiera b��dy!:"+infoBuStopName);
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
			log4j.info("Znalaz�em numer linii:"+infoLineNumber);
			lineNumber = infoLineNumber;
			return true;
		}
		else
		{
			log4j.warn("Numer linii zawiera b��dy!:"+infoLineNumber);
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
			log4j.info("Znalaz�em kierunek linii:"+infoDirection);
			direction = infoDirection;
			return true;
		}
		else 
		{
			log4j.warn("Kierunek linii zawiera b��dy!:"+infoDirection);
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

			log4j.info("Znalazlem godzin� odjazdu:"+infoHour);	
			return true;
		}
		else
		{
			log4j.warn("Godzina zawiera b��dy!:"+infoHour);
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
		BufferedReader output = null;
		Writer input = null;
		String line;
		StringBuilder builder = new StringBuilder();
		
		fileName = fileName.replace("\"", "");
		
		File toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			log4j.error("Nie uda�o mi si� utworzy� folderu!"+toSend.getParent());
		}
		
		try {
			if(!toSend.createNewFile())
			{
				log4j.error("Nie uda�o si� utworzy� pliku!",fileName);
			}
			log4j.info("Utworzy�em plik!"+fileName);
		} catch (IOException e) {
			log4j.error("Wyj�tek przy tworzeniu pliku!"+fileName,e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa�ny wyj�tek przy tworzeniu pliku!"+fileName,t.getMessage());
		} 

		try {
			input = new OutputStreamWriter(new FileOutputStream(toSend),"UTF-8");

			for(String s : hours)
			{
				input.write(s);
			}
			
			log4j.info("Nadpisa�em plik!"+fileName);
		} catch (IOException e) {
			log4j.error("Problem z zapisem do pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa�ny problem z zapisaniem do pliku!"+fileName+t.getMessage());
		} finally {
			if(input != null)
			{
				try {
					input.close();
				} catch (IOException e) {
					log4j.warn("Niepoprawnie zamkni�ty strumie�!"+e.getMessage());
				}
			}
		} 
		
		//Dodanie nazwy linii do linii danego przystanku
		
		fileName = "buStops/"+buStopName;
		fileName = fileName.replace("\"", "");

		toSend = new File(fileName);
		if(!new File(toSend.getParent()).mkdir())
		{
			log4j.error("Nie uda�o si� utworzy� pliku!"+fileName);
		}
		
		try {
			if(!toSend.exists())
			{
				if(!toSend.createNewFile())
				{
					log4j.error("Nie uda�o si� utworzy� pliku!"+fileName);
				}
			}
			
			log4j.info("Utworzy�em plik!"+fileName);
		} catch (IOException e) {
			log4j.error("Wyj�tek przy tworzeniu pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa�ny wyj�tek przy tworzeniu pliku!"+fileName+t.getMessage());
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
			log4j.error("Powa�ny problem z zapisaniem do pliku!"+fileName+t.getMessage());
		} finally {
			if(output != null)
			{
				try {
					output.close();
				} catch (IOException e) {
					log4j.warn("Niepoprawnie zamkni�ty strumie�!");
				}
			}
		}
		
		try {
			
			input = new FileWriter(toSend,true); //chcemy nadpisywa�

			if(!builder.toString().contains(lineNumber+direction))
			{
				input.write(lineNumber+direction+"\n");
			}
			
			log4j.info("Nadpisa�em plik!",fileName);
		} catch (IOException e) {
			log4j.error("Problem z zapisem do pliku!"+fileName+e.getMessage());
		} catch (Throwable t) {
			log4j.error("Powa�ny problem z zapisaniem do pliku!"+fileName+t.getMessage());
		} finally {
			if(input != null)
			{
				try {
					input.close();
				} catch (IOException e) {
					log4j.warn("Niepoprawnie zamkni�ty strumie�!");
				}
			}
		}
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie p�l, przygotowuj�c je na przyj�cie oraz
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
}