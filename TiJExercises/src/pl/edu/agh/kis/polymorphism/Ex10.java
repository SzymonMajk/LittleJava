package pl.edu.agh.kis.polymorphism;

/**
 * Bazowa z dwiema metodami, jedna wywołana w drugiej
 * @author Szymon
 *
 */
public class Ex10 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		B b = new B();
		((A)b).first(); 
		// czyli metoda second została wzięta z clasy B, bo została nadpisana
		//i zadziałało późne wiązanie!
	}
}

class A {
	void first() { System.out.println("first in A"); second(); }
	void second() { System.out.println("second in A"); }
}

class B extends A {
	@Override void second() { System.out.println("second in B"); }
}
/* Output (33%match)
 * first in A
 * second in B
 *///:~