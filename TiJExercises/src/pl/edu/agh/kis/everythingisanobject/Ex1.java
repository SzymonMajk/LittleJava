package pl.edu.agh.kis.everythingisanobject;

/** 
 *  Zadanie przedstawiające automatyczną inicjalizajcę prymitywów
 * @author Szymon
 * @version 1.7a
 */
public class Ex1 
{
	int var1;
	char var2;
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args)
	{
		Ex1 cwiczenie = new Ex1();
		
		System.out.println("Niezainicjowany int = " + cwiczenie.var1);
		System.out.println("Niezainicjowany char = " + cwiczenie.var2);
	}

}
/* Output 100% match
Niezainicjowany int = 0
Niezainicjowany char = 
*///:~