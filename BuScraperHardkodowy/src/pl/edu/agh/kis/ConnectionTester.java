package pl.edu.agh.kis;

import java.io.IOException;
import java.net.Socket;

/**
 * Klasa, kt�rej przeznaczeniem jest pr�ba po��czenia si� z danym hostem, sprawdzaj�c
 * czy mo�liwe jest nawi�zanie po��czenia. W przypadku wyst�pienia wyj�tku lub przekroczenia
 * czasu oczekiwania, funkcja testHost zwr�ci warto�� fa�szu informuj�c o niemo�no�ci
 * przeprowadzenia po��czenia.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class ConnectionTester {

	/**
	 * Zadaniem funkcji jest poinformowanie o mo�liwo�ci po��czenia si� z hostem, kt�rego
	 * nazwa zosta�a podana w argumencie. Funckja tworzy nowy obiekt Socket, jako parametry
	 * przyjmuj�c podan� w argumencie nazw� hosta oraz port 80, a nast�pnie pr�buje
	 * zainicjowa� po��czenie z czasem oczekiwania r�wnym 0.1 sekundy. Je�eli po tym czasie
	 * nie uda si� nawi�za� po��czenia lub wyst�pi wyj�tek typu wej�cia/wyj�cia ca�a funkcja
	 * zwr�ci warto�� logicznego fa�szu, natomiast w przypadku niewyst�pienia podobnych
	 * b��d�w, zwr�ci warto�� logicznej prawdy.
	 * @param hostName nazwa hosta, dla kt�rego sprawdzimy mo�liwo�� zainicjowania po��czenia.
	 * @return zwr�ci fa�sz, je�eli podczas przeprowadzania po��czenia wyst�pi wyj�tek typu
	 * 		wej�cia/wyj�cia lub je�eli zostanie przekroczony czas po��czenia, w przeciwnym
	 * 		wypadku zwr�ci prawd�.
	 */
	public boolean testHost(String hostName)
	{
		boolean result = true;
		Socket socket;
		
		try {
			socket = new Socket(hostName, 80);
			socket.setSoTimeout(100);
		} catch (IOException e) {
			result = false;
		}

		return result;
	}
}
