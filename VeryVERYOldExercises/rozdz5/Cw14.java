package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw14 */

/**
 * Klasa udowadniająca inicjalizacj statyczne
 * @author Szymon Majkut
 *
 */
public class Cw14 {
	
	static String s1 = "Inicjalizacja przy definicji \n";
	static String s2;
	
	static
	{
		s2 = "Inicjalizacja w bloku static \n";
	}

	static void f()
	{
		System.out.print(s1 + s2);
	}

	public static void main(String[] args) 
	{
		Cw14.f();
	}
	/* Output (100%match)
	 * Inicjalizacja przy definicji 
	 * Inicjalizacja w bloku static 
	 *///:~
}
