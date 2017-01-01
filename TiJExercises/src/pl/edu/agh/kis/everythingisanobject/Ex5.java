package pl.edu.agh.kis.everythingisanobject;

/** 
 *  Druga wersja programu skompilowanego z kawałków
 * @author Szymon
 * @version 1.7a
 */
public class Ex5 {
	int i;
	double d;
	boolean b;
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	static public void main(String[] args)
	{
		Ex5 td = new Ex5();
		
		td.i = 50;
		td.d = 525.252;
		td.b = true;
		
		if(td.b);
		System.out.println("Int = " + td.i + " Doubl = " + td.d);
	}

}
/* Output 100% match
Int = 50 Doubl = 525.252
*///:~