package pl.edu.agh.kis.polymorphism;

/**
 * Problem używania klas polimorficznych w konstruktorze, gdzie
 * mamy dostęp do funkcji korzystającej z jeszcze niezainicjowanych
 * zmiennych
 * @author Szymon
 *
 */
public class Ex15 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		new RectangularGlyph(1,2);
	}
}

class Glyph {
	void draw() { System.out.println("Glyph draw()");}
	Glyph()
	{
		System.out.println("przed draw()");
		draw();
		System.out.println("po draw()");
	}
}

class RoundGlyph extends Glyph {
	private int radius = 1;
	void draw() { System.out.println("RoundGlyph draw() radius = " + radius);}
	RoundGlyph(int i)
	{
		radius = i;
		System.out.println("RoundGlyph draw() radius = " + radius);
	}
}

class RectangularGlyph extends Glyph {
	private int x = 1;
	private int y = 1;
	void draw() { System.out.println("RectangularGlyph draw() x = " + x + ", y = " + y);}
	RectangularGlyph(int x, int y)
	{
		this.x = x;
		this.y = y;
		System.out.println("RectangularGlyph draw() x = " + x + ", y = " + y);
	}
}
/* Output (33%match)
 * przed draw()
 * RoundGlyph draw() x = 0, y = 0
 * po draw()
 * RoundGlyph draw() x = 1, y = 2
 *///:~