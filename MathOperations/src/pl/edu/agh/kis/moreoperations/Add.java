package pl.edu.agh.kis.moreoperations;

/**: Object/pl/edu/agh/kis/smmoreoperations/Add */
import java.math.BigInteger;

/**
 * Klasa dziedziczy po podstawowej klasie implementującej
 * interfejs Operation, podając swoją własną implementację
 * @author Szymon Majkut
 * @version 1.2
 */
public class Add extends MathOperations
{
	/**
	 * Funkcja zwraca nazwę operacji
	 * @return String z nazwą operacji
	 */
	public String name()
	{
		return "Dodaję!";
	}
	/**
	 * Funkcja odpowiedzialna za przeprowadzenie samej operacji
	 * @param first pierwsza duża liczba
	 * @param second druga duża liczba
	 * @return zwraca sumę parametrów
	 */
	public BigInteger operation(BigInteger first, BigInteger second)
	{
		return first.add(second);
	}

}/* Output brak
*///:~