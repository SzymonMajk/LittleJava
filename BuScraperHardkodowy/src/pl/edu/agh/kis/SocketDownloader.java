package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Klasa implementuj¹ca interfejst Downloader, umo¿liwiaj¹ca udostêpnianie strumieni
 * przy u¿yciu obiektu Socket, ³¹cz¹c siê z hostem podawanym jako argument metody
 * initDownloader, na porcie o numerze 80. Przy pomocy metody initDonwloader
 * jest w stanie zainicjowaæ strumienie do komunikacji, informuj¹c zwracan¹ wartoœci¹
 * o powodzeniu zainicjowania strumieni. Wa¿nym jest, aby nie udostêpniaæ niezainicjowanych
 * strumieni, oraz odbiorcy obiektu musz¹ byæ przygotowani na mo¿liwoœæ zerwania po³¹czenia
 * oraz brak mo¿liwoœci zainicjowania po³¹czenia, które jest przejawiane wartoœci¹ zwracan¹
 * funkcji initDownloader. B³êdy oraz wa¿niejsze kroki programu s¹ umieszczane w logach.
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
	 * Inicjalizacja strumienia nastêpuje poprzez wykonanie metody initDownloader, dlatego
	 * u¿ytkownik jest zobowi¹zany uruchomiæ metodê initDownloader przed wywo³aniem metody
	 * getOutputStream.
	 * @return strumieñ wyjœciowy przeznaczony do wysy³ania zapytañ do servera, z którym
	 * 		mo¿emy po³¹czyæ siê poprzez adres hosta oraz port podany w konstruktorze.
	 */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wejœciowy przechowywany w polu prywatnym.
	 * Inicjalizacja strumienia nastêpuje poprzez wykonanie metody initDownloader, dlatego
	 * u¿ytkownik jest zobowi¹zany uruchomiæ metodê initDownloader przed wywo³aniem metody
	 * getInputStream.
	 * @return strumieñ wejœciowy przeznaczony do odbierania odpowiedzi od servera, z którym
	 * 		mo¿emy po³¹czyæ siê poprzez adres hosta oraz port podany w konstruktorze.
	 */
	@Override
	public InputStream getInputSteam() {
		return input;
	}
	
	/**
	 * Zadaniem funkcji jest otwarcie po³¹czenia przy u¿yciu nowoutworzonego obiektu
	 * Socket, na rzecz hosta, którego adres podawany jest w argumencie funkcji, poprzez
	 * port o numerze 80, oraz pod³¹czenie strumieni tego Socketu do odpowiednich pól 
	 * prywatnych  przechowuj¹cych strumieñ wejœciowy oraz strumieñ wyjœciowy. Klasa 
	 * informuje wartoœci¹ zwracan¹ o powodzeniu swojego dzia³ania, ustalaj¹c maksymalny 
	 * dozwolony czas oczekiwania na niedosz³e po³¹czenie, zabezpiecza siê przed 
	 * zablokowaniem na strumieniu, który nigdy niczego nie zwróci, ze wzglêdu na 
	 * niedostêpnoœæ hosta.
	 * @param hostName nazwa hosta, na rzecz którego zamierzamy otworzyæ po³¹czenie
	 * 		na porcie numer 80 przy u¿yciu nowego obiektu Socket.
	 * @return zwraca wartoœæ fa³szu, je¿eli podczas próby otwarcia po³¹czenia nast¹pi
	 * 		wyj¹tek typu wejœcia/wyjœcia, wynikaj¹cy na przyk³ad z faktu niemo¿liwoœci
	 * 		nawi¹zania po³¹czenia z hostem.
	 */
	@Override
	public boolean initDownloader(String hostName) {
		boolean result = true;
		
		try {
			socket = new Socket(hostName, portNumber);
			socket.setSoTimeout(2000);
			output = socket.getOutputStream();
			input = socket.getInputStream();
		} catch (IOException e) {
			result = false;
			log4j.warn("Wyj¹tek przy inicjowaniu po³¹czenia"+e.getMessage());
		}

		return result;
	}
}
