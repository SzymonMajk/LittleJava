package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw11 */

/**
 * Ukazanie możliwości delegacji z klasy pakietowej Detergent
 * @author Szymon Majkut
 *
 */
public class Cw11 extends Detergent {

	private Detergent d = new Detergent();
	
	public void append(String a) { d.append(a); }
	public void dilute() { d.dilute(); }
	public void apply() { d.apply(); }
	public void scrub () { d.append(" Cw2.scrub()"); d.scrub(); }
	public void foam() { d.foam(); }
	public String toString() { return d.toString(); }
	
	public static void main(String[] args) 
	{
		Cw11 c = new Cw11();
		c.scrub();
		c.dilute();
		System.out.println(c);
	}
	/* Output (100%match)
	 * Cleanser Cw2.scrub() Detergent.scrub()scrub()dilute()
	 *///:~
}

