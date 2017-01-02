package pl.edu.agh.kis.reusingclasses;

/**
 * Klasa wypisująca kilka komunikatów w różnych funkcjach
 * @author Szymon
 * @version 1.2
 *
 */
class Cleanser {
	private String s = "Cleanser";
	public void append(String a) { s += a; }
	public void dilute() { append("dilute()"); }
	public void apply() { append("apply()"); }
	public void scrub () { append("scrub()"); }
	public String toString() { return s; }
}

/**
 * Nadpisanie jednej z funkcji oraz wykorzystanie jednej z istniejących
 * w nowy sposób w nowej funkji
 * @author Szymon
 * @version 1.2
 *
 */
class Detergent extends Cleanser {
	public void scrub()
	{
		append(" Detergent.scrub()");
		super.scrub();
	}
	public void foam() { append("foam()"); }
}

/**
 * Prezentacja przesłaniania oraz dziedziczenia
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex2 extends Detergent {

	public void sterilize() { append(" sterilize()"); }
	public void scrub()
	{
		append(" Cw2.scrub()");
		super.scrub();
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Ex2 c = new Ex2();
		c.scrub();
		c.sterilize();
		System.out.println(c);
	}
}
/* Output (100%match)
 * Cleanser Cw2.scrub() Detergent.scrub()scrub() sterilize()
 *///:~
