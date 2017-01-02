package pl.edu.agh.kis.reusingclasses;

/**
 * Dodanie funkcji sprzątającej po klasach
 * @author Szymon Majkut
 *
 */
public class Ex12 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		//jeden ze sposobów wymuszenia uruchomiania funkcji dispose()
		Stem3 s = new Stem3();
		try{
			
		} finally {
			s.dispose();
		}
		
		
	}
}

class Component13 {
	Component13() { System.out.println("Jestem konstruktorem Component1!"); }
	public void dispose() { System.out.println("Usuwam Component1!"); }
}
class Component23 {
	Component23() { System.out.println("Jestem konstruktorem Component2!"); }
	public void dispose() { System.out.println("Usuwam Component2!"); }
}
class Component33 {
	Component33() { System.out.println("Jestem konstruktorem Component3!"); }
	public void dispose() { System.out.println("Usuwam Component3!"); }
}
class Root3 {
	Component13 c1;
	Component13 c2;
	Component13 c3;
	Root3()
	{
		System.out.println("Jestem konstruktorem Root!");
		c1 = new Component13();
		c2 = new Component13();
		c3 = new Component13();
	}
	public void dispose() { System.out.println("Usuwam Root!");
					c3.dispose(); c2.dispose(); c1.dispose(); }
}
class Stem3 extends Root3 {
	Component13 c1;
	Component13 c2;
	Component13 c3;
	Stem3()
	{
		super();
		System.out.println("Jestem konstruktorem Stem!");
		c1 = new Component13();
		c2 = new Component13();
		c3 = new Component13();
	}
	public void dispose() { System.out.println("Usuwam Stem!");  
	c3.dispose(); c2.dispose(); c1.dispose(); super.dispose(); }
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
 * Usuwam Stem!
 * Usuwam Component1!
 * Usuwam Component1!
 * Usuwam Component1!
 * Usuwam Root!
 * Usuwam Component1!
 * Usuwam Component1!
 * Usuwam Component1!
 *///:~