package pl.edu.agh.kis.reusingclasses;

/**
 * Przykład inicjalizacji wszystkich pól w konstruktorze
 * @author Szymon Majkut
 *
 */
public class Ex7 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		C2 c = new C2();
	}
}

/**
 * Aby nie psuć efektu wizuanego poprzednich ćwiczeń, konkretne
 * klasy będę przepisywał dodając kolejne liczby po nazwie
 * @author Szymon
 *
 */

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
/* Output (100%match)
 * Jestem konstruktorem A!5
 * Jestem konstruktorem B!6
 *///:~