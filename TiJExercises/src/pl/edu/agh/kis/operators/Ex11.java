package pl.edu.agh.kis.operators;

/**
 * Wynik przesuwania bitowego liczby w prawo, jak widać najstarszy bit jest 
 * zastępowany przez 0, a pozostałe zostają przesunięte
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex11 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{	
		int number = 0x40000000;
		
		for(int i = 0; i<32; i++)
		{
			System.out.println(Integer.toBinaryString(number));
			number = number >> 1;
		}
	}
}
/* Output 15% match
1000000000000000000000000000000
100000000000000000000000000000
10000000000000000000000000000
1000000000000000000000000000
100000000000000000000000000
10000000000000000000000000
1000000000000000000000000
100000000000000000000000
10000000000000000000000
1000000000000000000000
100000000000000000000
10000000000000000000
1000000000000000000
100000000000000000
10000000000000000
1000000000000000
100000000000000
10000000000000
1000000000000
100000000000
10000000000
1000000000
100000000
10000000
1000000
100000
10000
1000
100
10
1
0
*///:~