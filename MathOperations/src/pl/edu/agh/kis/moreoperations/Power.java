package pl.edu.agh.kis.moreoperations;

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
	public String getName()
	{
		return "Podnoszę do potęgi!";
	}
	
	/**
	 * Funkcja odpowiedzialna za przeprowadzenie samej operacji,
	 * ta wersja wymagała zrzutowania drugiej liczb na obiekt int,
	 * dlatego przeprowadza wstępne sprawdzenie danych i zwraca magic number
	 * oraz informację o niepoprawnych danych
	 * @param first pierwsza duża liczba - podstawa potęgowania
	 * @param second druga duża liczba - wykładnik potęgowania
	 * @return zwraca pierwszą podniesioną do potęgi drugiej
	 */
	public BigInteger operation(BigInteger first, BigInteger second)
	{
		if(second.compareTo(new BigInteger(""+Integer.MAX_VALUE)) > 0 )
		{
			System.out.println("Zbyt duży wykładnik!");
			return new BigInteger(""+0);
		}
		else if(second.intValue() < 0)
		{
			System.out.println("Podany wykładnik mniejszy od zera!");
			return new BigInteger(""+0);
		}
		
		int parsedNumber = second.intValue();
	
		return first.pow(parsedNumber);
	}
}/* Output brak
*///:~
