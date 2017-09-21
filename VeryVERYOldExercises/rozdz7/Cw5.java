package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw5 */

/**
 * Najpierw konstruktor bazowej, później komponentów
 * @author Szymon Majkut
 *
 */
public class Cw5 {
	
	public static void main(String[] args) 
	{
		C c = new C();
	}
	/* Output (100%match)
	 * Jestem konstruktorem A!
	 * Jestem konstruktorem B!
	 *///:~
}

class A {
	A() { System.out.println("Jestem konstruktorem A!"); }
}
class B {
	B() { System.out.println("Jestem konstruktorem B!"); }
}
class C extends A{
	B b = new B();
}
