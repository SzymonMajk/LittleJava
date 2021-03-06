package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw6 */

/**
 * Przesłonięcie metody toString i jej polimorficzne zachowanie
 * @author Szymon Majkut
 *
 */
public class Cw6 {


	public static void main(String[] args) 
	{
		System.out.println(new Instrument());
		System.out.println(new Wind());
		System.out.println(new Percussion());
		System.out.println(new Stringed());
		System.out.println(new Brass());
		System.out.println(new Woodwind());
	}
	/* Output (33%match)
	 * Instrument
	 * Wind
	 * Percussion
	 * Stringed
	 * Brass
	 * Woodwind
	 *///:~
}

enum Note {
	MIDDLE_C;
}

class Instrument {
	public void play(Note n) { System.out.println("Instrument.play() " + n); }
	@Override public String toString() { return "Instrument"; }
	public void adjust() { System.out.println("Strojenie Instrumentu!"); }
}

class Wind extends Instrument {
	public void play(Note n) { System.out.println("Wind.play() " + n); }
	@Override public String toString() { return "Wind"; }
	public void adjust() { System.out.println("Strojenie Windu!"); }
}

class Percussion extends Instrument {
	public void play(Note n) { System.out.println("Percussion.play() " + n); }
	@Override public String toString() { return "Percussion"; }
	public void adjust() { System.out.println("Strojenie Percussionu!"); }
}

class Stringed extends Instrument {
	public void play(Note n) { System.out.println("Stringed.play() " + n); }
	@Override public String toString() { return "Stringed"; }
	public void adjust() { System.out.println("Strojenie Stringedu!"); }
}

class Brass extends Wind {
	public void play(Note n) { System.out.println("Brass.play() " + n); }
	@Override public String toString() { return "Brass"; }
	public void adjust() { System.out.println("Strojenie Brassu!"); }
}

class Woodwind extends Wind {
	public void play(Note n) { System.out.println("Woodwind.play() " + n); }
	@Override public String toString() { return "Woodwind"; }
	public void adjust() { System.out.println("Strojenie Woodwindu!"); }
}

class NowyInstrument extends Woodwind {
	public void play(Note n) { System.out.println("Nowy Woodwind.play() " + n); }
	@Override public String toString() { return "Nowy Woodwind"; }
	public void adjust() { System.out.println("Strojenie Nowego Woodwindu!"); }
}
class Music3
{
	public static void tune(Instrument i)
	{
		i.play(Note.MIDDLE_C);
	}
}