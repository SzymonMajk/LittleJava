package pl.edu.agh.kis.polymorphism;

import java.util.Random;

/**
 * Dopisanie @Override do przykładu
 * @author Szymon
 *
 */
public class Ex2 {

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
			ss.draw();
			ss.erase();
		}
	}
}

class Shape {
	public void draw() {}
	public void erase() {}
	public void nieprzeslaniana() { System.out.println("Ta funkcja nie jest przesłaniana"); }
	public void przeslonietaWCircle() {}
	public void przeslonietaWszedzie() {}
}

class Circle extends Shape {
	@Override public void draw() { System.out.println("Circle draw."); }
	@Override public void erase() { System.out.println("Circle erase."); }
	@Override public void przeslonietaWCircle() { System.out.println("Circle mnie przesłania!"); }
	@Override public void przeslonietaWszedzie() { System.out.println("Circle mnie przesłania! nie tylko ona!"); }
}

class Square extends Shape {
	@Override public void draw() { System.out.println("Square draw."); }
	@Override public void erase() { System.out.println("Square erase."); }
	@Override public void przeslonietaWszedzie() { System.out.println("Square mnie przesłania! nie tylko ona!"); }
}

class Triangle extends Shape {
	@Override public void draw() { System.out.println("Triangle draw."); }
	@Override public void erase() { System.out.println("Triangle erase."); }
	@Override public void przeslonietaWszedzie() { System.out.println("Triangle mnie przesłania! nie tylko ona!"); }
}

class RandomShapeGenerator {
	private Random rand = new Random(47);
	public Shape next()
	{
		switch(rand.nextInt(3))
		{
			default: // aby była pewność zwrócenia obiektu
			case 0: return new Circle();
			case 1: return new Square();
			case 2: return new Triangle();
		}
	}
}
/* Output (33%match)
 * Triangle draw.
 * Triangle erase.
 * Triangle draw.
 * Triangle erase.
 * Square draw.
 * Square erase.
 * Triangle draw.
 * Triangle erase.
 * Square draw.
 * Square erase.
 * Triangle draw.
 * Triangle erase.
 * Square draw.
 * Square erase.
 * Triangle draw.
 * Triangle erase.
 * Circle draw.
 * Circle erase.
 *///:~