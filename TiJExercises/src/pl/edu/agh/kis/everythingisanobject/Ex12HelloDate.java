package pl.edu.agh.kis.everythingisanobject;
//cw 12
//: HelloDate.java
import java.util.*;

/** Przykładowy
 *  Wyświetla ciąg znaków i datę
 * @author Bruce Eckel
 * @author MindView.net
 * @version 4.0
 */

public class Ex12HelloDate {
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 *  @throws exceptions nie zgłasza wyjątków
	 */
	public static void main(String[] args)
	{
		System.out.println("Witaj, dzisiaj jest:");
		System.out.println( new Date() );
	}
} /* Output 55% match
Witaj dzisiaj jest:
Thu Oct 13 15:57:04 CEST 2016
*///:~


