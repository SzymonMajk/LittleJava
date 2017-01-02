package pl.edu.agh.kis.operators;

/**
 * Prezentacja binarnych postaci zmiennych znakowych, zauważmy, że zwiększanie
 * wartości binarnej liczby o jakąś wartość, powoduje przesunięcie znaku w kodzie
 * ASCII o tę samą wartość
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex13 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{	
		char znak = 'c';
		System.out.println("c: " + Integer.toBinaryString((int)znak));
		
		znak = 'd';
		System.out.println("d: " + Integer.toBinaryString((int)znak));
		
		znak = 'e';
		System.out.println("e: " + Integer.toBinaryString((int)znak));
		
		znak = 'q';
		System.out.println("q: " + Integer.toBinaryString((int)znak));
		
		znak = 'w';
		System.out.println("w: " + Integer.toBinaryString((int)znak));
		
		znak = 'e';
		System.out.println("e: " + Integer.toBinaryString((int)znak));

	}
}
/* Output 15% match
c: 1100011
d: 1100100
e: 1100101
q: 1110001
w: 1110111
e: 1100101
*///:~