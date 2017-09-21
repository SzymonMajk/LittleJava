package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw11 */

/**
 * Dodanie klasy, ogólnie o odpalaniu konstruktorów baozwej
 * @author Szymon Majkut
 *
 */
public class Cw11 {
	
	public static void main(String[] args) 
	{
		new Sandwich();
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