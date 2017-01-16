package pl.edu.agh.kis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Klasa ma za zadanie implementowaæ metodê interfejsu StoreBusInfo, zapisuj¹c
 * otrzymywane dane w katalogach, których nazwy sugeruj¹ numer linii, w plikach
 * których nazwy bêd¹ odpowiada³y nazwom przystanków
 * @author Szymon Majkut
 * @version 1.1b
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
	 * Pole przechowuje listê wierszy, z których bêdzie siê sk³adaæ tabela odjazdów 
	 */
	private Queue<HourLine> departHours = new PriorityQueue<HourLine>();
		
	/**
	 * Zmienna okreœlaj¹ca mo¿liwoœæ wys³ania danych je¿eli s¹ poprawne
	 */
	private boolean readyToSend;
	
	/**
	 * Funkcja otrzymuje paczkê informacji do przetworzenia, po czym analizuje ka¿dy
	 * wiersz w poszukiwaniu odpowiednich informacji, zapamiêtuje informacje i zapisuje
	 * do logów wszelkie niezgodnoœci z za³o¿eniami, je¿eli otrzyma wszystkie niezbêdne
	 * informacje dopuœci do zesk³adowania danych w przeciwnym wypadku poprzez logi
	 * powiadomi o b³êdach
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
					storeLogger.info("Znalaz³em nazwê linii: ",name);
					buStopName = name;
					alreadyNamed = true;				}
				else 
				{
					storeLogger.warning("Nazwa linii zawiera b³êdy!: ",name);
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
					storeLogger.info("Znalaz³em numer linii: ",number);
					lineNumber = number;
					alreadySetLine = true;				}
				else
				{
					storeLogger.warning("Numer linii zawiera b³êdy!: ",number);
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
					storeLogger.info("Znalazlem godzinê odjazdu: ",hour);
					departHours.add(new HourLine(hour));
					atLeastOneHour = true;				}
				else
				{
					storeLogger.warning("Godzina zawiera b³êdy zawiera b³êdy!: ",hour);
				}
			}
			else
			{
				storeLogger.warning("Niepoprawne dane wejœciowe: ",s);
			}
		}
		
		if((alreadyNamed && alreadySetLine && atLeastOneHour))
		{
			readyToSend = true;
		}
		else
		{
			storeLogger.error("Niedopuszczam do wys³ania danych: ",allInformations);
		}
		
		storeLogger.execute();
	}
	
	/**
	 * Funkcja ma za zadanie wys³aæ przygotowane wczeœniej dane okreœlaj¹c ich po³o¿enia
	 * poprzez numer linii oraz nazwê przystanku, zabezpieczaj¹c siê zmienn¹ readyToSend
	 * mamy pewnoœæ, ¿e lineNumber oraz buStopName s¹ prawid³owymi obiektami typu String
	 */
	private void sendInfos()
	{
		String fileName = lineNumber+"/"+buStopName;
		File toSend = new File(fileName);
		new File(toSend.getParent()).mkdir();
		try {
			toSend.createNewFile();
			storeLogger.info("Utworzy³em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Nie uda³o siê utworzyæ pliku!",fileName);
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
			storeLogger.info("Nadpisa³em plik!",fileName);
		} catch (IOException e) {
			storeLogger.error("Problem z zapisem do pliku!",fileName);
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Funkcja odpowiada za wyczyszczenie pól, przygotowuj¹c je na przyjêcie kolejnej
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
	 * Funkcja otrzymuje paczkê informacji zesk³adowanych w stringu, jest to rozk³ad
	 * jednego przystanku dla jednej linii, przy czym te dane równie¿ musz¹ siê tam
	 * znajdowaæ w odpowiedniej konwencji
	 * @param allInformations paczka informacji, które musimy zapisaæ w jednym z plików
	 * 			oprócz rozk³adu musi siê tam znajdowaæ nazwa przystanku oraz numer linii
	 */
	@Override
	public void storeInfo(String allInformations) {
		//Uruchamiamy funkcjê, której zadaniem jest odpowiednie przygotowanie danych
		prepareInfos(allInformations);
		
		//Uruchamiamy funkcjê wysy³aj¹c¹, je¿eli dane zosta³y przygotowane prawid³owo
		if(readyToSend)
		{
			sendInfos();
		}
		
		//Sprz¹tamy dla kolejnego u¿ycia
		clear();
	}
	
	/**
	 * Konstruktor sparametryzowany, pozwalaj¹cy na okreœlenie sposoby sk³adowana
	 * logów, stworzony dla u³atwienia sprz¹tania po testach
	 */
	FileStoreBusInfo(Appends appender)
	{
		storeLogger = new Logger();
		storeLogger.changeAppender(appender);
		storeLogger.info("Czas rozpocz¹æ pracê!");
		storeLogger.execute();
	}
	
	/**
	 * Konstruktor domyœlny, który obs³uguje domyœlne przygotowania pod system logów
	 */
	FileStoreBusInfo()
	{
		this(new FileAppender("FileStore"));
	}
}

/**
 * Pomocnicza klasa u³atwiaj¹ca przetrzymywanie linii danych dotycz¹cych
 * godzin odjazdu, uwzglêdniaj¹c to w którym typie dnia siê znajduj¹
 * @author Szymon Majkut
 * @version 1.0
 *
 */
class HourLine implements Comparable<HourLine> {
	
	private char typeOfDay;
	
	private String hour;
	
	private String minutes;
	
	/**
	 * Prosty getter pobieraj¹cy zararoœæ pola typeOfDay
	 * @return zawartoœæ pola typeOfDay
	 */
	public int getTypeOfDay()
	{
		return typeOfDay;
	}
	
	/**
	 * Funkcja zwracaj¹ca godzinê oraz minuty odjazdu odzielane przecinkami
	 * @return godzina odjazdu oraz odpowiadaj¹ce jej minuty
	 */
	public String printHour()
	{
		return hour + ": " + minutes+"\n";
	}
	
	/**
	 * Konstruktor sparametryzowany, pozwalaj¹cy na przypisanie odpowiednich
	 * danych z otrzymanej linii godzin
	 * @param line linia minut dla danej godziny z uwzglêdnieniem typu dnia
	 */
	HourLine(String line)
	{
		typeOfDay = line.charAt(0);
		hour = line.substring(1,3);
		minutes = line.substring(4);
	}

	/**
	 * Implementacja comparatora, porównuje linie godzin odjazdów zgodnie
	 * z wartoœci¹ godzin
	 * @param o drugi obiekt HourLine do porównania
	 * @return wartoœæ okreœlaj¹ca wzajemn¹ relacjê obiektów w sferze porównania
	 */
	@Override
	public int compareTo(HourLine o) {
		return this.hour.compareTo(o.hour);
	}
}
