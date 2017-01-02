package pl.edu.agh.kis.reusingclasses;

/**
 * Domyślny konstruktor klasy pochodnej
 * @author Szymon Majkut
 *
 */
public class Ex3 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Cartoon c = new Cartoon();
	}
}

class Art {
	Art() { System.out.println("Konstruktor klasy Art"); }
}
class Drawing extends Art{
	Drawing() { System.out.println("Konstruktor klasy Drawing"); }
}
/**
 * Utworzenie konstruktora domyślnego
 * @author szymek
 *
 */
class Cartoon extends Drawing {
}
/* Output (100%match)
 * Konstruktor klasy Art
 * Konstruktor klasy Drawing
 *///:~