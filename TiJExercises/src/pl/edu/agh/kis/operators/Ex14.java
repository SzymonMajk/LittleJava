package pl.edu.agh.kis.operators;

/**
 * Operator przesunięcia
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex14 {

	/**
	 * Porównanie argumentów przy pomocy operatora == oraz metody equals()
	 * @param s1 pierwszy ciąg znaków do porównania
	 * @param s2 drugi ciąg znaków do porównania
	 */
	static void stringCompare(String s1, String s2)
	{
		System.out.print("Popatrzmy na: ");
		System.out.println(s1 + " " + s2);
		
		System.out.print(s1 + " == " + s2 + " -> ");
		System.out.println(s1==s2);
		System.out.println(s1 + " eq " + s2 + " -> " + s1.equals(s2));
		
		System.out.println();
		
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	static public void main(String[] args)
	{
		String s1 = "Kot";
		String s2 = "Pies";
		String s3 = "Koza";
		String s4 = s1;
		
		stringCompare(s1,s2);
		stringCompare(s1,s3);
		stringCompare(s3,s2);
		stringCompare(s1,s4);
		stringCompare(s1,"Kot");

	}
}
/* Output 15% match
Popatrzmy na: Kot Pies
Kot == Pies -> false
Kot eq Pies -> false

Popatrzmy na: Kot Koza
Kot == Koza -> false
Kot eq Koza -> false

Popatrzmy na: Koza Pies
Koza == Pies -> false
Koza eq Pies -> false

Popatrzmy na: Kot Kot
Kot == Kot -> true
Kot eq Kot -> true

Popatrzmy na: Kot Kot
Kot == Kot -> true
Kot eq Kot -> true
*///:~