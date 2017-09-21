package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw21 */

/**
 * Przesłanianie metody finalnej
 * @author Szymon Majkut
 *
 */
public class Cw21 {


	
	public static void main(String[] args) 
	{
		OverridePrivate c = new OverridePrivate();
		
		//c.f();
		
		WithFinal c2 = c;
		
		//c2.f() Kompilator już przy pisaniu przesłoniętej metody poinforumuje o błędzie!
	}
	/* Output (100%match)
	 *///:~
}

class WithFinal {
	final void f() { System.out.println("Jestem prywatna! f():WithFinal"); }
}
class OverridePrivate extends WithFinal {
    public void f() { System.out.println("Jestem prywatna! f():OverridePrivate"); }
}
