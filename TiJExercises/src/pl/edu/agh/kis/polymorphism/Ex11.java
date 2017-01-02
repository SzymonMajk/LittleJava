package pl.edu.agh.kis.polymorphism;

/**
 * Dodanie klasy, ogólnie o odpalaniu konstruktorów baozwej
 * @author Szymon
 *
 */
public class Ex11 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		new Sandwich();
	}
}

class Meal {
	Meal() { System.out.println("Meal()"); }
}

class Bread {
	Bread() { System.out.println("Bread()"); }
}

class Chesse {
	Chesse() { System.out.println("Chesse()"); }
}

class Lettuce {
	Lettuce() { System.out.println("Lettuce()"); }
}

class Pickle {
	Pickle() { System.out.println("Pickle()"); }
}

class Lunch extends Meal {
	Lunch() { System.out.println("Lunch()"); }
}

class PortableLunch extends Lunch {
	PortableLunch() { System.out.println("PortableLunch()"); }
}

class Sandwich extends PortableLunch {
	private Bread b = new Bread();
	private Chesse c = new Chesse();
	private Lettuce l = new Lettuce();
	private Pickle p = new Pickle();
}
/* Output (33%match)
 * Meal()
 * Lunch()
 * PortableLunch()
 * Bread()
 * Chesse()
 * Lettuce()
 * Pickle()
 *///:~