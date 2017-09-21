package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw2 */


class Cleanser {
	private String s = "Cleanser";
	public void append(String a) { s += a; }
	public void dilute() { append("dilute()"); }
	public void apply() { append("apply()"); }
	public void scrub () { append("scrub()"); }
	public String toString() { return s; }
}

class Detergent extends Cleanser {
	public void scrub()
	{
		append(" Detergent.scrub()");
		super.scrub();
	}
	public void foam() { append("foam()"); }
}

/**
 * Pokazanie przesłaniania i dziedziczenia
 * @author Szymon Majkut
 *
 */
public class Cw2 extends Detergent {

	public void sterilize() { append(" sterilize()"); }
	public void scrub()
	{
		append(" Cw2.scrub()");
		super.scrub();
	}
	
	public static void main(String[] args) 
	{
		Cw2 c = new Cw2();
		c.scrub();
		c.sterilize();
		System.out.println(c);
	}
	/* Output (100%match)
	 * Cleanser Cw2.scrub() Detergent.scrub()scrub() sterilize()
	 *///:~
}

