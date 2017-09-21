package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw10 */

/**
 * Bazowa z dwiema metodami, jedna wywo³ana w drugiej
 * @author Szymon Majkut
 *
 */
public class Cw10 {
	
	public static void main(String[] args) 
	{
		B b = new B();
		((A)b).first(); // czyli metoda second zosta³a wziêta z clasy B, bo zosta³a nadpisana!
	}
	/* Output (33%match)
	 * first in A
	 * second in B
	 *///:~
}

class A {
	void first() { System.out.println("first in A"); second(); }
	void second() { System.out.println("second in A"); }
}

class B extends A {
	@Override void second() { System.out.println("second in B"); }
}