package pl.edu.agh.kis.storeinfo;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Klasa ma za zadanie implementowa� metod� interfejsu StoreBusInfo, zapisuj�c otrzymywane
 * dane w katalogach, kt�rych nazwy sugeruj� numer linii wraz z kierunkiem, w plikach kt�rych
 * nazwy b�d� odpowiada�y nazwom przystank�w pozbawionych niedozwolonych znak�w oraz tworz�c
 * katalog buStops plik�w o nazwach przystank�w, w kt�rych znajd� si� numery linii 
 * przeje�dzaj�cych przez przystanek wraz z kierunkiem. Posiada konstruktor sparametryzowany,
 * kr�ry pozwala na okre�lenie katalogu nadrz�dnego dla sk�adowanych danych, oraz konstruktor
 * domy�lny, kt�ry jako taki katalog przypisuje katalog o nazwie Data. Je�eli kierunek linii
 * zawiera� w swojej nazwie znak ":", to zostanie on usuni�ty.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class FileStoreBusInfo implements StoreBusInfo {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(FileStoreBusInfo.class.getName());
	
	/**
	 * Przechowuje nazw� katalogu, w kt�rym b�dziemy sk�adowa� katalogi z danymi
	 */
	private String toStoreDataDirectoryName;
	
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
	 * Lista przechowuj�ca gotowe linie z godzinami oraz minutami oddzielone dwukropkami
	 * w formacie gotowym do zesk�adowania
	 */
	private ArrayList<String> readyTimeLines;
	
	/**
	 * Mapa obiekt�w s�u��cych do sprawdzania otrzymywanych danych
	 */
	private Map<String,CheckInformations> checkInformationImplementations;
	
	/**
	 * Funkcja odpowiada za wyczyszczenie p�l, przygotowuj�c je na przyj�cie oraz
	 * przetworzenie kolejnej porcji informacji
	 */
	private void clear()
	{
		lineNumber = "";
		buStopName = "";
		direction = "";
		readyTimeLines = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param implementationName
	 * @param storedInformation
	 * @return
	 */
	private boolean checkInformation(String implementationName, String storedInformation)
	{
		return checkInformationImplementations.get(implementationName).
				checkInformation(storedInformation);
	}
	
	/**
	 * 
	 * @param allInformations
	 * @return
	 */
	private boolean prepareNewInformations(Map<String,String> allInformations)
	{
		for(String l : allInformations.keySet())
		{
			if(!checkInformation(l,allInformations.get(l)))
			{
				return false;
			}
		}
				
		lineNumber = allInformations.get("lineNumber");
		buStopName = allInformations.get("buStopName");
		direction = allInformations.get("direction");
		
		StringBuilder timeLineBuilder;
		String[] hourLines = allInformations.get("hours").split("\n");
		String[] casualMinutesLines = allInformations.get("casualMinutes").split("\n");
		String[] saturdayMinutesLines = allInformations.get("saturdayMinutes").split("\n");
		String[] sundayMinutesLines = allInformations.get("sundayMinutes").split("\n");
		
		for(int i = 0; i < hourLines.length; ++i)
		{
			timeLineBuilder = new StringBuilder();
			timeLineBuilder.append(hourLines[i].replace(" ", ","));
			timeLineBuilder.append(casualMinutesLines[i].replace(" ", ","));
			timeLineBuilder.append(saturdayMinutesLines[i].replace(" ", ","));
			timeLineBuilder.append(sundayMinutesLines[i].replace(" ", ","));
			readyTimeLines.add(timeLineBuilder.toString());
		}
		
		return true;
	}
	
	/**
	 * W funkcji korzystam z faktu, �e gdy mamy dopisywa�, to wykonujemy to tylko w przypadku
	 * pliku pomocniczego, natomiast sk�aduj�c dane zawsze tworzymy nowy plik i zapisujemy
	 * do pliku i tak czystego, wi�c nie dopisujemy, w innym przypadku powinni�my rozdzieli�
	 * to co znajduje si� w bloku try z zapisywaniem na dwa osobne przypadki zale�ne od jeszcze
	 * innej zmiennej. Je�eli nie uda�o si� utworzy� pliku lub utworzy� funkcja ko�czy si�
	 * kt�rego� z nadkatalog�w funkcja zwr�ci warto�� fa�szu, podobnie przy wyst�pieniu wyj�tku
	 * przy zapisywaniu do pliku.
	 * @param filePath zabezpieczona uprzednio przed wyst�pieniem niedozwolonych znak�w 
	 * 		�cie�ka do pliku, je�eli kt�ry� z plik�w lub sam plik nie istnieje, s� 
	 * 		tworzone potrzebne katalogi oraz sam plik.
	 * @param append warto�� true okre�la, �e funkcja ma nadpisa� plik, kt�rego �cie�ka zosta�a
	 *      podana w pierwszym argumencie, warto�� fa�szu sprawia �e plik b�dzie zawsze 
	 *      zapisywany na nowo, dodatkowo okre�la ona rodzaj sk�adowanych informacji, 
	 *      poniewa� pozwala na to logika zachowania funkcji sendInfos().
	 * @return zwraca warto�� true, gdy plik, kt�rego �cie�ka zosta�a podana w pierwszym
	 * 		argumencie, zosta� poprawnie zapisany lub tylko nadpisany, w zale�no�ci od warto�ci
	 * 		drugiego argumentu, nastomiast w przypadku nie utworzenia pliku lub jego
	 * 		nadkatalog�w, w przypadku ich nieistnienia lub wyst�pienia b��du przy pr�bie zapisu
	 * 		do istniej�cego pliku, zwracana jest warto�� logicznego fa�szu.
	 */
	protected boolean sendInfoToFile(String filePath, boolean append)
	{
		boolean result = true;
		
		File toSend = new File(filePath);
		File parentToSend = new File(toSend.getParent());
		
		//cz�� odpowiedzialna za stworzenie w przypadku nieistnienia
		if(!parentToSend.mkdirs())
		{
			if(!parentToSend.exists())
			{
				log4j.error("Nie uda�o mi si� utworzy� folderu:"+parentToSend);
				return false;
			}
		}
		
		try {
			if(!toSend.createNewFile())
			{
				if(!toSend.exists())
				{
					log4j.error("Nie uda�o si� utworzy� pliku:"+filePath);
					return false;
				}
			}
			log4j.info("Utworzy�em plik:"+filePath);
		} catch (IOException e) {
			StringBuilder exceptionBuilder = new StringBuilder();
			exceptionBuilder.append("Wyj�tek przy tworzeniu pliku:");
			exceptionBuilder.append(filePath);
			exceptionBuilder.append(":");
			exceptionBuilder.append(e.getMessage());
			log4j.error(exceptionBuilder.toString());
			result = false;
		}
		
		//Teraz cz�� odpowiedzialna za zapis
		try (OutputStreamWriter input = 
				new OutputStreamWriter(new FileOutputStream(toSend,append),"UTF-8")) {
			if(append)
			{
				input.write(lineNumber+direction+"\n");
				log4j.info("Nadpisa�em plik:"+filePath);
			}
			else
			{
				input.write(buStopName+"\n");
				for(String s : readyTimeLines)
				{
					input.write(s+"\r\n");
				}
				log4j.info("Zapisa�em plik:"+filePath);
			}
		} catch (IOException e) {
			StringBuilder exceptionBuilder = new StringBuilder();
			exceptionBuilder.append("Problem z zapisem do pliku:");
			exceptionBuilder.append(filePath);
			exceptionBuilder.append(":");
			exceptionBuilder.append(e.getMessage());
			log4j.error(exceptionBuilder.toString());
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Funkcja ma za zadanie przypisa� zawarto�� pliku, kt�rego �cie�ka jest podawana
	 * w pierwszym argumencie do pami�ci przy pomocy obiektu StringBuilder, aby nast�pnie
	 * sprawdzi� czy w utworzonym ci�gu znak�w znajduje si� ten okre�lony w drugim parametrze.
	 * Funkcja zosta�a zaprojektowana dla plik�w pomocniczych, przechowuj�cych dane dotycz�ce
	 * numer�w linii wraz z kierunkami przeje�dzaj�cymi przez dany przystanek, z tej racji
	 * mamy do czynienia z du�� ilo�ci� niewielkich plik�w i mo�liwe jest zastosowanie takiego
	 * rozwi�zania.
	 * @param filePath zabezpieczona przed wyst�pieniem niedozwolonych znak�w �cie�ka do pliku,
	 * 		w funkcji sprawdzamy istnienie takiego pliku.
	 * @param toCheck obiekt String, zawarty w nim ci�g znak�w, ma zosta� sprawdzony przez
	 * 		funkcj� pod k�tem wyst�powania w pliku, kt�rego �cie�ka zosta�a podana w pierwszym
	 * 		argumencie.
	 * @return zwraca prawd�, je�eli plik istnia� oraz zawiera� ci�g znak�w zawarty w obiekcie
	 * 		String z drugiego argumentu, w przypadku nieistnienia pliku lub niezawierania
	 * 		w nim podanego ci�gu znak�w, zwracana jest warto�� logicznego fa�szu.
	 */
	protected boolean checkIfFileContains(String filePath, String toCheck)
	{
		File from = new File(filePath);
		StringBuilder builder = new StringBuilder();
		
		if(from.exists())
		{
			try (BufferedReader output = new BufferedReader(
					new InputStreamReader(new FileInputStream(from),"UTF-8"))) {
				
				String line = "";
				builder = new StringBuilder(line);
				
				while((line = output.readLine()) != null)
				{
					builder.append(line);
				}
				
			} catch (IOException e) {
				StringBuilder exceptionBuilder = new StringBuilder();
				exceptionBuilder.append("Problem z odczytem z pliku:");
				exceptionBuilder.append(filePath);
				exceptionBuilder.append(":");
				exceptionBuilder.append(e.getMessage());
				log4j.error(exceptionBuilder.toString());
			}
			
			return builder.toString().contains(toCheck);
		}
		
		return false;
	}
	
	/**
	 * Funkcja ma za zadanie zek�adowa� dane otrzymane przez obiekt w plikach, na pocz�tku 
	 * tworzy nazw� �cie�ki do pliku z danymi, kt�ra sk�ada si� ze �cie�ki podanej przez
	 * u�ytkownika, katalogu z numerem linii oraz nazw� kierunku, sam plik nazywaj�c nazw�
	 * przystanku. Wszystkie nazwy plik�w oraz katalog�w zostaj� sprawdzone pod k�tem znak�w
	 * niedozwolonych, a nast�pnie znaki te je�eli trzeba s� usuwane. Wyszukiwarki musz�
	 * bra� pod uwag� brak znak�w niedozwolonych przy sk�adanych danych. W pierwszej linii 
	 * pliku z danymi zapisujemy pe�n� nazw� przystanku, a nast�pnie godziny odjazd�w. Funkcja
	 * wczytuje plik z katalogu pomocniczego buStops z pliku o tej samej nazwie, sprawdza czy 
	 * plik pomocniczy posiada ju� podan� lini� wraz z nazw� kierunku, je�eli nie dopisuje 
	 * okre�lony numer linii oraz kierunek do pliku pomocniczego.
	 */
	protected void sendInfos()
	{	
		
		String safeToStoreDataDirectoryName 
			= toStoreDataDirectoryName.replaceAll("[.\\:*?\"|<>]", "");
		String safeLineNumber = lineNumber.replaceAll("[\\/.\\:*?\"|<>]", "");
		String safeDirection = direction.replaceAll("[\\/.\\:*?\"|<>]", "");
		String safeBuStopName = buStopName.replaceAll("[\\/.\\:*?\"|<>]", "");
		
		StringBuilder builder = new StringBuilder();
		builder.append(safeToStoreDataDirectoryName);
		builder.append(safeLineNumber);
		builder.append(safeDirection);
		builder.append("/");
		builder.append(safeBuStopName);
		
		if(sendInfoToFile(builder.toString(),false))
		{
			builder = new StringBuilder();
			builder.append(safeToStoreDataDirectoryName);
			builder.append("buStops/");
			builder.append(safeBuStopName);
			
			if(!checkIfFileContains(builder.toString(),lineNumber+direction))
			{
				if(!sendInfoToFile(builder.toString(),true))
				{
					log4j.error("Nast�pi� problem przy nadpisaniu pliku:"
							+builder.toString());
				}
			}
		}
		else
		{
			log4j.error("Nast�pi� problem przy zapisie do pliku:",builder.toString());
		}
	}

	
	/**
	 * @param allInformations
	 */
	@Override
	public void storeInfo(Map<String,String> allInformations) {
		
		if(prepareNewInformations(allInformations))
		{
			sendInfos();
			clear();
		}
	}
	
	/**
	 * Konstruktor domy�lny, wywo�uj�cy konstruktor sparamertryzowany FileStoreBusInfo(String),
	 * przypisuj�cy sk�adowanym danym katalog nadrz�dny Data. Pliki tworzone w czasie
	 * aktualizacji danych b�d� znajdowa�y si� w katalogu Data, znajduj�cym si� w katalogu
	 * g��wnym programu.
	 */
	public FileStoreBusInfo()
	{
		this("Data/");
	}
	
	/**
	 * 
	 * @param toStoreDataDirectoryName nazwa katalogu, w kt�rym maj� zosta� zesk�adowane
	 * 		dane. Utworzony katalog b�dzie znajdowa� si� w katalogu projektu, je�eli
	 * 		w argumencie znajd� si� znaki niedozwolne dla nazwy pliku, zostan� usuni�te.
	 */
	public FileStoreBusInfo(String toStoreDataDirectoryName)
	{
		this.toStoreDataDirectoryName = toStoreDataDirectoryName+"/";
		readyTimeLines = new ArrayList<String>();
		checkInformationImplementations = new HashMap<String,CheckInformations>();
		checkInformationImplementations.put("buStopName", new CheckBuStopName());
		checkInformationImplementations.put("lineNumber", new CheckLineNumber());
		checkInformationImplementations.put("direction", new CheckDirection());
		CheckInformations checkTime = new CheckTimeArray();
		checkInformationImplementations.put("hours",checkTime);
		checkInformationImplementations.put("casualMinutes",checkTime);
		checkInformationImplementations.put("saturdayMinutes",checkTime);
		checkInformationImplementations.put("sundayMinutes",checkTime);
	}
}