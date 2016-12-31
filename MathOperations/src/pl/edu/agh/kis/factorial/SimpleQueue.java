package pl.edu.agh.kis.factorial;

import java.util.PriorityQueue;
/**
 * Klasa implementująca interfejs kolejki zleceń, robi to w najprostrzy sposób
 * w kolejce priorytetowej, gdzie priorytetem jest podana przez nas liczba,
 * więc będą one wykonywane rosnąco
 * @author Szymon Majkut
 *
 */
public class SimpleQueue implements Queueable {

	/**
	 * Priorytetem będzie wartość wkładanego elementu,
	 * ponieważ implementacja dedykowana jest dla kalkulatora
	 * silni wykorzystującego wcześniej obliczone wartości
	 */
	private PriorityQueue<Integer> numbersToCalculate = new PriorityQueue<Integer>();
	
	/**
	 * Dodaje liczbę do kolejki, priorytetem jest wartość liczby
	 * @param i liczba którą należy dodać do kolejki
	 */
	public void add(int i) {
		
		// Zabezpiecznie przed powtarzaniem się elementów w kolejce
		if(!exist(i))
		{
			numbersToCalculate.add(i);
		}
	}
	
	/**
	 * Pobieranie zlecenia z kolejki
	 * należy własnoręcznie sprawdzić czy element istnieje
	 * @return pierwsza liczba w kolejce
	 */

	public int poll() {
	
		return numbersToCalculate.poll();
	}

	/**
	 * Sprawdza czy dana liczba już istnieje w kolejce
	 * @param i liczba do sprawdzenia
	 * @return informacja o tym czy podana liczba istnieje w kolejce
	 */
	public boolean exist(int i) {
		return numbersToCalculate.contains(i);
	}
	
	/**
	 * Sprawdza czy kolejka jest pusta
	 * @return informacja o tym czy kolejka jest pusta
	 */
	public boolean isEmpty() {
		return numbersToCalculate.isEmpty();
	}
}
