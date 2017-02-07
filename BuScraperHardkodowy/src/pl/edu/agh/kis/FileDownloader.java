package pl.edu.agh.kis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa implementuj¹ca interfejs Downloader, umo¿liwiaj¹ca udostêpnianie strumieni
 * do plików, jest przeznaczona g³ównie do testów.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class FileDownloader implements Downloader {

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
	 * @return informacja o powodzeniu utworzenia strumieni do plików
	 */
	@Override
	public boolean initStreams() {
		boolean result = true;
		
		try {
			input = new FileInputStream(inputFileName);
			output= new FileOutputStream(outputFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Zadaniem funkcji jest zamkniêcie strumieni przechowywanych w polach prywatnych.
	 * @return informacja o powodzeniu zamkniêcia strumieni
	 * @throws IOException wyrzucany przy problemach z zamkniêciem strumieni lub dostêpu
	 *         do plików.
	 */
	public void closeStreams() throws IOException
	{
		input.close();
		output.close();
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
