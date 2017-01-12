package pl.edu.agh.kis;

/**
 * Klasa ma za zadanie implementowa� metod� interfejsu StoreBusInfo, zapisuj�c
 * otrzymywane dane w katalogach, kt�rych nazwy sugeruj� numer linii, w plikach
 * kt�rych nazwy b�d� odpowiada�y nazwom przystank�w
 * @author Szymon Majkut
 * @version 1.0
 */
public class FileStoreBusInfo implements StoreBusInfo {

	/**
	 * W�asny system log�w
	 */
	private Logger storeLogger;
	
	/**
	 * Pole przechowuje numer aktualnie przetwarzanej linii
	 */
	private int lineNumber;
	
	/**
	 * Pole przechowuje nazw� aktualnie przetwarzanego przystanku
	 */
	private String buStopName;
	
	/**
	 * Funkcja otrzymuje paczk� informacji zesk�adowanych w stringu, jest to rozk�ad
	 * jednego przystanku dla jednej linii, przy czym te dane r�wnie� musz� si� tam
	 * znajdowa� w odpowiedniej konwencji
	 * @param allInformations paczka informacji, kt�re musimy zapisa� w jednym z plik�w
	 * 			opr�cz rozk�adu musi si� tam znajdowa� nazwa przystanku oraz numer linii
	 */
	@Override
	public void storeInfo(String allInformations) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Cia�o konstruktora obs�uguje przygotowania pod system log�w
	 */
	FileStoreBusInfo()
	{
		storeLogger = new Logger();
		storeLogger.changeAppender(new FileAppender("FileStore"));
	}

}
