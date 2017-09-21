package pl.edu.agh.kis.smmoreoperations;

/**: Object/pl/edu/agh/kis/smmoreoperations/Power */
import java.math.BigInteger;

/**
 * Klasa dziedziczy po podstawowej klasie implementującej
 * interfejs Operation, podając swoją własną implementację
 * @author Szymon Majkut
 * @version 1.2
 */
public class Power extends MathOperations
{
	/**
	 * Funkcja zwraca nazwę operacji
	 * @return String z nazwą operacji
	 */
	public String name()
	{
		return "Podnoszę do potęgi!";
	}
	
	/**
	 * Funkcja odpowiedzialna za przeprowadzenie samej operacji,
	 * ta wersja wymagała zrzutowania drugiej liczb na obiekt int
	 * @param first pierwsza duża liczba - podstawa potęgowania
	 * @param second druga duża liczba - wykładnik potęgowania
	 * @return zwraca pierwszą podniesioną do potęgi drugiej
	 */
	public BigInteger operation(BigInteger first, BigInteger second)
	{
		int parsedNumber = second.intValue();
	
		return first.pow(parsedNumber);
	}
}/* Output brak
*///:~
