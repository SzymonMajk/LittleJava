package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw4 */

/**
 * Dla nowego te¿ dzia³a!
 * @author Szymon Majkut
 *
 */
public class Cw4 {

	public static RandomShapeGenerator gen = new RandomShapeGenerator();
	
	public static void main(String[] args) 
	{
		Shape s = new NowyTyp();
		s.draw();
		s.erase();
	}
	/* Output (33%match)
	 * Dzia³am?
	 * Dzia³am?
	 *///:~
}

class NowyTyp extends Shape {
	@Override public void draw() {System.out.println("Dzia³am?");}
	@Override public void erase() {System.out.println("Dzia³am?");}
}