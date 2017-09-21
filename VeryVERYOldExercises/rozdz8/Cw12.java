package pl.edu.agh.kis.rozdz8;

import java.util.Random;

/**: Object/pl/edu/agh/kis/rozdz7/Cw12 */

/**
 * Demonstracja kolejno�ci inicjalizacji
 * @author Szymon Majkut
 *
 */
public class Cw12 {
	
	static public RandomRodentGenerator2 gen = new RandomRodentGenerator2();
	
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
	/* Output (33%match)
	 * Tworz� Rodent!
	 * 2
	 * Tworz� Mouse!
	 * 2 4
	 * Tworz� Rodent!
	 * 2
	 * Tworz� Mouse!
	 * 2 4
	 * Chomik gryzie!
	 * Chomik gryzie!
	 *///:~
}

class Rodent2 {
	int i = 2;
	void gnaw() { System.out.println("Gryzo� gryzie!"); }
	Rodent2() { System.out.println("Tworz� Rodent!"); System.out.println(i); }
}

class Mouse2 extends Rodent2 {
	int j = 3;
	@Override void gnaw() { System.out.println("Mysz gryzie!"); }
	Mouse2() { System.out.println("Tworz� Mouse!"); System.out.println(i + " " + j); }
}

class Hamster2 extends Rodent2  {
	int k = 4;
	@Override void gnaw() { System.out.println("Chomik gryzie!"); }
	Hamster2() { System.out.println("Tworz� Mouse!"); System.out.println(i + " " + k); }
}

class RandomRodentGenerator2 {
	private Random rand = new Random(47);
	public Rodent2 next()
	{
		switch(rand.nextInt(3))
		{
			default: // aby by�a pewno�� zwr�cenia obiektu
			case 0: return new Rodent2();
			case 1: return new Mouse2();
			case 2: return new Hamster2();

		}
	}
}