package pl.edu.agh.kis.reusingclasses;

/**
 * Konstruktor domyślny, a w nim sparametryzowany
 * @author Szymon Majkut
 *
 */
public class Ex8 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		FirstFloor f1 = new FirstFloor();
		FirstFloor f2 = new FirstFloor();
	}
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
/* Output (100%match)
 * Jestem inny niż domyślny!5
 * Jestem pochodnej, nie mam argumentów
 * Jestem inny niż domyślny!5
 * Jestem pochodnej, nie mam argumentów
 *///:~