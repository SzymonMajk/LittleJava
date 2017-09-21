package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw15 */

/**
 * Klasa inicjująca obiekt niestatyczny w bloku inicjalizacji egzemplarza
 * @author Szymon Majkut
 *
 */
public class Cw15 {
	
	static String s;
	
	static
	{
		s = "Inicjalizacja w bloku inicjalizacji egzemplarza \n";
	}

	static void f()
	{
		System.out.print(s);
	}

	public static void main(String[] args) 
	{
		Cw15 c = new Cw15();
		c.f();
	}
	/* Output (100%match)
	 * Inicjalizacja w bloku inicjalizacji egzemplarza 
	 *///:~
}