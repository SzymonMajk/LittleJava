package pl.edu.agh.kis.reusingclasses;

/**
 * Ładowanie klas ma miejsce tylko przy pierwszym utworzeniu lub 
 * pierwszym odwołaniu się do zmiennej statycznej
 * @author Szymon Majkut
 *
 */
public class Ex23 {

	static int number = 0;
	public static int print(String s)
	{
		number++;
		System.out.println(s);
		return number;
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) {
		
		Loaded1 l1 = new Loaded1(); // Ładowanie wystąpi tylko tutaj
		Loaded1 l2 = new Loaded1();
		
		System.out.println("A teraz spróbujmy dostać się do zmiennej w Loaded2");
		
		int a = Loaded2.x2; // Ładowanie przez dostęp do zmiennej statycznej
	}
}

class Loaded1 {
	public static int x1 = Ex23.print("Ładowanie statycznych klasy Loaded1");
	Loaded1() { System.out.println("Tworzę obiekt Loaded1"); }
}

class Loaded2 {
	public static int x2 = Ex23.print("Ładowanie statycznych klasy Loaded2");
	Loaded2 () { System.out.println("Tworzę obiekt Loaded2"); }
}
/* Output (100%match)
 * Tworzę obiekt Loaded1
 * Tworzę obiekt Loaded1
 * A teraz spróbujmy dostać się do zmiennej w Loaded2
 * Ładowanie statycznych klasy Loaded2
 *///:~