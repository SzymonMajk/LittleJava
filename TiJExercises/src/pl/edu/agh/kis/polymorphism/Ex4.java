package pl.edu.agh.kis.polymorphism;

/**
 * Sprawdzenie czy dodanie nowego typu dziedziczącego po Shape zadziała
 * równie dobrze jak dla poprzednich
 * @author Szymon
 *
 */
public class Ex4 {

	public static RandomShapeGenerator gen = new RandomShapeGenerator();
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Shape s = new NewShape();
		s.draw();
		s.erase();
	}
}

class NewShape extends Shape {
	@Override public void draw() {System.out.println("Działam?");}
	@Override public void erase() {System.out.println("Działam?");}
}
/* Output (33%match)
 * Działam?
 * Działam?
 *///:~