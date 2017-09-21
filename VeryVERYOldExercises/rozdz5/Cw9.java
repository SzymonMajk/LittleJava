package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw9 */

/**
 * Klasa wywołująca sparametryzowany konstruktor w sparametryzowanym
 * konstruktorze
 * @author Szymon Majkut
 *
 */
public class Cw9 {

	Cw9(int i)
	{
		System.out.println("Ja się wykonam! " + i);
	}
	Cw9(char c)
	{
		this(5);
		System.out.println("A teraz czas na mnie! " + c);
	}
	public static void main(String[] args) 
	{
		char a = 'c';
		
		Cw9 c = new Cw9(a);
	}
	/* Output (100%match)
	 * Ja się wykonam! 5
	 * A teraz czas na mnie! c
	 *///:~

}
