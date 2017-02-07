package pl.edu.agh.kis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa implementuj�ca interfejs Downloader, umo�liwiaj�ca udost�pnianie strumieni
 * do plik�w, jest przeznaczona g��wnie do test�w.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public class FileDownloader implements Downloader {

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
	 * @return informacja o powodzeniu utworzenia strumieni do plik�w
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
	 * Zadaniem funkcji jest zamkni�cie strumieni przechowywanych w polach prywatnych.
	 * @return informacja o powodzeniu zamkni�cia strumieni
	 * @throws IOException wyrzucany przy problemach z zamkni�ciem strumieni lub dost�pu
	 *         do plik�w.
	 */
	public void closeStreams() throws IOException
	{
		input.close();
		output.close();
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
