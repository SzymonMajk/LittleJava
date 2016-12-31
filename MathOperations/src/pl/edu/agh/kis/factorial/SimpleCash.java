package pl.edu.agh.kis.factorial;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.TreeSet;

public class SimpleCash implements Cashable {

	/**
	 * Przechowuje liczby i wyniki ich obliczeń
	 */
	private HashMap<Integer, BigInteger> calculatedNumbersWithResults =
			new HashMap<Integer, BigInteger>();
	
	/**
	 * Przechowuje liczby, dla których jesteśmy w stanie znaleźć wynik
	 */
	private TreeSet<Integer> calculatedNumbers = new TreeSet<Integer>();
	
	/**
	 * Dodaje jednocześnie do mapy i do zbioru, aby można było
	 * łatwo sprawdzić czy obiekt jest w mapie, a również łatwo pobrać
	 * jeśli znajduje się takowy w zbiorze
	 */
	public void add(int i, BigInteger b) {
		
		/**
		 * Mapa nadpisuje wartości istniejące
		 */
		calculatedNumbersWithResults.put(i,b);
		/**
		 * Zbiór nadpisuje wartości istniejące
		 */
		calculatedNumbers.add(i);
	}

	/**
	 * Najprostrze pobranie wartości z mapy
	 * @param i liczba dla której mamy zwrócić wartość
	 * @return wartość dla danej liczby lub null jeżeli takowa nie istnieje
	 */
	public BigInteger get(int i) {
		
		//Zakładamy, że użytkownik już sprawdził czy istnieje
		return calculatedNumbersWithResults.get(i);
	}

	/**
	 * Funkcja sprawdza czy obiekt istnieje w cashu
	 * @param i liczba która istnienie chcemy sprawdzić
	 */
	public boolean exist(int i) {
		/*
		 * Sprawdza obecnośc elementu w zbiorze jeżeli element
		 * jest w zbiorze, to musi znajdować się również w mapie
		 */
		return calculatedNumbers.contains(i);
	}
}
