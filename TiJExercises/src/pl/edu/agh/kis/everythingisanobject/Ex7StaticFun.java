package pl.edu.agh.kis.everythingisanobject;

/** 
 * Program skompilowany z kawałków
 * @author Szymon
 * @version 1.7a
 */
public class Ex7StaticFun {
	
	/**
	 * Prosta klasa przeprowadzająca inkrementację
	 */
	static void incr()
	{
		StaticTest.i++;
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	static public void main(String[] args)
	{
		Ex7StaticFun sf = new Ex7StaticFun();
		
		System.out.println("Przed inkrementowaniem: " + StaticTest.i);
		
		//odwołanie przez obiek
		
		sf.incr();

		System.out.println("Po inkrementowaniu przez obiekt: " + StaticTest.i);
		
		//odwołanie przez klase
		
		Ex7StaticFun.incr();

		System.out.println("Po inkrementowaniu przez klasę: " + StaticTest.i);
	}
}

/**
 * Klasa utworzona do testu klasy StaticFun
 * @author Szymon
 *
 */
class StaticTest {

	static int i = 47;
}
/* Output 100% match
Przed inkrementowaniem: 47
Po inkrementowaniu przez obiekt: 48
Po inkrementowaniu przez klasę: 49
*///:~