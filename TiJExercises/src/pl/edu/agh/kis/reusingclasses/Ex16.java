package pl.edu.agh.kis.reusingclasses;

/**
 * Prezentacja rzutowania w górę
 * @author Szymon Majkut
 *
 */
public class Ex16 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		Frog f = new Frog();
		((Amphibian)f).swim();
		((Amphibian)f).walk();
	}
}
/**
 * Prosta klasa bazowa, z której będziemy dziedziczyć, posiada
 * dwie metody możliwe do uruchomienia
 * @author Szymon
 *
 */
class Amphibian {
	
	public void swim() { System.out.println("Amphibian swim!"); }
	public void walk() { System.out.println("Amphibian walk!"); }
}
/**
 * Klasa która nie robi nic oprócz dziedziczenia po Amphibian
 * @author Szymon
 *
 */
class Frog extends Amphibian{
	// nothing
}
/* Output (100%match)
 * Amphibian swim!
 * Amphibian walk!
 *///:~