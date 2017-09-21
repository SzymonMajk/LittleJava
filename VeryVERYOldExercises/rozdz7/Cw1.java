package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw1 */

/**
 * Prosta inicjalizacja leniwa
 * @author Szymon Majkut
 *
 */
public class Cw1 {

	Need n;
	
	private void printNeed()
	{
		if(n == null)
		{
			n = new Need();
		}
		System.out.println(n);
	}
	
	public static void main(String[] args) 
	{
		Cw1 c = new Cw1();
		c.printNeed();
	}
	/* Output (100%match)
	 * Inicjalizuję klasę składową.
	 *///:~
}
class Need {
	private String s;
	
	Need()
	{
		s = "Inicjalizuję klasę składową.";
	}
	public String toString()
	{
		return s;
	}
}
