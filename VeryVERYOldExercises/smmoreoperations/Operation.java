package pl.edu.agh.kis.smmoreoperations;

/**: Object/pl/edu/agh/kis/smmoreoperations/Operation */
import java.math.BigInteger;

/**
 * Intefrejs operacji na dużych liczbach wymagający zaimplementowania 
 * nazwy operacji i samej operacji na dużych liczbach
 * @author Szymon Majkut
 * @version 1.1
 */
interface Operation {
	
	/**
	 * Prototyp funkcji zwracającej nazwe operacji matematycznej
	 * @return string z nazwą operacji matematycznej
	 */
	String name();
	
	/**
	 * Prototyp funkcji wykonania operacji matematycznej
	 * @param first, second - dwie duże liczby na których ma zostać
	 * przeprowadzona operacja
	 * @return wynik operacji w postaci dużej liczby
	 */
	BigInteger operation(BigInteger first, BigInteger second);

}/* Output brak
*///:~