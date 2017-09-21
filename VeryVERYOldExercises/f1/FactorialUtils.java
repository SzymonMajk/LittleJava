package pl.edu.agh.kis.f1;

import java.math.BigInteger;

public class FactorialUtils {

	/**
	 * Oblicza silnie z podanej liczby i zwraca rezultat jako obiekt BigInteger
	 * 
	 * @param n
	 *            liczba, z ktorej ma zostac obliczona silnia
	 * @return wartosc silni
	 */
	public static BigInteger calcFactorial(long n) {
		BigInteger result = BigInteger.ONE;
		for (long i = 2; i <= n; ++i) {
			result = result.multiply(BigInteger.valueOf(i));
		}
		return result;
	}
}
