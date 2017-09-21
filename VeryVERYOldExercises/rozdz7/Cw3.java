package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw3 */

/**
 * Domyślny konstruktor klasy pochodnej
 * @author Szymon Majkut
 *
 */
public class Cw3 {
	
	public static void main(String[] args) 
	{
		Cartoon c = new Cartoon();
	}
	/* Output (100%match)
	 * Konstruktor klasy Art
	 * Konstruktor klasy Drawing
	 *///:~
}

class Art {
	Art() { System.out.println("Konstruktor klasy Art"); }
}
class Drawing extends Art{
	Drawing() { System.out.println("Konstruktor klasy Drawing"); }
}
class Cartoon extends Drawing {
	// zostanie stworzony konstruktor domyślny
}