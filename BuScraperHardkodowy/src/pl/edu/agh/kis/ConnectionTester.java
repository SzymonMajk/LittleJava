package pl.edu.agh.kis;

import java.io.IOException;
import java.net.Socket;

/**
 * Klasa, której przeznaczeniem jest próba po³¹czenia siê z danym hostem, sprawdzaj¹c
 * czy mo¿liwe jest nawi¹zanie po³¹czenia. W przypadku wyst¹pienia wyj¹tku lub przekroczenia
 * czasu oczekiwania, funkcja testHost zwróci wartoœæ fa³szu informuj¹c o niemo¿noœci
 * przeprowadzenia po³¹czenia.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class ConnectionTester {

	/**
	 * Zadaniem funkcji jest poinformowanie o mo¿liwoœci po³¹czenia siê z hostem, którego
	 * nazwa zosta³a podana w argumencie. Funckja tworzy nowy obiekt Socket, jako parametry
	 * przyjmuj¹c podan¹ w argumencie nazwê hosta oraz port 80, a nastêpnie próbuje
	 * zainicjowaæ po³¹czenie z czasem oczekiwania równym 0.1 sekundy. Je¿eli po tym czasie
	 * nie uda siê nawi¹zaæ po³¹czenia lub wyst¹pi wyj¹tek typu wejœcia/wyjœcia ca³a funkcja
	 * zwróci wartoœæ logicznego fa³szu, natomiast w przypadku niewyst¹pienia podobnych
	 * b³êdów, zwróci wartoœæ logicznej prawdy.
	 * @param hostName nazwa hosta, dla którego sprawdzimy mo¿liwoœæ zainicjowania po³¹czenia.
	 * @return zwróci fa³sz, je¿eli podczas przeprowadzania po³¹czenia wyst¹pi wyj¹tek typu
	 * 		wejœcia/wyjœcia lub je¿eli zostanie przekroczony czas po³¹czenia, w przeciwnym
	 * 		wypadku zwróci prawdê.
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
