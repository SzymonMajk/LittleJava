package pl.edu.agh.kis.reusingclasses;

/**
 * Lekka modyfikacja poprzedniego ćwizenia, żaden komponent
 * nie ma konstruktora domyślnego
 * @author Szymon Majkut
 *
 */
public class Ex10 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		BStem s = new BStem(66);
	}
}

class BComponent1 {
	BComponent1(int i) { System.out.println("Jestem konstruktorem Component1!" + i); }
}
class BComponent2 {
	BComponent2(int i) { System.out.println("Jestem konstruktorem Component2!" + i); }
}
class BComponent3 {
	BComponent3(int i) { System.out.println("Jestem konstruktorem Component3!" + i); }
}
class BRoot {
	BComponent1 c1;
	BComponent1 c2;
	BComponent1 c3;
	BRoot(int i)
	{
		System.out.println("Jestem konstruktorem Root!" + i);
		c1 = new BComponent1(1);
		c2 = new BComponent1(2);
		c3 = new BComponent1(3);
	}
}
class BStem extends BRoot {
	BComponent1 c1;
	BComponent1 c2;
	BComponent1 c3;
	BStem(int i)
	{
		super(55);
		System.out.println("Jestem konstruktorem Stem!" + i);
		c1 = new BComponent1(4);
		c2 = new BComponent1(5);
		c3 = new BComponent1(6);
	}
}
/* Output (100%match)
 * Jestem konstruktorem Root!55
 * Jestem konstruktorem Component1!1
 * Jestem konstruktorem Component1!2
 * Jestem konstruktorem Component1!3
 * Jestem konstruktorem Stem!66
 * Jestem konstruktorem Component1!4
 * Jestem konstruktorem Component1!5
 * Jestem konstruktorem Component1!6
 *///:~