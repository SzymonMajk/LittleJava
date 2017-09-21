package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw8 */

/**
 * Klasa obrazująca istnienie referencji this
 * @author Szymon Majktu
 *
 */
public class Cw8 {

	void f()
	{
		System.out.println("Działa, ale nieeee korzystamy!");
	}
	void g()
	{
		f();
		this.f();
	}
	public static void main(String[] args) 
	{
		Cw8 c = new Cw8();
		
		c.g();
	}
	/* Output (100%match)
	 * Działa, ale nieeee korzystamy!
	 * Działa, ale nieeee korzystamy!
	 *///:~
}
