package pl.edu.agh.kis.polymorphism;

import java.util.Random;

/**
 * Generator losowych instrumentów
 * @author Szymon
 *
 */
public class Ex8 {

	static public RandomInstrumentGenerator gen = new RandomInstrumentGenerator();
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
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
}

class RandomInstrumentGenerator {
	private Random rand = new Random(47);
	public Instrument next()
	{
		switch(rand.nextInt(7))
		{
			default: // aby by�a pewno�� zwr�cenia obiektu
			case 0: return new Instrument();
			case 1: return new Wind();
			case 2: return new Percussion();
			case 3: return new Stringed();
			case 4: return new Brass();
			case 5: return new Woodwind();
			case 6: return new NewInstrument();
		}
	}
}
/* Output (33%match)
 * Strojenie Nowego Woodwindu!
 * Strojenie Percussionu!
 * Strojenie Nowego Woodwindu!
 *///:~