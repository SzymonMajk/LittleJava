package pl.edu.agh.kis.polymorphism;

/**
 * Prezentacja rzutowania w górę na przykładzie hierarchi klas rowerowych
 * @author Szymon
 *
 */
public class Ex1 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Unicycle u = new Unicycle();
		Bicycle b = new Bicycle();
		Tricycle t = new Tricycle();
		Rider.ride(u);
		Rider.ride(b);
		Rider.ride(t);
	}
}

class Rider {
	public static void ride(Cycle c)
	{
		c.wheels();
	}
}

class Cycle {
	public void wheels() {
		System.out.println("2 Wheels!");
	}
}

class Unicycle extends Cycle {
	public void wheels() {
		System.out.println("1 Wheels!");
	}
}

class Bicycle extends Cycle {
	public void wheels() {
		System.out.println("2 Wheels!");
	}
}

class Tricycle extends Cycle {
	public void wheels() {
		System.out.println("3 Wheels!");
	}
}
/* Output (100%match)
 * 1 Wheels!
 * 2 Wheels!
 * 3 Wheels!
 *///:~