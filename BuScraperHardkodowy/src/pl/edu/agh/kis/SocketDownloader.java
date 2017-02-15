package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Klasa implementuj¹ca interfejst Downloader, umo¿liwiaj¹ca udostêpnianie strumieni
 * przy u¿yciu obiektów do komunikacji sieciowej oraz po³¹czenia siê z hostem podanym 
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
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wejœcia od hosta
	 */
	private InputStream input;
	
	/**
	 * Pole przechowuj¹ce na czas pobierania zawartoœci strumieñ wyjœcia do hosta
	 */
	private OutputStream output;

	/**
	 * Pole przechowuj¹ce numer portu
	 */
	private int portNumber = 80;

	/**
	 * Referencja do socket'u
	 */
	private Socket socket;
	
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
	 * Zadaniem funkcji jest otwarcie po³¹czenia HTTP na nowym Sockecie z hostem
	 * przetrzymywanym w polu prywatnym oraz pod³¹czenie strumieni tego Socketu do
	 * odpowiednich pól prywatnych strumieni klasy, dodatkowo klasa informuje wartoœci¹ 
	 * zwracan¹ o powodzeniu swojego dzia³ania, ustalaj¹c maksymalny dozwolony czas
	 * po³¹czenia, zabezpiecza siê przed zablokowaniem na metodzie read() strumienia.
	 * @return informacja o powodzeniu utworzenia nowego po³¹czenia z przechowywanym hostem
	 * @throws UnknownHostException wyrzucany gdy wystêpuje problem z utworzeniem po³¹czenia
	 *         sieciowego np. z powodu braku internetu
	 * @throws IOException wyrzucany gdy wystêpuje problem ze zwróceniem strumieni
	 *         i przypisaniem ich do w³aœciwych pól prywatnych
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
			log4j.warn("Wyj¹tek przy inicjowaniu po³¹czenia"+e.getMessage());
		}

		return result;
	}
}
