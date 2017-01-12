package pl.edu.agh.kis;

/**
 * Zadaniem interfejsu jest udostêpniæ u¿ytkownikowi funkcje pobrania zawartoœci, dodania
 * zawartoœci oraz sprawdzenia pustoœci oraz sprawdzenia zape³nienia synchronizowanego
 * bufora, na którym oprzemy metodê producent-konsument przy przetwarzaniu stron
 * @author Szymon Majkut
 * @version 1.0
 *
 */
public interface PagesBuffer {

	/**
	 * Implementacja ma informowaæ o pustoœci bufora
	 * @return informacja logiczna o pustoœci bufora
	 */
	public boolean isEmpty();
	
	/**
	 * Implementacja ma informowaæ o zape³nieniu bufora
	 * @return informacja logiczna o zape³nieniu bufora
	 */
	public boolean isFull();
	
	/**
	 * Implementacja ma za zadanie dodaæ stronê do bufora, u¿ytkownik
	 * sam powinien zatroszczyæ siê sprawdzenie zape³nienia
	 * @param pageHTML Strona w HTML, któr¹ zamierzamy dodaæ do bufora
	 */
	public void addPage(String pageHTML);
	
	/**
	 * Implementacja ma za zadanie zwróciæ oraz usun¹æ stronê z bufora,
	 * u¿ytkownik sam powinien zatroszczyæ siê o sprawdzenie pustoœci
	 * @return Strona w HTML, która zosta³a pobrana z bufora
	 */
	public String pollPage();
}
