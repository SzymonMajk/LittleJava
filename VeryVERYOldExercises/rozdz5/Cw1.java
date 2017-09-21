package pl.edu.agh.kis.rozdz5;

/**: Object/pl/edu/agh/kis/rozdz5/Cw1 */

/**
 * Klasa z niezainicjowaną referencją String
 * @author Szymon Majkut
 *
 */
public class Cw1 {

	String s;
	
	/**
	 * Wykażemy, że s został zainicjowany wartością null
	 */
	public static void main(String[] args) 
	{
		Cw1 c = new Cw1();
		
		if(c.s == null)
		{
			System.out.println("s jest równy null");
		}

	}
	/* Output (100%match)
	 * s jest równy null
	 *///:~

}
