package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw20 */

/**
 * Prezentacja zastosowania adnotacji @Override, jako zabezpieczenia
 * przed problemem nie przesłaniania finalnych metod prywatnych
 * @author Szymon Majkut
 *
 */
public class Cw20 {


	
	public static void main(String[] args) 
	{
		OverridngPrivate2 c = new OverridngPrivate2();
		
		c.f();
		c.g();
		
		OverridngPrivate c2 = c;
		
		//c2.f(); nie możemy wywołać tych metod, gdyż nie zostały
		//c2.g(); przesłonięte w klasie pochodnej, ale po prostu są niewidoczne!
	}
	/* Output (100%match)
	 *///:~
}

class WithFinals {
	private final void f() { System.out.println("Jestem prywatna! f():WithFinals"); }
	private void g() { System.out.println("Jestem prywatna! g():WithFinals"); }
}
class OverridngPrivate extends WithFinals {
    private final void f() { System.out.println("Jestem prywatna! f():OverridingPrivate"); }
	private void g() { System.out.println("Jestem prywatna! g():OverridingPrivate"); }
}
class OverridngPrivate2 extends OverridngPrivate {
	 @Override public final void f() { System.out.println("Jestem prywatna! f():OverridingPrivate2"); }
	 @Override public void g() { System.out.println("Jestem prywatna! g():OverridingPrivate2"); }
}