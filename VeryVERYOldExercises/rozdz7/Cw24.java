package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw24 */

/**
 * Ładowanie klas - rozbudowanie przykładu o żuka
 * @author Szymon Majkut
 *
 */
public class Cw24 {

	static int number = 0;
	public static int print(String s)
	{
		number++;
		System.out.println(s);
		return number;
	}
	
	public static void main(String[] args) 
	{
		BigBeetle b = new BigBeetle();
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
}

class BigBeetle extends Beetle {
	private int m = 8;
	public BigBeetle() {
		System.out.println("Konstruktor BigBeetle!");
	   System.out.println("m = " + m);
	   System.out.println("j = " + j);
	 }
	 private static int x3 = Cw24.print("Init x3");
}

class Insect {
	  private int i = 9;
	  protected int j;
	  Insect() {
		  System.out.println("Konstruktor Insect!");
	    System.out.println("i = " + i + ", j = " + j);
	    j = 39;
	  }
	  private static int x1 = Cw24.print("Init x1");
}

class Beetle extends Insect {
	private int k = 8;
	public Beetle() {
		System.out.println("Konstruktor Beetle!");
	   System.out.println("k = " + k);
	   System.out.println("j = " + j);
	 }
	 private static int x2 = Cw24.print("Init x2");
} 
