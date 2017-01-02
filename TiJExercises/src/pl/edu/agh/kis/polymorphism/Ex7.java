package pl.edu.agh.kis.polymorphism;

/**
 * Sprawdzenie czy dodanie nowej klasy nie popsuje polimorfizmu
 * @author Szymon
 *
 */
public class Ex7 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		System.out.println(new NewInstrument());

		Music3.tune(new NewInstrument());
	}
}

class NewInstrument extends Woodwind {
	public void play(Note n) { System.out.println("Nowy Woodwind.play() " + n); }
	@Override public String toString() { return "Nowy Woodwind"; }
	public void adjust() { System.out.println("Strojenie Nowego Woodwindu!"); }
}
/* Output (33%match)
 * Nowy Woodwind
 * Nowy Woodwind.play() MIDDLE_C
 *///:~