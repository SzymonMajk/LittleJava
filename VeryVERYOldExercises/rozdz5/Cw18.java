package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw18 */

/**
 * Tablica referencji do obieków z inicjalizajcą
 * @author Szymon Majkut
 *
 */
public class Cw18 {


	Cw18(String s)
	{
		System.out.print(s + " ");
	}
	
	public static void main(String[] args)
	{
		Cw17[] tab = { new Cw17("jeden"),  new Cw17("dwa"),  new Cw17("trzy")};

	}
	/* Output (100%match)
	 * jeden dwa trzy 
	 *///:~
}
