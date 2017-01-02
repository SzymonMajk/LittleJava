package pl.edu.agh.kis.reusingclasses;

/**
 * Konstruktor bazowej zawsze jest uruchamiany przed ciałem 
 * konstruktora pochodnej dla tej bazowej
 * @author Szymon Majkut
 *
 */
public class Ex4 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		ExClass c = new ExClass();
	}
}

class BaseCLass {
	BaseCLass() { System.out.println("Jestem konstruktorem klasy bazowej!"); }
}
class ExClass extends BaseCLass{
	ExClass() { System.out.println("Jestem konstruktorem klasy pochodnej!"); }
}
/* Output (100%match)
 * Jestem konstruktorem klasy bazowej!
 * Jestem konstruktorem klasy pochodnej!
 *///:~