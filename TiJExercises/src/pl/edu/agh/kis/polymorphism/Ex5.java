package pl.edu.agh.kis.polymorphism;

/**
 * Modyfikacja ćwiczenia pierwszego
 * @author Szymon
 *
 */
public class Ex5 {

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
/* Output (100%match)
 * 1 Wheels!
 * 2 Wheels!
 * 3 Wheels!
 *///:~