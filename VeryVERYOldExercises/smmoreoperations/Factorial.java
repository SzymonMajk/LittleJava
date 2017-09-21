package pl.edu.agh.kis.smmoreoperations;

/**: Object/pl/edu/agh/kis/smmoreoperations/Factorial */
import java.math.BigInteger;
import pl.edu.agh.kis.smfactorial.*;

/**
 * Klasa dziedziczy po podstawowej klasie implementującej
 * interfejs Operation, podając swoją własną implementację
 * @author Szymon Majkut
 * @version 1.2
 */
public class Factorial extends MathOperations
{
	/**
	 * Funkcja zwraca nazwę operacji
	 * @return String z nazwą operacji
	 */
	public String name()
	{
		return "Liczę silnię!";
	}

	/**
	 * Funkcja odpowiedzialna za przeprowadzenie samej operacji,
	 * ta wersja wymagała jeszcze poprawki związanej ze sposobem
	 * grupowania danych zwracanych przez obiegkt calc
	 * @param first pierwsza duża liczba
	 * @param second druga duża liczba
	 * @return zwraca wynik silni dla pierwszej dużej liczby
	 */
	public BigInteger operation(BigInteger first, BigInteger second)
	{
		int parsedNumber = second.intValue();
		
		FactorialCalculator calc = new FactorialCalculator();
		calc.addNumber(parsedNumber);
		String result = calc.calculateAll();

		result = result.substring(0,result.length()-1); // musimy pozbyć się śmieci
		return new BigInteger(result);
	}

}/* Output brak
*///:~