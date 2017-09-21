package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw10 */

/**
 * Klasa ukazująca wywołanie metody finalize
 * @author szymek
 *
 */
public class Cw10 {

	public void finalize()
	{
		//W sumie nie użyłem nawet metody natywnej...
		System.out.println("Czas posprzątać!");
	}
	public static void main(String[] args) 
	{
		Cw10 c = new Cw10();
		
		System.out.println("Trochę dużo wolnej pamięci... Nie wypisze...");

	}
	/* Output (100%match)
	 * Trochę dużo wolnej pamięci... Nie wypisze...
	 *///:~

}
