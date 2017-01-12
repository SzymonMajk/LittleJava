package pl.edu.agh.kis;

/**
 * Zadaniem interfejsu jest udost�pni� u�ytkownikowi funkcje pobrania zawarto�ci, dodania
 * zawarto�ci oraz sprawdzenia pusto�ci oraz sprawdzenia zape�nienia synchronizowanego
 * bufora, na kt�rym oprzemy metod� producent-konsument przy przetwarzaniu stron
 * @author Szymon Majkut
 * @version 1.0
 *
 */
public interface PagesBuffer {

	/**
	 * Implementacja ma informowa� o pusto�ci bufora
	 * @return informacja logiczna o pusto�ci bufora
	 */
	public boolean isEmpty();
	
	/**
	 * Implementacja ma informowa� o zape�nieniu bufora
	 * @return informacja logiczna o zape�nieniu bufora
	 */
	public boolean isFull();
	
	/**
	 * Implementacja ma za zadanie doda� stron� do bufora, u�ytkownik
	 * sam powinien zatroszczy� si� sprawdzenie zape�nienia
	 * @param pageHTML Strona w HTML, kt�r� zamierzamy doda� do bufora
	 */
	public void addPage(String pageHTML);
	
	/**
	 * Implementacja ma za zadanie zwr�ci� oraz usun�� stron� z bufora,
	 * u�ytkownik sam powinien zatroszczy� si� o sprawdzenie pusto�ci
	 * @return Strona w HTML, kt�ra zosta�a pobrana z bufora
	 */
	public String pollPage();
}
