package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw5 */

/**
 * Znów sprawdzenie czy polimorfizm siê nie popsuje w nowej klasie
 * @author Szymon Majkut
 *
 */
public class Cw5 {


	public static void main(String[] args) 
	{
		Unicycle u = new Unicycle();
		Bicycle b = new Bicycle();
		Tricycle t = new Tricycle();
		Rider.ride(u);
		Rider.ride(b);
		Rider.ride(t);
	}
	/* Output (33%match)
	 * 1!
	 * 2!
	 * 3!
	 *///:~
}