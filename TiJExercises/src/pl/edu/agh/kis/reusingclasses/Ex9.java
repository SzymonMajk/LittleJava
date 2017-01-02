package pl.edu.agh.kis.reusingclasses;

/**
 * Kolejna sztuczka z dużą liczbą klas, prezentacja ładowania komponentów
 * @author Szymon Majkut
 *
 */
public class Ex9 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Stem s = new Stem();
	}
}

class Component1 {
	Component1() { System.out.println("Jestem konstruktorem Component1!"); }
}
class Component2 {
	Component2() { System.out.println("Jestem konstruktorem Component2!"); }
}
class Component3 {
	Component3() { System.out.println("Jestem konstruktorem Component3!"); }
}
class Root {
	Component1 c1;
	Component1 c2;
	Component1 c3;
	Root()
	{
		System.out.println("Jestem konstruktorem Root!");
		c1 = new Component1();
		c2 = new Component1();
		c3 = new Component1();
	}
}
class Stem extends Root {
	Component1 c1;
	Component1 c2;
	Component1 c3;
	Stem()
	{
		super();
		System.out.println("Jestem konstruktorem Stem!");
		c1 = new Component1();
		c2 = new Component1();
		c3 = new Component1();
	}
}
/* Output (100%match)
 * Jestem konstruktorem Root!
 * Jestem konstruktorem Component1!
 * Jestem konstruktorem Component1!
 * Jestem konstruktorem Component1!
 * Jestem konstruktorem Stem!
 * Jestem konstruktorem Component1!
 * Jestem konstruktorem Component1!
 * Jestem konstruktorem Component1!
 *///:~