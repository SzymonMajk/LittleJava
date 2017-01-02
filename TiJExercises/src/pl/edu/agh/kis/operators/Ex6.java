package pl.edu.agh.kis.operators;

/**
 * Przykład wykorzystania operatorów ==, oraz metody equals()
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex6 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		Dog spot = new Dog("Spot","Hau!");
		Dog scruffy = new Dog("Scruffy","Wrr!");
		
		Dog ref;
		ref = spot;
		
		System.out.print("Spot + ref ==? : ");
		System.out.println(ref == spot);
		System.out.print("Scruffy + ref ==? : ");
		System.out.println(ref == scruffy);
		System.out.print("Spot + Scruffy ==? : ");
		System.out.println(scruffy == spot);
		System.out.println("Spot + ref eq? : " + ref.equals(spot));
		System.out.println("SCruffy + ref eq? : " + ref.equals(scruffy));
		System.out.println("Spot + Scruffy eq? : " + scruffy.equals(spot));
	}
}
/* Output 15% match
Spot + ref ==? : true
Scruffy + ref ==? : false
Spot + Scruffy ==? : false
Spot + ref eq? : true
SCruffy + ref eq? : false
Spot + Scruffy eq? : false
*///:~