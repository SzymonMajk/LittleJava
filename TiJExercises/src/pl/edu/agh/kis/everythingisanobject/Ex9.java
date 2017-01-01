package pl.edu.agh.kis.everythingisanobject;

/** 
 * Program prezentuje właność automatycznego pakowania prymitywów
 * @author Szymon
 * @version 1.7a
 */
public class Ex9 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	static public void main(String[] args)
	{		
		Boolean b = true;
		Character c = (char)'c';
		Byte by = (byte)10;
		Short s = (short)20;
		Integer i = (int)50;
		Long l = (long)50;
		Float f = (float)2000.005;
		Double d = (double)9.5325;
		
		System.out.println( "Boolean = " + b );
		System.out.println( "Character = " + c );
		System.out.println( "Byte = " + by );
		System.out.println( "Short = " + s );
		System.out.println( "Integer = " + i );
		System.out.println( "Long = " + l );
		System.out.println( "Float = " + f );
		System.out.println( "Double = " + d );
		
		
	}

}
/* Output 100% match
Boolean = true
Character = c
Byte = 10
Short = 20
Integer = 50
Long = 50
Float = 2000.005
Double = 9.5325
*///:~