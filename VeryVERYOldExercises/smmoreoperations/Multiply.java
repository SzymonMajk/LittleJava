package pl.edu.agh.kis.smmoreoperations;


/**: Object/pl/edu/agh/kis/smmoreoperations/Multiply */
import java.math.BigInteger;

/**
 * Klasa dziedziczy po podstawowej klasie implementującej
 * interfejs Operation, podając swoją własną implementację
 * @author Szymon Majkut
 * @version 1.2
 */
public class Multiply extends MathOperations
{
	/**
	 * Funkcja zwraca nazwę operacji
	 * @return String z nazwą operacji
	 */
	public String name()
	{
		return "Mnożę!";
	}
	/**
	 * Funkcja odpowiedzialna za przeprowadzenie samej operacji
	 * @param first pierwsza duża liczba
	 * @param second druga duża liczba
	 * @return zwraca iloczyn parametrów
	 */
	public BigInteger operation(BigInteger first, BigInteger second)
	{
		return first.multiply(second);
	}

}/* Output brak
*///:~