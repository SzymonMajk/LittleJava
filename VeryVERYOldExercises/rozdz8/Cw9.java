package pl.edu.agh.kis.rozdz8;

import java.util.Random;

/**: Object/pl/edu/agh/kis/rozdz7/Cw9 */

/**
 * Hierarchia dziedziczenia dla gryzoni
 * @author Szymon Majkut
 *
 */
public class Cw9 {

	static public RandomRodentGenerator gen = new RandomRodentGenerator();
	
	public static void main(String[] args) 
	{
		Rodent[] s = new Rodent[5];
		for(int i = 0; i < s.length; ++i)
		{
			s[i] = gen.next();
		}
		for(Rodent r : s)
		{
			r.gnaw();
		}
	}
	/* Output (33%match)
	 * Chomik gryzie!
	 * Chomik gryzie!
	 * Mysz gryzie!
	 * Chomik gryzie!
	 * Mysz gryzie!
	 *///:~
}

class Rodent {
	void gnaw() { System.out.println("Gryzoñ gryzie!"); }
}

class Mouse extends Rodent {
	@Override void gnaw() { System.out.println("Mysz gryzie!"); }
}

class Hamster extends Rodent  {
	@Override void gnaw() { System.out.println("Chomik gryzie!"); }
}

class RandomRodentGenerator {
	private Random rand = new Random(47);
	public Rodent next()
	{
		switch(rand.nextInt(3))
		{
			default: // aby by³a pewnoœæ zwrócenia obiektu
			case 0: return new Rodent();
			case 1: return new Mouse();
			case 2: return new Hamster();

		}
	}
}