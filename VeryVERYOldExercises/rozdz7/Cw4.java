package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw4 */

/**
 * Kostruktory bazowe są wywołane przed konstruktorami pochodnymi
 * @author Szymon Majkut
 *
 */
public class Cw4 {
	
	public static void main(String[] args) 
	{
		ExClass c = new ExClass();
	}
	/* Output (100%match)
	 * Jestem konstruktorem klasy bazowej!
	 * Jestem konstruktorem klasy pochodnej!
	 *///:~
}

class BaseCLass {
	BaseCLass() { System.out.println("Jestem konstruktorem klasy bazowej!"); }
}
class ExClass extends BaseCLass{
	ExClass() { System.out.println("Jestem konstruktorem klasy pochodnej!"); }
}
