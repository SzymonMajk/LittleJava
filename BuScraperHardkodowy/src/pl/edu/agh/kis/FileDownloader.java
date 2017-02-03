package pl.edu.agh.kis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa implementuj¹ca interfejst Downloader, umo¿liwiaj¹ca udostêpnianie strumieni
 * dzia³aj¹cych na plikach, przeznaczona g³ównie do testów
 * @author Szymon Majkut
 * @version 1.3
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
	 * Funkcja ma za zadanie zwracaæ strumieñ wyjœciowy przechowywany w polu prywatnym
	 * @return strumieñ wyjœciowy przechowywany w polu prywatnym
	 */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wejœciowy przechowywany w polu prywatnym
	 * @return strumieñ wyjœciowy przechowywany w polu prywatnym
	 */
	@Override
	public InputStream getInputSteam() {
		return input;
	}
	
	/**
	 * Zadaniem funkcji jest otworzenie strumieni dla plików podanych w konstruktorze
	 * @return informacja o powodzeniu utworzenia strumieni z plików
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
	 * Zadaniem funkcji jest zamkniêcie strumieni oraz poinformowanie o powodzeniu
	 * @return informacja o powodzeniu zamkniêcia strumieni
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
	 * Konstruktor sparametryzowany, którego zadaniem jest poprawne przypisanie nazwy Hosta
	 * @param pageURL adres URL strony, z której wyodrêbnimy nazwê hosta
	 */
	FileDownloader(String inputFileName, String outputFileName) {
		
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
	}

}
