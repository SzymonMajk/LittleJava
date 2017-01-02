package pl.edu.agh.kis.polymorphism;

/**
 * Metoda balance, która nie występuje we wszystkich dziedziczonych
 * jak widać, obiekt wziął sobie metodę z klasy bazowej,
 * jeżeli sam jej nie przedefiniowywał!
 * @author Szymon
 *
 */
public class Ex17 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Cycle2[] c = { new Unicycle2(),
			new Bicycle2(), new Tricycle2() };
		for(Cycle2 cc : c)
		{
			cc.balance();
		}
	}
}

class Cycle2 {
	public void balance() { System.out.println("Balance Cycle!"); }
	public void wheels() {
		System.out.println("2!");
	}
}

class Unicycle2 extends Cycle2 {
	public void balance() { System.out.println("Balance Unicycle!"); }
	public void wheels() {
		System.out.println("1!");
	}
}

class Bicycle2 extends Cycle2 {
	public void balance() { System.out.println("Balance Bicycle!"); }
	public void wheels() {
		System.out.println("2!");
	}
}

class Tricycle2 extends Cycle2 {
	public void wheels() {
		System.out.println("3!");
	}
}
/* Output (33%match)
 * Balance Unicycle!
 * Balance Bicycle!
 * Balance Cycle!
 *///:~