package pl.edu.agh.kis.controlingexecution;

/**
 * Własna implementacja funkcji toBinaryString, wraz z zastosowaniem
 * analogicznym do ćwieczenia 10 z operators
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex5 {

	/**
	 * Same zera i jedynki, na przemian tutaj od jedynki
	 */
	static int FIRST = 0xaaaaaaaa;
	/**
	 * Same zera i jedynki, na przemian tutaj od zera
	 */
	static int SECOND = 0x55555555;
		
	/** Przykład funkcji wypisującej binarnie liczbę
	*  @param liczba zapisana hexagonalnie, którą mamy wypisać binarnie
	*/	
	static void myToBinaryString(int n)
	{
		char[] tab = new char[32];
		boolean negativSafety = true;
			
		for(int i = 32; i > 0; --i)
		{
			tab[32 - i] = (n & (1 << i)) == 0 ? '0' : '1';
		}
			
		for(int i = 0; i < 32; ++i)
		{

			if(negativSafety && tab[i] == '1')
			{
				negativSafety = false;
			}
			System.out.print(tab[i]);
		}
		if(negativSafety)
		{
			System.out.print('0');
		}
		System.out.println();
	}
		
	/** Punkt wyjścia do klasy i aplikacji
	*  @param args tablica ciągów argumentów wywołania
	*/	
	public static void main(String[] args)
	{
		System.out.print("F = "); 
		myToBinaryString(FIRST);
		System.out.print("S = "); 
		myToBinaryString(SECOND);
		System.out.print("~F = "); 
		myToBinaryString(~FIRST);
		System.out.print("~S = "); 
		myToBinaryString(~SECOND);
			
		System.out.print("F & S = "); 
		myToBinaryString(FIRST&SECOND);
		System.out.print("F | S = "); 
		myToBinaryString(FIRST|SECOND);
		System.out.print("F ^ S = "); 
		myToBinaryString(FIRST^SECOND);
	}
}
/* Output 100% match
F = 01010101010101010101010101010101
S = 10101010101010101010101010101010
~F = 10101010101010101010101010101010
~S = 01010101010101010101010101010101
F & S = 000000000000000000000000000000000
F | S = 11111111111111111111111111111111
F ^ S = 11111111111111111111111111111111
*///:~