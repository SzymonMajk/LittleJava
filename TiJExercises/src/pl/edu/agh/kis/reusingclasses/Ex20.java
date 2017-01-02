package pl.edu.agh.kis.reusingclasses;

/**
 * Prezentacja zastosowania adnotacji @Override, jako zabezpieczenia
 * przed problemem nie przesłaniania finalnych metod prywatnych
 * @author Szymon Majkut
 *
 */
public class Ex20 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		OverridngPrivate2 c = new OverridngPrivate2();
		
		c.f();
		c.g();
		
		//możemy zrzutować w górę, ale nie możemy wywołać metod
		OverridngPrivate c2 = c;
		
		//c2.f();// nie możemy wywołać tych metod, gdyż nie zostały
		c2.g(); //przesłonięte w klasie pochodnej, ale po prostu są niewidoczne!
	}
}

class WithFinals {
	private final void f() { System.out.println("Jestem prywatna! f():WithFinals"); }
	private void g() { System.out.println("Jestem prywatna! g():WithFinals"); }
}
class OverridngPrivate extends WithFinals {
	private final void f() { System.out.println("Jestem prywatna! f():OverridingPrivate"); }
	//ustalmy dla metody g() dostęp pakietowy
	void g() { System.out.println("Jestem prywatna! g():OverridingPrivate"); }
}
class OverridngPrivate2 extends OverridngPrivate {
	 public final void f() { System.out.println("Jestem prywatna! f():OverridingPrivate2"); }
	 //teraz dzięki adnotacji wiemy, że g() zostało przesłonięte, ale itak
	 //zostanie uruchomiona ta wersja, z powodu późnego wiązania
	 @Override public void g() { System.out.println("Jestem prywatna! g():OverridingPrivate2"); }
}
/* Output (100%match)
 * Jestem prywatna! f():OverridingPrivate2
 * Jestem prywatna! g():OverridingPrivate2
 * Jestem prywatna! g():OverridingPrivate2
 *///:~