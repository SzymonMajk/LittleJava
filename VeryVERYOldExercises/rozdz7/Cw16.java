package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw16 */

/**
 * Prosta inicjalizacja leniwa
 * @author Szymon Majkut
 *
 */
public class Cw16 {
	
	public static void main(String[] args) 
	{
		Frog f = new Frog();
		((Amphibian)f).swim();
		((Amphibian)f).walk();
	}
	/* Output (100%match)
	 * Amphibian swim!
	 * Amphibian walk!
	 *///:~
}
class Amphibian {
	
	public void swim() { System.out.println("Amphibian swim!"); }
	public void walk() { System.out.println("Amphibian walk!"); }
}
class Frog extends Amphibian{
	// nothing
}