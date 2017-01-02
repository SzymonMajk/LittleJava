package pl.edu.agh.kis.reusingclasses;

import java.util.*;
/**
 * Różnica między statycznym i niestatycznym polem finalnym
 * statyczne nawet losowe zostaje określone przy ładowaniu zmiennych
 * losowych, natomiast wartość niestatycznego jest określana przy tworzeniu
 * każdego nowego obiektu, chociaż obie nie mogą już ulec zmianie
 * @author Szymon Majkut
 *
 */
public class Ex18 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		StaticFinalInt n1 = new StaticFinalInt();
		StaticFinalInt n2 = new StaticFinalInt();
		
		System.out.println(n1);
		System.out.println(n2);
	}
}

/**
 * Klasa ze statycznym polem finalnym, inicjalizowanym liczbą
 * losową
 * @author Szymon
 *
 */
class StaticFinalInt {
	
	private static Random rand = new Random(37);
	static final int LOOK_LIKE_C_STYLE = rand.nextInt(20);
	final int simpleValue = rand.nextInt(20);
	public String toString()
	{
		return "" + LOOK_LIKE_C_STYLE + " " + simpleValue;
	}
}
/* Output (100%match)
 * 5 3
 * 5 10
 *///:~