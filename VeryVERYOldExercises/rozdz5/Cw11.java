package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw11 */

/**
 * Klasa ukazująca wywołanie metody finalize zawsze udane
 * @author szymek
 *
 */
public class Cw11 {

	public void dispose()
	{
		System.out.println("Usuwanko! ... Oczywiście ja tylko sugeruję...");
		System.gc();
	}
	public void finalize()
	{
		System.out.println("Czas posprzątać!");
	}
	public static void main(String[] args) 
	{
		Cw11 c = new Cw11();
		
		System.out.println("Trochę dużo wolnej pamięci... Trzeba zmusić...");
		c.dispose();
		System.out.println("No i się usunął!");
	}
	/* Output (100%match)
	 * Trochę dużo wolnej pamięci... Trzeba zmusić...
	 * Usuwanko! ... Oczywiście ja tylko sugeruję...
	 * No i się usunął!
	 *///:~

}
