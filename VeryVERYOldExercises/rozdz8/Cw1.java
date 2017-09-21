package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw1 */

/**
 * Prezentacja rzutowania w górê
 * @author Szymon Majkut
 *
 */
public class Cw1 {

	public static void main(String[] args) 
	{
		Unicycle u = new Unicycle();
		Bicycle b = new Bicycle();
		Tricycle t = new Tricycle();
		Rider.ride(u);
		Rider.ride(b);
		Rider.ride(t);
	}
	/* Output (100%match)
	 * 1!
	 * 2!
	 * 3!
	 *///:~
}

class Rider {
	public static void ride(Cycle c)
	{
		c.wheels();
	}
}

class Cycle {
	public void wheels() {
		System.out.println("2!");
	}
}

class Unicycle extends Cycle {
	public void wheels() {
		System.out.println("1!");
	}
}

class Bicycle extends Cycle {
	public void wheels() {
		System.out.println("2!");
	}
}

class Tricycle extends Cycle {
	public void wheels() {
		System.out.println("3!");
	}
}