package pl.edu.agh.kis.everythingisanobject;

/** 
 *  Utworzenie kompilowalnego programu z kawałków
 * @author Szymon
 * @version 1.7a
 */
public class Ex4DataOnly {
	int i;
	double d;
	boolean b;
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	static public void main(String[] args)
	{
		Ex4DataOnly td = new Ex4DataOnly();
		if ( !td.b)
		System.out.println("Int = " + td.i + " Doubl = " + td.d);
	}

}
/* Output 100% match
Int = 0 Doubl = 0.0
*///:~