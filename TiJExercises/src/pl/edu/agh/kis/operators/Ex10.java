package pl.edu.agh.kis.operators;

/**
 * Zabawy z ciekawymi liczbami, oglądane w ich zapisach binarnych
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex10 {

	/**
	 * Same zera i jedynki, na przemian tutaj od jedynki
	 */
	static int FIRST = 0xaaaaaaaa;
	/**
	 * Same zera i jedynki, na przemian tutaj od zera
	 */
	static int SECOND = 0x55555555;
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		System.out.println("F = " + Integer.toBinaryString(FIRST));
		System.out.println("S = " + Integer.toBinaryString(SECOND));
		System.out.println("~F = " + Integer.toBinaryString(~FIRST));
		System.out.println("~S = " + Integer.toBinaryString(~SECOND));
		
		System.out.println("F & S = " + Integer.toBinaryString(FIRST&SECOND));
		System.out.println("F | S = " + Integer.toBinaryString(FIRST|SECOND));
		System.out.println("F ^ S = " + Integer.toBinaryString(FIRST^SECOND));
	}
}
/* Output 15% match
F = 10101010101010101010101010101010
S = 1010101010101010101010101010101
~F = 1010101010101010101010101010101
~S = 10101010101010101010101010101010
F & S = 0
F | S = 11111111111111111111111111111111
F ^ S = 11111111111111111111111111111111
*///:~