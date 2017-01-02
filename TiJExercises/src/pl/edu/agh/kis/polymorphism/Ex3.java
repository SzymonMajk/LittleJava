package pl.edu.agh.kis.polymorphism;

/**
 * Zabawa z przesłanianiem, jak widać uruchamiana jest ta wersja metody
 * przeslonietawszedzie, na rzecz jakiego obiektu została wywołana, natomiast
 * metoda nieprzeslaniana, zostaje uruchomiona w swojej wersji z klasy bazowej
 * @author Szymon
 *
 */
public class Ex3 {

	public static RandomShapeGenerator gen = new RandomShapeGenerator();
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Shape[] s = new Shape[9];
		for(int i = 0; i < s.length; ++i)
		{
			s[i] = gen.next();
		}
		for(Shape ss : s)
		{
			ss.nieprzeslaniana();
			ss.przeslonietaWCircle();
			ss.przeslonietaWszedzie();
		}
	}
}
/* Output (33%match)
 * Ta funkcja nie jest przesłaniana
 * Triangle mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Triangle mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Square mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Triangle mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Square mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Triangle mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Square mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Triangle mnie przesłania! nie tylko ona!
 * Ta funkcja nie jest przesłaniana
 * Circle mnie przesłania!
 * Circle mnie przesłania! nie tylko ona!
 *///:~