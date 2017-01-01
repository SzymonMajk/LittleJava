package pl.edu.agh.kis.everythingisanobject;

/** 
 * Program wypisuje podane dla siebie trzy parametry lub informację,
 * że podano ich za mało
 * @author Szymon
 * @version 1.7a
 */
public class Ex10 {
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	static public void main(String[] args)
	{		
		if(args.length >= 3)
		{
			System.out.println(args[0] + " " + args[1] + " " + args[2]);
		}
		else
		{
			System.out.println("Podano za mało parametrów programu!");
		}
	}
}
/* Output 15% match
jeden dwa trzy
*///:~
