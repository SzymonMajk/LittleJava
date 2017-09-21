package pl.edu.agh.kis.smfactorial;

import java.math.BigInteger;

/**
 * Jego zadaniem jest zaimplementowanie klasy przechowującej
 * wyliczone wartości zadanej operacji matematycznej, sprawdzanie
 * ich istnienia oraz umieszczanie i pobieranie
 * @author Szymon Majkut
 *
 */
public interface Cashable {

	/**
	 * Zadaniem funkcji jest zaimplementowanie metody dodawania
	 * dla zadanego elementu jego wartości do cashu
	 * @param i element dla którego wartość ma zostać dodana do Cashu
	 * @param b wartość która ma zostać dodana do Cashu
	 */
	void add(int i, BigInteger b);
	
	/**
	 * Zadaniem funkcji jest zaimplementowanie metody zwracania
	 * elementu dla zadanego parametru
	 * @return zwraca wartość dla zadanego parametru
	 */
	BigInteger get(int i);
	
	/**
	 * Zadaniem funkcji jest sprawdzenie czy dla zadanego parametru
	 * istnieje w cashu obliczon wartość
	 * @param i wartość której istnienie chcemy sprawdzić
	 * @return prawda jeśli wartość elementu istnieje, fałsz jeżeli nie istnieje
	 */
	boolean exist(int i);
	
}
