package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw4 */

/**
 * Klasa z konstruktorem bezargumentowym wypisującym komunikat
 * i konstruktorem sparametryzowanym parametrem typu string, który
 * ma zostać w nim wypisany
 * @author Szymon Majkut
 */
public class Cw4 {

	Cw4()
	{
		System.out.println("Prosty komunikat");
	}
	Cw4(String s)
	{
		System.out.println("Prosty komunikat" + " " + s);
	}
	
	public static void main(String[] args)
	{
		String s = "Zwykły string";
		Cw4 c = new Cw4("Zwykły string");
	}
	/* Output (100%match)
	 * Prosty komunikat Zwykły string
	 *///:~
}