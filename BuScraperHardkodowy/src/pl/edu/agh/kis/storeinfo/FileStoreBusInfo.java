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
 * Klasa ma za zadanie implementowaæ metodê interfejsu StoreBusInfo, zapisuj¹c otrzymywane
 * dane w katalogach, których nazwy sugeruj¹ numer linii wraz z kierunkiem, w plikach których
 * nazwy bêd¹ odpowiada³y nazwom przystanków pozbawionych niedozwolonych znaków oraz tworz¹c
 * katalog buStops plików o nazwach przystanków, w których znajd¹ siê numery linii 
 * przeje¿dzaj¹cych przez przystanek wraz z kierunkiem. Posiada konstruktor sparametryzowany,
 * króry pozwala na okreœlenie katalogu nadrzêdnego dla sk³adowanych danych, oraz konstruktor
 * domyœlny, który jako taki katalog przypisuje katalog o nazwie Data. Je¿eli kierunek linii
 * zawiera³ w swojej nazwie znak ":", to zostanie on usuniêty.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class FileStoreBusInfo implements StoreBusInfo {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(FileStoreBusInfo.class.getName());
	
	/**
	 * Przechowuje nazwê katalogu, w którym bêdziemy sk³adowaæ katalogi z danymi
	 */
	private String toStoreDataDirectoryName;
	
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
	 * Lista przechowuj¹ca gotowe linie z godzinami oraz minutami oddzielone dwukropkami
	 * w formacie gotowym do zesk³adowania
	 */
	private ArrayList<String> readyTimeLines;
	
	/**
	 * Mapa obiektów s³u¿¹cych do sprawdzania otrzymywanych danych
	 */
	private Map<String,CheckInformations> checkInformationImplementations;
	
	/**
	 * Funkcja odpowiada za wyczyszczenie pól, przygotowuj¹c je na przyjêcie oraz
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
	 * W funkcji korzystam z faktu, ¿e gdy mamy dopisywaæ, to wykonujemy to tylko w przypadku
	 * pliku pomocniczego, natomiast sk³aduj¹c dane zawsze tworzymy nowy plik i zapisujemy
	 * do pliku i tak czystego, wiêc nie dopisujemy, w innym przypadku powinniœmy rozdzieliæ
	 * to co znajduje siê w bloku try z zapisywaniem na dwa osobne przypadki zale¿ne od jeszcze
	 * innej zmiennej. Je¿eli nie uda³o siê utworzyæ pliku lub utworzyæ funkcja koñczy siê
	 * któregoœ z nadkatalogów funkcja zwróci wartoœæ fa³szu, podobnie przy wyst¹pieniu wyj¹tku
	 * przy zapisywaniu do pliku.
	 * @param filePath zabezpieczona uprzednio przed wyst¹pieniem niedozwolonych znaków 
	 * 		œcie¿ka do pliku, je¿eli któryœ z plików lub sam plik nie istnieje, s¹ 
	 * 		tworzone potrzebne katalogi oraz sam plik.
	 * @param append wartoœæ true okreœla, ¿e funkcja ma nadpisaæ plik, którego œcie¿ka zosta³a
	 *      podana w pierwszym argumencie, wartoœæ fa³szu sprawia ¿e plik bêdzie zawsze 
	 *      zapisywany na nowo, dodatkowo okreœla ona rodzaj sk³adowanych informacji, 
	 *      poniewa¿ pozwala na to logika zachowania funkcji sendInfos().
	 * @return zwraca wartoœæ true, gdy plik, którego œcie¿ka zosta³a podana w pierwszym
	 * 		argumencie, zosta³ poprawnie zapisany lub tylko nadpisany, w zale¿noœci od wartoœci
	 * 		drugiego argumentu, nastomiast w przypadku nie utworzenia pliku lub jego
	 * 		nadkatalogów, w przypadku ich nieistnienia lub wyst¹pienia b³êdu przy próbie zapisu
	 * 		do istniej¹cego pliku, zwracana jest wartoœæ logicznego fa³szu.
	 */
	protected boolean sendInfoToFile(String filePath, boolean append)
	{
		boolean result = true;
		
		File toSend = new File(filePath);
		File parentToSend = new File(toSend.getParent());
		
		//czêœæ odpowiedzialna za stworzenie w przypadku nieistnienia
		if(!parentToSend.mkdirs())
		{
			if(!parentToSend.exists())
			{
				log4j.error("Nie uda³o mi siê utworzyæ folderu:"+parentToSend);
				return false;
			}
		}
		
		try {
			if(!toSend.createNewFile())
			{
				if(!toSend.exists())
				{
					log4j.error("Nie uda³o siê utworzyæ pliku:"+filePath);
					return false;
				}
			}
			log4j.info("Utworzy³em plik:"+filePath);
		} catch (IOException e) {
			StringBuilder exceptionBuilder = new StringBuilder();
			exceptionBuilder.append("Wyj¹tek przy tworzeniu pliku:");
			exceptionBuilder.append(filePath);
			exceptionBuilder.append(":");
			exceptionBuilder.append(e.getMessage());
			log4j.error(exceptionBuilder.toString());
			result = false;
		}
		
		//Teraz czêœæ odpowiedzialna za zapis
		try (OutputStreamWriter input = 
				new OutputStreamWriter(new FileOutputStream(toSend,append),"UTF-8")) {
			if(append)
			{
				input.write(lineNumber+direction+"\n");
				log4j.info("Nadpisa³em plik:"+filePath);
			}
			else
			{
				input.write(buStopName+"\n");
				for(String s : readyTimeLines)
				{
					input.write(s+"\r\n");
				}
				log4j.info("Zapisa³em plik:"+filePath);
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
	 * Funkcja ma za zadanie przypisaæ zawartoœæ pliku, którego œcie¿ka jest podawana
	 * w pierwszym argumencie do pamiêci przy pomocy obiektu StringBuilder, aby nastêpnie
	 * sprawdziæ czy w utworzonym ci¹gu znaków znajduje siê ten okreœlony w drugim parametrze.
	 * Funkcja zosta³a zaprojektowana dla plików pomocniczych, przechowuj¹cych dane dotycz¹ce
	 * numerów linii wraz z kierunkami przeje¿dzaj¹cymi przez dany przystanek, z tej racji
	 * mamy do czynienia z du¿¹ iloœci¹ niewielkich plików i mo¿liwe jest zastosowanie takiego
	 * rozwi¹zania.
	 * @param filePath zabezpieczona przed wyst¹pieniem niedozwolonych znaków œcie¿ka do pliku,
	 * 		w funkcji sprawdzamy istnienie takiego pliku.
	 * @param toCheck obiekt String, zawarty w nim ci¹g znaków, ma zostaæ sprawdzony przez
	 * 		funkcjê pod k¹tem wystêpowania w pliku, którego œcie¿ka zosta³a podana w pierwszym
	 * 		argumencie.
	 * @return zwraca prawdê, je¿eli plik istnia³ oraz zawiera³ ci¹g znaków zawarty w obiekcie
	 * 		String z drugiego argumentu, w przypadku nieistnienia pliku lub niezawierania
	 * 		w nim podanego ci¹gu znaków, zwracana jest wartoœæ logicznego fa³szu.
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
	 * Funkcja ma za zadanie zek³adowaæ dane otrzymane przez obiekt w plikach, na pocz¹tku 
	 * tworzy nazwê œcie¿ki do pliku z danymi, która sk³ada siê ze œcie¿ki podanej przez
	 * u¿ytkownika, katalogu z numerem linii oraz nazw¹ kierunku, sam plik nazywaj¹c nazw¹
	 * przystanku. Wszystkie nazwy plików oraz katalogów zostaj¹ sprawdzone pod k¹tem znaków
	 * niedozwolonych, a nastêpnie znaki te je¿eli trzeba s¹ usuwane. Wyszukiwarki musz¹
	 * braæ pod uwagê brak znaków niedozwolonych przy sk³adanych danych. W pierwszej linii 
	 * pliku z danymi zapisujemy pe³n¹ nazwê przystanku, a nastêpnie godziny odjazdów. Funkcja
	 * wczytuje plik z katalogu pomocniczego buStops z pliku o tej samej nazwie, sprawdza czy 
	 * plik pomocniczy posiada ju¿ podan¹ liniê wraz z nazw¹ kierunku, je¿eli nie dopisuje 
	 * okreœlony numer linii oraz kierunek do pliku pomocniczego.
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
					log4j.error("Nast¹pi³ problem przy nadpisaniu pliku:"
							+builder.toString());
				}
			}
		}
		else
		{
			log4j.error("Nast¹pi³ problem przy zapisie do pliku:",builder.toString());
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
	 * Konstruktor domyœlny, wywo³uj¹cy konstruktor sparamertryzowany FileStoreBusInfo(String),
	 * przypisuj¹cy sk³adowanym danym katalog nadrzêdny Data. Pliki tworzone w czasie
	 * aktualizacji danych bêd¹ znajdowa³y siê w katalogu Data, znajduj¹cym siê w katalogu
	 * g³ównym programu.
	 */
	public FileStoreBusInfo()
	{
		this("Data/");
	}
	
	/**
	 * 
	 * @param toStoreDataDirectoryName nazwa katalogu, w którym maj¹ zostaæ zesk³adowane
	 * 		dane. Utworzony katalog bêdzie znajdowa³ siê w katalogu projektu, je¿eli
	 * 		w argumencie znajd¹ siê znaki niedozwolne dla nazwy pliku, zostan¹ usuniête.
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