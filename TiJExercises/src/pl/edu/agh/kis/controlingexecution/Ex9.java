package pl.edu.agh.kis.controlingexecution;

/**
 * Klasa wykonująca ciąg fibonacciego dla zadanej liczby, przy czym jej wyniki
 * muszą mieścić się w zakresie prymitywa int
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex9 {

	/** Punkt wyjścia do klasy i aplikacji
	*  @param args parametry programu, w tym przypadku liczba elementów ciągu
	*/	
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.out.println("Podaj liczbę jako argument programu!");
			return;
		}
		int f = Integer.parseInt(args[0]);
		
		if ( f >= 2)
		{
			System.out.print("1 1");
		}
		
		if (f > 2)
		{
			int prev = 1;
			int prevPrev = 1;
			int result = 2;
			
			for(int i = 3; i <= f; ++i)
			{
				result = prev + prevPrev;
				System.out.print(" " + result);
				prevPrev = prev;
				prev = result;
			}
			
		}
		
	}
}
/* Output 10% match
Podaj liczbę jako argument programu!
*///:~