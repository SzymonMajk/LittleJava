package pl.edu.agh.kis.smfactorial;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

public class TempCash implements Cashable {

	/**
	 * Przechowuje liczby i wyniki ich obliczeń
	 */
	private HashMap<Integer, BigInteger> calculatedNumbersWithResults
		= new HashMap<Integer, BigInteger>();
	
	/**
	 * Kolejność elementów w kolejce odpowiada kolejności umieszczania ich w cashu
	 * każdorazowe użycie któregoś z elementów, powoduje przesunięcie go na początek kolejki
	 */
	public Queue<Integer> timeStatusInfo = new LinkedList<Integer>();
	
	/**
	 * Przechowuje liczby, dla których jesteśmy w stanie znalezc wynik
	 */
	private TreeSet<Integer> calculatedNumbers = new TreeSet<Integer>();
	
	/**
	 * Jeżeli w kolejce znajduje się zadana liczba, to jest przesuwana na poczatek kolejki
	 * a jeżeli jeszcze jej nie było, jest po prostu do niej dodawana
	 */
	private void changeNumberStatus(int i)
	{
		if(timeStatusInfo.contains(i))
		{
			timeStatusInfo.remove(i);
			timeStatusInfo.offer(i);
		}
		else
		{
			timeStatusInfo.offer(i);
		}
	}
	
	private boolean enoughtMemoryForNumber(BigInteger b)
	{
		return Runtime.getRuntime().freeMemory() > b.byteValue();
	}
	
	private void removeOldest()
	{
		if (timeStatusInfo.size() > 0)
		{	
			int i = timeStatusInfo.poll();
			System.out.println("Usuwam obiekt! " + i);
			calculatedNumbersWithResults.remove(i);
			timeStatusInfo.remove(i);
		}
	}
	
	/**
	 * Dodawnie elementu z uwzględnieniem niewystarczalności pamięci
	 * w związku z czym koniecznością usunięcia dawno nie używanych zmiennych
	 */
	public void add(int i, BigInteger b) {
		
		//Usuwanie nastarszych wartości jeżeli brakuje pamięci
		while(!enoughtMemoryForNumber(b) && !calculatedNumbers.isEmpty())
		{
			removeOldest();
		}
			
		/**
		 * Mapa nadpisuje wartości istniejące
		 */
		calculatedNumbersWithResults.put(i,b);
		/**
		 * Zbiór nadpisuje wartości istniejące
		 */
		calculatedNumbers.add(i);
		/**
		 * określna moment dodania elementu względem pozostałych
		 */
		changeNumberStatus(i);
	}

	/**
	 * Wyciąganie wyniku dla danej liczby, użytkownik
	 * jest zobowiązany własnoręcznie sprawdzić czy element istnieje
	 */
	public BigInteger get(int i) {
		changeNumberStatus(i);
		return calculatedNumbersWithResults.get(i);
	}

	/**
	 * Sprawdzenie obecności w zbiorze i równocześne w mapie
	 */
	public boolean exist(int i) {
		return calculatedNumbers.contains(i);
	}
}
