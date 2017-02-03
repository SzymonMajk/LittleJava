package pl.edu.agh.kis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa implementuj�ca interfejst Downloader, umo�liwiaj�ca udost�pnianie strumieni
 * dzia�aj�cych na plikach, przeznaczona g��wnie do test�w
 * @author Szymon Majkut
 * @version 1.3
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
	 * Funkcja ma za zadanie zwraca� strumie� wyj�ciowy przechowywany w polu prywatnym
	 * @return strumie� wyj�ciowy przechowywany w polu prywatnym
	 */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Funkcja ma za zadanie zwraca� strumie� wej�ciowy przechowywany w polu prywatnym
	 * @return strumie� wyj�ciowy przechowywany w polu prywatnym
	 */
	@Override
	public InputStream getInputSteam() {
		return input;
	}
	
	/**
	 * Zadaniem funkcji jest otworzenie strumieni dla plik�w podanych w konstruktorze
	 * @return informacja o powodzeniu utworzenia strumieni z plik�w
	 */
	@Override
	public boolean createStreams() {
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
	 * Zadaniem funkcji jest zamkni�cie strumieni oraz poinformowanie o powodzeniu
	 * @return informacja o powodzeniu zamkni�cia strumieni
	 */
	public boolean closeStreams()
	{
		boolean result = true;
		
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest poprawne przypisanie nazwy Hosta
	 * @param pageURL adres URL strony, z kt�rej wyodr�bnimy nazw� hosta
	 */
	FileDownloader(String inputFileName, String outputFileName) {
		
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
	}

}
