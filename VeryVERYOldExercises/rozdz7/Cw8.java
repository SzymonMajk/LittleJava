package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw8 */

/**
 * Konstruktor domyślny, a w nim sparametryzowany
 * @author Szymon Majkut
 *
 */
public class Cw8 {
	
	public static void main(String[] args) 
	{
		FirstFloor f1 = new FirstFloor();
		FirstFloor f2 = new FirstFloor();
	}
	/* Output (100%match)
	 * Jestem inny niż domyślny!5
	 * Jestem pochodnej, nie mam argumentów
	 * Jestem inny niż domyślny!5
	 * Jestem pochodnej, nie mam argumentów
	 *///:~
}

class Basement {
	Basement(int i) { System.out.println("Jestem inny niż domyślny!" + i); }
}
class FirstFloor extends Basement{
	FirstFloor() 
	{ 
		super(5); 
		System.out.println("Jestem pochodnej, nie mam argumentów");
	}
	FirstFloor(int i) 
	{ 
		super(4);
		System.out.println("Jestem pochodnej, mam argument! " + i); 
	}
}
