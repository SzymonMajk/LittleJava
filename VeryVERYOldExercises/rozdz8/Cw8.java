package pl.edu.agh.kis.rozdz8;

import java.util.Random;

/**: Object/pl/edu/agh/kis/rozdz7/Cw8 */

/**
 * Generator losowy instrumentów
 * @author Szymon Majkut
 *
 */
public class Cw8 {

	static public RandomInstrumentGenerator gen = new RandomInstrumentGenerator();
	
	public static void main(String[] args) 
	{
		Instrument[] s = new Instrument[3];
		for(int i = 0; i < s.length; ++i)
		{
			s[i] = gen.next();
		}
		for(Instrument i : s)
		{
			i.adjust();
		}
	}
	/* Output (33%match)
	 * Strojenie Nowego Woodwindu!
	 * Strojenie Percussionu!
	 * Strojenie Nowego Woodwindu!
	 *///:~
}

class RandomInstrumentGenerator {
	private Random rand = new Random(47);
	public Instrument next()
	{
		switch(rand.nextInt(7))
		{
			default: // aby by³a pewnoœæ zwrócenia obiektu
			case 0: return new Instrument();
			case 1: return new Wind();
			case 2: return new Percussion();
			case 3: return new Stringed();
			case 4: return new Brass();
			case 5: return new Woodwind();
			case 6: return new NowyInstrument();
		}
	}
}