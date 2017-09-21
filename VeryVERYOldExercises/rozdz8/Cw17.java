package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw17 */

/**
 * Metoda balance, która nie wystêpuje we wszystkich dziedziczonych
 * jak widaæ, wzi¹³ sobie metodê z klasy bazowej, je¿eli jej nie przedefiniowywa³!
 * @author Szymon Majkut
 *
 */
public class Cw17 {
	
	
	public static void main(String[] args) 
	{
		Cycle2[] c = { new Unicycle2(),
			new Bicycle2(), new Tricycle2() };
		for(Cycle2 cc : c)
		{
			cc.balance();
		}
	}
	/* Output (33%match)
	 * Balance Unicycle!
	 * Balance Bicycle!
	 * Balance Cycle!
	 *///:~
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