package pl.edu.agh.kis;

import java.net.Socket;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Klasa implementuj¹ca interfejst Downloader, umo¿liwiaj¹ca udostêpnianie strumieni
 * przy u¿yciu socketów i po³¹czenia siê z hostem podanym w konstruktorze na porcie 80
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class SocketDownloader implements Downloader {

	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wejœcia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wyjœcia do hosta
	 */
	private OutputStream output;

	/**
	 * Pole przechowuj¹ce nazwê hosta
	 */
	private String hostName;
	
	/**
	 * Pole przechowuj¹ce numer portu
	 */
	private int portNumber = 80;
	
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
	 * Zadaniem funkcji jest otwarcie po³¹czenia HTTP na nowym Sockecie z hostem przetrzymywanym
	 * w polu prywatnym oraz pod³¹czenie strumieni tego Socketu do odpowiednich pól prywatnych
	 * strumieni klasy, dodatkowo klasa informuje wartoœci¹ zwracan¹ o powodzeniu swojego dzia³ania
	 * @return informacja o powodzeniu utworzenia nowego po³¹czenia z przechowywanym hostem
	 */
	@Override
	public boolean createStreams() {
		boolean result = true;
		Socket socket;
		try {
			socket = new Socket(hostName, portNumber);
			output = socket.getOutputStream();
			input = socket.getInputStream();
		} catch (IOException e) {
			result = false;
		}
		return result;
	}
	
	/**
	 * Konstruktor sparametryzowany, którego zadaniem jest poprawne przypisanie nazwy Hosta
	 * @param pageURL adres URL strony, z której wyodrêbnimy nazwê hosta
	 */
	SocketDownloader(String pageURL) {
		
		URL url;
		try {
			url = new URL(pageURL);
			hostName = url.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
	}
}
