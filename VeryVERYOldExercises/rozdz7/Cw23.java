package pl.edu.agh.kis.rozdz7;

/**
 * Ładowanie klas ma miejsce tylko przy pierwszym utworzeniu lub 
 * pierwszym odwołaniu się do zmiennej statycznej
 * @author Szymon Majkut
 *
 */
public class Cw23 {

	static int number = 0;
	public static int print(String s)
	{
		number++;
		System.out.println(s);
		return number;
	}
	
	public static void main(String[] args) {
		
		Loaded1 l1 = new Loaded1(); // Ładowanie wystąpi tylko tutaj
		Loaded1 l2 = new Loaded1();
		
		int a = Loaded2.x2; // Ładowanie przez dostęp do zmiennej statycznej
	}
	/* Output (100%match)
	 * Ładowanie statycznych klasy Loaded1
	 * Tworzę obiekt Loaded1
	 * Tworzę obiekt Loaded1
	 * Ładowanie statycznych klasy Loaded2
	 *///:~

}

class Loaded1 {
	public static int x1 = Cw23.print("Ładowanie statycznych klasy Loaded1");
	Loaded1() { System.out.println("Tworzę obiekt Loaded1"); }
}

class Loaded2 {
	public static int x2 = Cw23.print("Ładowanie statycznych klasy Loaded2");
	Loaded2 () { System.out.println("Tworzę obiekt Loaded2"); }
}