package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Klasa implementuj�ca interfejst Downloader, umo�liwiaj�ca udost�pnianie strumieni
 * przy u�yciu obiekt�w do komunikacji sieciowej oraz po��czenia si� z hostem podanym 
 * w konstruktorze na porcie numer 80.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class SocketDownloader implements Downloader {

	/**
	 * System Log4J
	 */
	private static final Logger log4j = LogManager.getLogger(SocketDownloader.class.getName());
	
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wej�cia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj�ce na czas pobierania zawarto�ci strumie� wyj�cia do hosta
	 */
	private OutputStream output;

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
	public boolean initDownloader(String hostName) {
		boolean result = true;
		
		try {
			socket = new Socket(hostName, portNumber);
			socket.setSoTimeout(2000);//odpowiada za uchronienie przed blokowaniem
			output = socket.getOutputStream();
			input = socket.getInputStream();
		} catch (IOException e) {
			result = false;
			log4j.warn("Wyj�tek przy inicjowaniu po��czenia"+e.getMessage());
		}

		return result;
	}
}
