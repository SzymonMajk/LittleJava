package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa implementuj�ca interfejs Downloader, umo�liwiaj�ca inicjowanie, oraz
 * udost�pnianie strumieni do plik�w, jest przeznaczona g��wnie do test�w.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class FileDownloader implements Downloader {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(FileDownloader.class.getName());
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wej�cia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wyj�cia do hosta
	 */
	private OutputStream output;
	
	/**
	 * Pole przechowuj�ce nazw� pliku dla strumienia wej�ciowego
	 */
	private String inputFileName;
	
	/**
	 * Pole przechowuj�ce nazw� pliku dla strumienia wyj�ciowego
	 */
	private String outputFileName;
	
	/**
	 * Funkcja ma za zadanie zwraca� strumie� wyj�ciowy przechowywany w polu prywatnym.
	 * @return strumie� wyj�ciowy przechowywany w polu prywatnym
	 */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Funkcja ma za zadanie zwraca� strumie� wej�ciowy przechowywany w polu prywatnym.
	 * @return strumie� wyj�ciowy przechowywany w polu prywatnym
	 */
	@Override
	public InputStream getInputSteam() {
		return input;
	}
	
	/**
	 * Zadaniem funkcji jest otworzenie strumieni dla plik�w podanych w konstruktorze.
	 * @param hostName w tej implementacji nie jest u�ywany
	 * @return informacja o powodzeniu utworzenia strumieni do plik�w
	 */
	@Override
	public boolean initDownloader(String hostName) {
		boolean result = true;
		
		try {
			input = new FileInputStream(inputFileName);
			output= new FileOutputStream(outputFileName);
		} catch (FileNotFoundException e) {
			log4j.error("Nast�pi� problem podczas ustalania stanu pocz�tkowego strumieni:"+
					e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest poprawne przypisanie nazw plik�w
	 * odpowiedzialnych za dane przychodz�ce oraz dane wysy�ane ( dane przychodz�ce s�
	 * czytane z pliku inputFileName, natomiast wysy�ane s� do pliku outputFileName ).
	 * @param inputFileName nazwa pliku odpowiedzialnego za dane przychodz�ce
	 * @param outputFileName nazwa pliku odpowiedzialnego za dane wysy�ane
	 */
	FileDownloader(String inputFileName, String outputFileName) {
		
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
	}

}
