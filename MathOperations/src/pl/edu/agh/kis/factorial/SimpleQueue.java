package pl.edu.agh.kis.factorial;

import java.util.PriorityQueue;

public class SimpleQueue implements Queueable {

	/**
	 * Priorytetem będzie wartość wkładanego elementu,
	 * ponieważ implementacja dedykowana jest dla kalkulatora
	 * silni wykorzystującego wcześniej obliczone wartości
	 */
	PriorityQueue<Integer> numbersToCalculate = new PriorityQueue<Integer>();
	
	/**
	 * Dodaje liczbę do kolejki...
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
	 */
	public int poll() {
	
		return numbersToCalculate.poll();
	}

	/**
	 * Sprawdza czy liczba jest już w kolejce do policzenia
	 */
	public boolean exist(int i) {
		return numbersToCalculate.contains(i);
	}
	
	/**
	 * Sprawdza czy kolejka jest pusta
	 */
	public boolean isEmpty() {
		return numbersToCalculate.isEmpty();
	}

}
