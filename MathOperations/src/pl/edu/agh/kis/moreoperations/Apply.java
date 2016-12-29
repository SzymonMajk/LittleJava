package pl.edu.agh.kis.moreoperations;

/**: Object/pl/edu/agh/kis/smmoreoperations/Apply */
import java.math.BigInteger;

/**
 * Klasa odpowiedzialna za wywołanie konkretnego procesu otrzymanego
 * jako klasę implementującą interfejs Operations
 * @author Szymon Majkut
 * @version 1.1
 */
class Apply
{
	/**
	 * Funkcja otrzymuje jako parametry dane potrzebne do zaprzęgnięcia
	 * odpowiedniej klasy i wykonania w niej operacji
	 * @param o Referencja na klasę implementującą interfejs Operation
	 * której implementacja ma zostać wykonana
	 * @param first pierwsza duża liczba do operacji
	 * @param second druga duża liczba do operacji
	 */
	static void procces(Operation o, BigInteger first, BigInteger second)
	{
		System.out.println("Wykonuje " + o.name());
		System.out.println(o.operation(first,second));
	}
}/* Output brak
*///:~