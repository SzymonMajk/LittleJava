package pl.edu.agh.kis.smfactorial;

/**
 * Jego zadaniem jest zaimplementowanie klasy, która będzie przechowywać,
 * umożliwiać dodanie, pobranie oraz sprawdzenie istnienia elementów
 * w priorytetowej kolejce zleceń
 * @author Szymon Majkut
 *
 */
public interface Queueable {

	/**
	 * Zadaniem funkcji implementowanej jest dodanie elementu o zadanej
	 * wartości do kolejki priorytetowej
	 * @param i wartość elementu który chcemy dodać
	 */
	void add(int i);
	
	/**
	 * Zadaniem funkcji implementowanejjest pobranie oraz usunięcie elementu
	 *  o najwyższym priorytecie
	 * @return wartość elementu o najwyższym priorytecie
	 */
	int poll();
	
	/**
	 * Implementuje funkcję, której zadaniem jest sprawdzenie czy zadana
	 * wartość występuje w kolejce
	 * @param i element której istnienie chcemy sprawdzić
	 * @return prawda jeśli element istnieje, fałsz jeżeli nie istnieje
	 */
	boolean exist(int i);
	
	/**
	 * Implementuje funkcję, której zadaniem jest sprawdzenie czy kolejka
	 * jest pusta
	 * @return true jeśli kolejka jest pusta, false jeśli nie
	 */
	boolean isEmpty();
}
