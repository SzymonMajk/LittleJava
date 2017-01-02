package pl.edu.agh.kis.operators;

/**
 * Przedstawienie aliasingu poprzez metodę
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex3 {

	float number;
	
	/**
	 * Funkcja odpowiadzialna za głęboką kopię
	 * @param lF
	 */
	static void foo(Ex3 lF)
	{
		lF.number = (float)13.1313;
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		Ex3 _float = new Ex3();
		
		_float.number = (float)5.152;
		
		System.out.println("Float: " + _float.number);
		
		foo(_float);
		
		System.out.println("Float: " + _float.number);
	}
}
/* Output 15% match
Float: 5.152
Float: 13.1313
*///:~