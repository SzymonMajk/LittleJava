package pl.edu.agh.kis.crawler;

/**
 * Interfejs bufora potrzebnego dla klasy, która ma w sobie zawrzeć
 * metodę składowania i analizowania danych typu producent - konsument
 * @author Szymon Majkut
 *
 */
public interface Buffer {

	/**
	 * Informuje o wypełnieniu kontenera
	 * @return Zwraca prawdę gdy kontener jest zapełniony
	 */
	boolean isFull();

	/**
	 * Ma za zadanie umieścić element w konterze zakładająć, że ktoś sprawdził
	 * już czy nie jest pełny
	 * @param s
	 */
	void put(String s);
	
	/**
	 * Sprawdza czy konetener jest pusty
	 * @return Zwraca prawdę gdy kontener jest pusty
	 */
	boolean isEmpty();
	
	/**
	 * Pobiera element z kontenera zakładając, że ktoś sprawdził już czy
	 * nie jest pusty
	 * @return
	 */
	String take();
	
	/**
	 * Zwraca ilość elementów zawartych w buforze
	 * @return ilość elementów zawartych w buforze
	 */
	int size();
}
