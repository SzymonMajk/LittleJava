package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw7 */

/**
 * Przykład inicjalizacji wszystkich pól w konstruktorze
 * @author Szymon Majkut
 *
 */
public class Cw7 {
	
	public static void main(String[] args) 
	{
		C2 c = new C2();
	}
	/* Output (100%match)
	 * Jestem konstruktorem A!5
	 * Jestem konstruktorem B!6
	 *///:~
}

class A2 {
	A2(int i) { System.out.println("Jestem konstruktorem A!" + i); }
}
class B2 {
	B2(int i) { System.out.println("Jestem konstruktorem B!" + i); }
}
class C2 extends A2{
	B2 b;
	C2() 
	{
		super(5);
		b = new B2(6);
	}
}
