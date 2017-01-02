package pl.edu.agh.kis.polymorphism;

import java.util.Random;

/**
 * Hierarchia dziedziczenia dla gryzoni
 * @author Szymon
 *
 */
public class Ex9 {

	static public RandomRodentGenerator gen = new RandomRodentGenerator();
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
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
}

class Rodent {
	void gnaw() { System.out.println("Gryzoń gryzie!"); }
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
			default: // aby była pewność zwrócenia obiektu
			case 0: return new Rodent();
			case 1: return new Mouse();
			case 2: return new Hamster();

		}
	}
}
/* Output (33%match)
 * Chomik gryzie!
 * Chomik gryzie!
 * Mysz gryzie!
 * Chomik gryzie!
 * Mysz gryzie!
 *///:~