package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw15 */
import pl.edu.agh.kis.rozdz6.*;
/**
 * Sięganie po metodę chronioną spoza pakietu i dziedziczenie
 * klasy spoza pakietu z metodą chronioną
 * @author Szymon Majkut
 *
 */
public class Cw15 {
	
	public static void main(String[] args) 
	{
		ClassToExercise15InChampter7 c = new ClassToExercise15InChampter7();
		//c.protectedMethod(); nie jest widoczna
		ExtendAfterProtected d = new ExtendAfterProtected();
		d.newProtectedMethod();
	}
	/* Output (100%match)
	 * Jestem chroniona!
	 *///:~
}
class ExtendAfterProtected extends ClassToExercise15InChampter7 {
	void newProtectedMethod() { protectedMethod(); }
}
