package pl.edu.agh.kis.reusingclasses;

/**
 * Ukazanie możliwości delegacji z klasy pakietowej Detergent
 * @author Szymon Majkut
 *
 */
public class Ex11 extends Detergent {

	private Detergent d = new Detergent();
	
	public void append(String a) { d.append(a); }
	public void dilute() { d.dilute(); }
	public void apply() { d.apply(); }
	public void scrub () { d.append(" Cw2.scrub()"); d.scrub(); }
	public void foam() { d.foam(); }
	public String toString() { return d.toString(); }
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		Ex11 c = new Ex11();
		c.scrub();
		c.dilute();
		System.out.println(c);
	}
}
/* Output (100%match)
 * Cleanser Cw2.scrub() Detergent.scrub()scrub()dilute()
 *///:~
