package pl.edu.agh.kis.reusingclasses;

/**
 * Najpierw konstruktor bazowej, później komponentów
 * @author Szymon Majkut
 *
 */
public class Ex5 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		C c = new C();
	}
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
/* Output (100%match)
 * Jestem konstruktorem A!
 * Jestem konstruktorem B!
 *///:~