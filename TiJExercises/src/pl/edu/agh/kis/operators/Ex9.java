package pl.edu.agh.kis.operators;

/**
 * Zakres prymitywów float oraz double
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex9 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		float f = Float.MIN_VALUE;
		System.out.println("Float min: " + f);
		f = Float.MAX_VALUE;
		System.out.println("Float max: " + f);
		

		double d = Double.MIN_VALUE;
		System.out.println("Double min: " + d);
		d = Double.MAX_VALUE;
		System.out.println("Double max: " + d);
	}
}
/* Output 15% match
Float min: 1.4E-45
Float max: 3.4028235E38
Double min: 4.9E-324
Double max: 1.7976931348623157E308
*///:~