package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw19 */

/**
 * Funkcja przyjmująca nieokreśloną liczbę argumentów
 * możemy podawać także tablice
 * @author Szymon Majkut
 *
 */
public class Cw19 {

	void f(String...strings)
	{
		for(String i : strings)
		{
			System.out.print(i + ", ");
		}
	}
	
	public static void main(String[] args)
	{
		Cw19 c = new Cw19();
		
		c.f("trzy","dwa","jeden");
		String[] tab = {"jeden", "dwa", "trzy"};
		c.f(tab);
	}
	/* Output (100%match)
	 * trzy, dwa, jeden, jeden, dwa, trzy, 
	 *///:~
}