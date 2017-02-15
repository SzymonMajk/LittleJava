package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.util.ArrayList;
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
			return false;
		}
	}
	
	/**
	 * Zadaniem funkcji jest przygotowanie kierunku linii z surowych danych otrzymanych
	 * w parametrze oraz poinformowanie o poprawnoœci tych danych. Funkcja usuwa z numeru
	 * linii znaki ":", które s¹ u¿ywane przy sk³adowaniu danych
	 * @param infoDirection surowe dane dotycz¹ce kierunku linii
	 * @return informacja czy uda³o siê przygotowaæ poprawny kierunek linii
	 */
	private boolean prepareDirection(String infoDirection)
	{	
		if(infoDirection != null && !infoDirection.equals("") 
			&& infoDirection.contains("Do"))
		{
			infoDirection = infoDirection.replace(":", "");
			log4j.info("Znalaz³em kierunek linii:"+infoDirection);
			direction = infoDirection;
			return true;
		}
		else 
		{
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
		
		if(infoHours == null)
		{
			return false;
		}	
		
		//TODO Trzeba coœ powa¿niejszego wymyœleæ...
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
				hoursWithMinutes.add(readyLine);
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
				for(String s : hours)
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
	 * zadanie sprawdziæ poprawnoœæ tych danych, a je¿eli oka¿¹ siê podane poprawnie, 
	 * zapisaæ je w katalogach z nazwami linii wraz z kierunkiem, w plikach o nazwach 
	 * przystanków, dodatkowo tworz¹c katalog pomocniczy dla wyszukiwania zawieraj¹cy pliki
	 * o nazwach identycznych z nazwami przystanków, w których znajduj¹ siê numery linii wraz
	 * z kierunkiem przeje¿dzaj¹ce przez dany przystanek.
	 * @param allInformations mapa, której kluczami s¹ nazwy odpowiednich wyra¿eñ XPath,
	 * 		natomiast wartoœciami s¹ dane, które zosta³y wyodrêbnione przy pomocy wyra¿enia
	 * 		XPath o nazwie z klucza.
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
	 * Kostruktor domyœlny, który przypisuje dla sk³adowanych danych katalog Data, znajduj¹cy
	 * siê w katalogu programu.
	 */
	FileStoreBusInfo()
	{
		this("Data/");
	}
	
	/**
	 * Konstruktor pozwalaj¹cy na przypisanie œcie¿ki do katalogu, w którym maj¹ byæ sk³adowane
	 * przez ten obiekt dane. W przypadku nie istnienia katalogu lub katalogów nadrzêdnych,
	 * obiekt bêdzie w stanie go utworzyæ o ile bêdzie to mo¿liwe z poziomu aplikacji.
	 * @param toStoreDataDirectoryName œcie¿ka do katalogu, w którym maj¹ zostaæ zesk³adowane
	 * 		infomacje, nie powinien posiadaæ na koñcu znaku "/"
	 */
	FileStoreBusInfo(String toStoreDataDirectoryName)
	{
		this.toStoreDataDirectoryName = toStoreDataDirectoryName+"/";
	}
}