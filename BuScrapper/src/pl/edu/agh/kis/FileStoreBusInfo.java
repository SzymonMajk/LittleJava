package pl.edu.agh.kis;

/**
 * Klasa ma za zadanie implementowaæ metodê interfejsu StoreBusInfo, zapisuj¹c
 * otrzymywane dane w katalogach, których nazwy sugeruj¹ numer linii, w plikach
 * których nazwy bêd¹ odpowiada³y nazwom przystanków
 * @author Szymon Majkut
 * @version 1.0
 */
public class FileStoreBusInfo implements StoreBusInfo {

	/**
	 * W³asny system logów
	 */
	private Logger storeLogger;
	
	/**
	 * Pole przechowuje numer aktualnie przetwarzanej linii
	 */
	private int lineNumber;
	
	/**
	 * Pole przechowuje nazwê aktualnie przetwarzanego przystanku
	 */
	private String buStopName;
	
	/**
	 * Funkcja otrzymuje paczkê informacji zesk³adowanych w stringu, jest to rozk³ad
	 * jednego przystanku dla jednej linii, przy czym te dane równie¿ musz¹ siê tam
	 * znajdowaæ w odpowiedniej konwencji
	 * @param allInformations paczka informacji, które musimy zapisaæ w jednym z plików
	 * 			oprócz rozk³adu musi siê tam znajdowaæ nazwa przystanku oraz numer linii
	 */
	@Override
	public void storeInfo(String allInformations) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Cia³o konstruktora obs³uguje przygotowania pod system logów
	 */
	FileStoreBusInfo()
	{
		storeLogger = new Logger();
		storeLogger.changeAppender(new FileAppender("FileStore"));
	}

}
