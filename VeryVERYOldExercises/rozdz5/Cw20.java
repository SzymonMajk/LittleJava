package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw20 */

/**
 * Main z możliwością przyjęcia zmiennej liczby argumentów
 * @author Szymon Majkut
 *
 */
public class Cw20 {

	void f(String...strings)
	{
		for(String i : strings)
		{
			System.out.print(i + ", ");
		}
	}
	
	public static void main(String...args)
	{
		for(String i : args)
		{
			System.out.println(i);
		}
	}
	/* Output (100%match) 
	 *///:~
}