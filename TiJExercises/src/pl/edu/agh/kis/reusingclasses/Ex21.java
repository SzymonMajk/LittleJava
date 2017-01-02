package pl.edu.agh.kis.reusingclasses;

/**
 * Przesłanianie metody finalnej
 * @author Szymon Majkut
 *
 */
public class Ex21 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		OverridePrivate c = new OverridePrivate();
		
		c.f();
		
		WithFinal c2 = c;
		
		c2.f() ;//Kompilator już przy pisaniu przesłoniętej metody poinforumuje o błędzie!
	}
}

class WithFinal {
	final void f() { System.out.println("Jestem finalna! f():WithFinal"); }
}
class OverridePrivate extends WithFinal {
	//Kompilator nie pozwala na nadpisanie funkcji finalnej
    //@Override public void f() { System.out.println("Jestem prywatna! f():OverridePrivate"); }
}
/* Output (100%match)
 * Jestem finalna! f():WithFinal
 * Jestem finalna! f():WithFinal
 *///:~