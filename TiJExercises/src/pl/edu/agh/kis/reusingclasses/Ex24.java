package pl.edu.agh.kis.reusingclasses;

/**
 * Ładowanie klas - rozbudowanie przykładu o żuka
 * Najpierw ładowane są zmienne statyczne, później klasa
 * Insect, przy czym parametr j = 0, ponieważ
 * jego inijalizacja liczbą 39 następuje po wypisaniu w konstruktorze
 * następnie analogicznie tworzony jest obiekt Beetle a na samym końcu
 * wykonuje się ciało konstruktora BigBettle, dodatkowo zauważamy,
 * że inicjalizacja przy deklaracji jest wykonywana przed ciałem
 * konstruktora
 * @author Szymon Majkut
 *
 */
public class Ex24 {

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
	public static void main(String[] args) 
	{
		BigBeetle b = new BigBeetle();
	}
}

class BigBeetle extends Beetle {
	private int m = 8;
	public BigBeetle() {
		System.out.println("Konstruktor BigBeetle!");
	   System.out.println("m = " + m);
	   System.out.println("j = " + j);
	 }
	 private static int x3 = Ex24.print("Init x3");
}

class Insect {
	  private int i = 9;
	  protected int j;
	  Insect() {
		  System.out.println("Konstruktor Insect!");
	    System.out.println("i = " + i + ", j = " + j);
	    j = 39;
	  }
	  private static int x1 = Ex24.print("Init x1");
}

class Beetle extends Insect {
	private int k = 8;
	public Beetle() {
		System.out.println("Konstruktor Beetle!");
	   System.out.println("k = " + k);
	   System.out.println("j = " + j);
	 }
	 private static int x2 = Ex24.print("Init x2");
} 
/* Output (100%match)
 * Init x1
 * Init x2
 * Init x3
 * Konstruktor Insect!
 * i = 9, j = 0
 * Konstruktor Beetle!
 * k = 8
 * j = 39
 * Konstruktor BigBeetle!
 * m = 8
 * j = 39
 *///:~