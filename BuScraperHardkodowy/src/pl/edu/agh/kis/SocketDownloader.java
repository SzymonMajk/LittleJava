package pl.edu.agh.kis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Klasa implementuj�ca interfejst Downloader, umo�liwiaj�ca udost�pnianie strumieni
 * przy u�yciu obiektu Socket, ��cz�c si� z hostem podawanym jako argument metody
 * initDownloader, na porcie o numerze 80. Przy pomocy metody initDonwloader
 * jest w stanie zainicjowa� strumienie do komunikacji, informuj�c zwracan� warto�ci�
 * o powodzeniu zainicjowania strumieni. Wa�nym jest, aby nie udost�pnia� niezainicjowanych
 * strumieni, oraz odbiorcy obiektu musz� by� przygotowani na mo�liwo�� zerwania po��czenia
 * oraz brak mo�liwo�ci zainicjowania po��czenia, kt�re jest przejawiane warto�ci� zwracan�
 * funkcji initDownloader. B��dy oraz wa�niejsze kroki programu s� umieszczane w logach.
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
	 * Inicjalizacja strumienia nast�puje poprzez wykonanie metody initDownloader, dlatego
	 * u�ytkownik jest zobowi�zany uruchomi� metod� initDownloader przed wywo�aniem metody
	 * getOutputStream.
	 * @return strumie� wyj�ciowy przeznaczony do wysy�ania zapyta� do servera, z kt�rym
	 * 		mo�emy po��czy� si� poprzez adres hosta oraz port podany w konstruktorze.
	 */
	@Override
	public OutputStream getOutputStream() {
		return output;
	}

	/**
	 * Funkcja ma za zadanie zwraca� strumie� wej�ciowy przechowywany w polu prywatnym.
	 * Inicjalizacja strumienia nast�puje poprzez wykonanie metody initDownloader, dlatego
	 * u�ytkownik jest zobowi�zany uruchomi� metod� initDownloader przed wywo�aniem metody
	 * getInputStream.
	 * @return strumie� wej�ciowy przeznaczony do odbierania odpowiedzi od servera, z kt�rym
	 * 		mo�emy po��czy� si� poprzez adres hosta oraz port podany w konstruktorze.
	 */
	@Override
	public InputStream getInputSteam() {
		return input;
	}
	
	/**
	 * Zadaniem funkcji jest otwarcie po��czenia przy u�yciu nowoutworzonego obiektu
	 * Socket, na rzecz hosta, kt�rego adres podawany jest w argumencie funkcji, poprzez
	 * port o numerze 80, oraz pod��czenie strumieni tego Socketu do odpowiednich p�l 
	 * prywatnych  przechowuj�cych strumie� wej�ciowy oraz strumie� wyj�ciowy. Klasa 
	 * informuje warto�ci� zwracan� o powodzeniu swojego dzia�ania, ustalaj�c maksymalny 
	 * dozwolony czas oczekiwania na niedosz�e po��czenie, zabezpiecza si� przed 
	 * zablokowaniem na strumieniu, kt�ry nigdy niczego nie zwr�ci, ze wzgl�du na 
	 * niedost�pno�� hosta.
	 * @param hostName nazwa hosta, na rzecz kt�rego zamierzamy otworzy� po��czenie
	 * 		na porcie numer 80 przy u�yciu nowego obiektu Socket.
	 * @return zwraca warto�� fa�szu, je�eli podczas pr�by otwarcia po��czenia nast�pi
	 * 		wyj�tek typu wej�cia/wyj�cia, wynikaj�cy na przyk�ad z faktu niemo�liwo�ci
	 * 		nawi�zania po��czenia z hostem.
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
			log4j.warn("Wyj�tek przy inicjowaniu po��czenia"+e.getMessage());
		}

		return result;
	}
}
