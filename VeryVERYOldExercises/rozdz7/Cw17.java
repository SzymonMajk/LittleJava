package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw17 */

/**
 * Prosta inicjalizacja leniwa
 * @author Szymon Majkut
 *
 */
public class Cw17 {
	
	public static void main(String[] args) 
	{
		Frog2 f = new Frog2();
		((Amphibian)f).swim();
		((Amphibian)f).walk();
	}
	/* Output (100%match)
	 * Frog swim!
	 * Frog Jump!
	 *///:~
}
class Frog2 extends Amphibian{
	
	public void swim() { System.out.println("Frog swim!"); }
	public void walk() { System.out.println("Frog Jump!"); }
}