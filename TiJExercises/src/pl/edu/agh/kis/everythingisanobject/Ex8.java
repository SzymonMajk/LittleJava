package pl.edu.agh.kis.everythingisanobject;

/** 
 * Program prezentujący własność współdzielenia przez obiekty klasy
 * jej pól statycznych
 * @author Szymon
 * @version 1.7a
 */
public class Ex8 {

	 static public int testInt = 5;
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	static public void main(String[] args)
	{
		Ex8 first = new Ex8();
		Ex8 second = new Ex8();
		
		System.out.println("Wypisuje dla obiektu a1: " +
		first.testInt);
		System.out.println("Poprzez obiekt a1 zwiększam wartość pola statycznego: ");
		++first.testInt;
		System.out.println("Wypisuje dla obiektu a2: " +
		second.testInt);
	}
}
/* Output 100% match
Wypisuje dla obiektu a1: 5
Poprzez obiekt a1 zwiększam wartość pola statycznego: 
Wypisuje dla obiektu a2: 6
*///:~