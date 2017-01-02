package pl.edu.agh.kis.operators;

/**
 * Przykład na obliczenie prędkości średniej, znając całkowitą drogę
 * oraz całkowity czas jej pokonania
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex4 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		double distance = 500.25;
		double time = 251.23;
		
		System.out.println("Policzmy predkość dla drogi: " +
		distance + "[m] pokonanej w czasie: " + time + "[s]");
		
		System.out.println("Wynik to: " + distance/time + "[m/s]");
	}
}
/* Output 15% match
Policzmy predkość dla drogi: 500.25[m] pokonanej w czasie: 251.23[s]
Wynik to: 1.9912032798630737[m/s]
*///:~