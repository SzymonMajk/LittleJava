package pl.edu.agh.kis.logger;

/**
 * Interfejs odpowiada za abstrakcjê nad wyborem odpowiedniego sposobu
 * przesyłania logów
 * @author Szymon Majkut
 *
 */
public interface Appends {

	/**
	 * Implementacja ma za zadanie przesłać log w odpowiedni dla
	 * siebie sposób
	 * @param s ciąg znaków z konkrentego loga
	 */
	void sendNext(String s);
	
	/**
	 * Metoda oczyszczająca appendera z poprzednich logów
	 */
	void clear();
}