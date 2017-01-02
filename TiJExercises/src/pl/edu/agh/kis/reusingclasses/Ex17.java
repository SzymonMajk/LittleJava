package pl.edu.agh.kis.reusingclasses;

/**
 * Mimo zrzutowania w górę, zostają uruchomione przedefiniowane
 * wersje metod z klasy pochodnej, wynika z to z faktu, że wszystkie
 * metody niestatyczne są polimorficzne, oraz uruchomione zostało
 * późne wiązanie
 * @author Szymon Majkut
 *
 */
public class Ex17 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		Frog2 f = new Frog2();
		((Amphibian)f).swim();
		((Amphibian)f).walk();
	}
}
/**
 * Nowa wersja klasy Frog, tym razem przesłonimy metody klasy
 * Amphibian
 * @author szymek
 *
 */
class Frog2 extends Amphibian{
	
	public void swim() { System.out.println("Frog swim!"); }
	public void walk() { System.out.println("Frog Jump!"); }
}
/* Output (100%match)
 * Frog swim!
 * Frog Jump!
 *///:~