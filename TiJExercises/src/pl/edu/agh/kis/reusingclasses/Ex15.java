package pl.edu.agh.kis.reusingclasses;

import pl.edu.agh.kis.helppackage.*;
/**
 * Sięganie po metodę chronioną spoza pakietu i dziedziczenie
 * klasy spoza pakietu z metodą chronioną
 * @author Szymon Majkut
 *
 */
public class Ex15 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		ClassToExercise15InChampter7 c = new ClassToExercise15InChampter7();
		//c.protectedMethod(); nie jest widoczna
		ExtendAfterProtected d = new ExtendAfterProtected();
		d.newProtectedMethod();
	}
}
class ExtendAfterProtected extends ClassToExercise15InChampter7 {
	void newProtectedMethod() { protectedMethod(); }
}
/* Output (100%match)
 * Jestem chroniona!
 *///:~