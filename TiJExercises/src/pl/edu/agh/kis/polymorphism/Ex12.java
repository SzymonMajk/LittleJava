package pl.edu.agh.kis.polymorphism;

import java.util.Random;

/**
 * Demonstracja kolejności inicjalizacji
 * @author Szymon
 *
 */
public class Ex12 {
	
	static public RandomRodentGenerator2 gen = new RandomRodentGenerator2();
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Rodent2[] s = new Rodent2[2];
		for(int i = 0; i < s.length; ++i)
		{
			s[i] = gen.next();
		}
		for(Rodent2 r : s)
		{
			r.gnaw();
		}
	}
}

class Rodent2 {
	int i = 2;
	void gnaw() { System.out.println("Gryzoń gryzie!"); }
	Rodent2() { System.out.println("Tworzę Rodent!"); System.out.println(i); }
}

class Mouse2 extends Rodent2 {
	int j = 3;
	@Override void gnaw() { System.out.println("Mysz gryzie!"); }
	Mouse2() { System.out.println("Tworzę Mouse!"); System.out.println(i + " " + j); }
}

class Hamster2 extends Rodent2  {
	int k = 4;
	@Override void gnaw() { System.out.println("Chomik gryzie!"); }
	Hamster2() { System.out.println("Tworzę Hamster!"); System.out.println(i + " " + k); }
}

class RandomRodentGenerator2 {
	private Random rand = new Random(47);
	public Rodent2 next()
	{
		switch(rand.nextInt(3))
		{
			default:
			case 0: return new Rodent2();
			case 1: return new Mouse2();
			case 2: return new Hamster2();

		}
	}
}
/* Output (33%match)
 * Tworzę Rodent!
 * 2
 * Tworzę Hamster!
 * 2 4
 * Tworzę Rodent!
 * 2
 * Tworzę Hamster!
 * 2 4
 * Chomik gryzie!
 * Chomik gryzie!
 *///:~