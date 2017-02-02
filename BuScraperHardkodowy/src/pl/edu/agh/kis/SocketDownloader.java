package pl.edu.agh.kis;

import java.net.Socket;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Klasa implementuj�ca interfejst Downloader, umo�liwiaj�ca udost�pnianie strumieni
 * przy u�yciu socket�w i po��czenia si� z hostem podanym w konstruktorze na porcie 80
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public class SocketDownloader implements Downloader {

	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wej�cia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wyj�cia do hosta
	 */
	private OutputStream output;

	/**
	 * Pole przechowuj�ce nazw� hosta
	 */
	private String hostName;
	
	/**
	 * Pole przechowuj�ce numer portu
	 */
	private int portNumber = 80;
	
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
	 * Zadaniem funkcji jest otwarcie po��czenia HTTP na nowym Sockecie z hostem przetrzymywanym
	 * w polu prywatnym oraz pod��czenie strumieni tego Socketu do odpowiednich p�l prywatnych
	 * strumieni klasy, dodatkowo klasa informuje warto�ci� zwracan� o powodzeniu swojego dzia�ania
	 * @return informacja o powodzeniu utworzenia nowego po��czenia z przechowywanym hostem
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
	 * Konstruktor sparametryzowany, kt�rego zadaniem jest poprawne przypisanie nazwy Hosta
	 * @param pageURL adres URL strony, z kt�rej wyodr�bnimy nazw� hosta
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
