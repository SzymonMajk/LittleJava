package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Klasa implementuj¹ca interfejst Downloader, umo¿liwiaj¹ca udostêpnianie strumieni
 * przy u¿yciu obiektów do komunikacji sieciowej oraz po³¹czenia siê z hostem podanym 
 * w konstruktorze na porcie numer 80.
 * @author Szymon Majkut
 * @version 1.4
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
	 * Pole przechowuj¹ce nazwê hosta
	 */
	private String hostName;
	
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
	public boolean initDownloader() throws UnknownHostException, IOException {
		boolean result = true;
		
		socket = new Socket(hostName, portNumber);
		socket.setSoTimeout(1000);//odpowiada za uchronienie przed blokowaniem
		output = socket.getOutputStream();
		input = socket.getInputStream();

		return result;
	}
	
	//TODO mo¿e to wywalimy co?
	/**
	 * Zadaniem funkcji jest zamkniêcie strumieni.
	 * @throws IOException wyrzucany przy problemach z zamkniêciem strumieni lub
	 *         socket'u odpowiedzialnych za komunikacjê sieciow¹
	 */
	public void closeStreams() throws IOException 
	{
		input.close();
		output.close();
		socket.close();
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
			log4j.error("Problem z po³¹czeniem z URL"+e.getMessage());
		} 
	}
}
