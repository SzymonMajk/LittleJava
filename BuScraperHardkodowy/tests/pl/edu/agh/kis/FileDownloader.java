package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa implementuj¹ca interfejs Downloader, umo¿liwiaj¹ca inicjowanie, oraz
 * udostêpnianie strumieni do plików, jest przeznaczona g³ównie do testów.
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
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wejœcia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wyjœcia do hosta
	 */
	private OutputStream output;
	
	/**
	 * Pole przechowuj¹ce nazwê pliku dla strumienia wejœciowego
	 */
	private String inputFileName;
	
	/**
	 * Pole przechowuj¹ce nazwê pliku dla strumienia wyjœciowego
	 */
	private String outputFileName;
	
	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wyjœciowy przechowywany w polu prywatnym.
	 * @return strumieñ wyjœciowy przechowywany w polu prywatnym
	 */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wejœciowy przechowywany w polu prywatnym.
	 * @return strumieñ wyjœciowy przechowywany w polu prywatnym
	 */
	@Override
	public InputStream getInputSteam() {
		return input;
	}
	
	/**
	 * Zadaniem funkcji jest otworzenie strumieni dla plików podanych w konstruktorze.
	 * @param hostName w tej implementacji nie jest u¿ywany
	 * @return informacja o powodzeniu utworzenia strumieni do plików
	 */
	@Override
	public boolean initDownloader(String hostName) {
		boolean result = true;
		
		try {
			input = new FileInputStream(inputFileName);
			output= new FileOutputStream(outputFileName);
		} catch (FileNotFoundException e) {
			log4j.error("Nast¹pi³ problem podczas ustalania stanu pocz¹tkowego strumieni:"+
					e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest poprawne przypisanie nazw plików
	 * odpowiedzialnych za dane przychodz¹ce oraz dane wysy³ane ( dane przychodz¹ce s¹
	 * czytane z pliku inputFileName, natomiast wysy³ane s¹ do pliku outputFileName ).
	 * @param inputFileName nazwa pliku odpowiedzialnego za dane przychodz¹ce
	 * @param outputFileName nazwa pliku odpowiedzialnego za dane wysy³ane
	 */
	FileDownloader(String inputFileName, String outputFileName) {
		
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
	}

}
