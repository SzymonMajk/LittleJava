package pl.edu.agh.kis.rozdz6;
/**: Object/pl/edu/agh/kis/rozdz6/Cw6 */

/**
 * Metoda klasy manipulująca składnikami chornionymi drugiej klasy
 * @author Szymon Majkut
 *
 */
class Prot {
	protected int i;
	protected int j;
}
public class Cw6 {

	void manipulate(int i, int j,Prot p)
	{
		p.i = i;
		p.j = j;
		
		System.out.println(i + " " + j);
	}
	public static void main(String[] args)
	{
		Prot p = new Prot();
		Cw6 c = new Cw6();
		
		c.manipulate(4,5,p);
	}
	/* Output (100%match)
	 * 4 5
	 *///:~
}
