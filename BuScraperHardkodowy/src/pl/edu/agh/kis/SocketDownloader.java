package pl.edu.agh.kis;

import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Klasa implementuj�ca interfejst Downloader, umo�liwiaj�ca udost�pnianie strumieni
 * przy u�yciu obiekt�w do komunikacji sieciowej oraz po��czenia si� z hostem podanym 
 * w konstruktorze na porcie numer 80.
 * @author Szymon Majkut
 * @version 1.4
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
	 * Referencja do socket'u
	 */
	private Socket socket;
	
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
	 * Zadaniem funkcji jest otwarcie po��czenia HTTP na nowym Sockecie z hostem
	 * przetrzymywanym w polu prywatnym oraz pod��czenie strumieni tego Socketu do
	 * odpowiednich p�l prywatnych strumieni klasy, dodatkowo klasa informuje warto�ci� 
	 * zwracan� o powodzeniu swojego dzia�ania, ustalaj�c maksymalny dozwolony czas
	 * po��czenia, zabezpiecza si� przed zablokowaniem na metodzie read() strumienia.
	 * @return informacja o powodzeniu utworzenia nowego po��czenia z przechowywanym hostem
	 * @throws UnknownHostException wyrzucany gdy wyst�puje problem z utworzeniem po��czenia
	 *         sieciowego np. z powodu braku internetu
	 * @throws IOException wyrzucany gdy wyst�puje problem ze zwr�ceniem strumieni
	 *         i przypisaniem ich do w�a�ciwych p�l prywatnych
	 */
	@Override
	public boolean initStreams() throws UnknownHostException, IOException {
		boolean result = true;
		
		socket = new Socket(hostName, portNumber);
		socket.setSoTimeout(100);//odpowiada za uchronienie przed blokowaniem
		output = socket.getOutputStream();
		input = socket.getInputStream();

		return result;
	}
	
	/**
	 * Zadaniem funkcji jest zamkni�cie strumieni.
	 * @throws IOException wyrzucany przy problemach z zamkni�ciem strumieni lub
	 *         socket'u odpowiedzialnych za komunikacj� sieciow�
	 */
	public void closeStreams() throws IOException 
	{
		input.close();
		output.close();
		socket.close();
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
