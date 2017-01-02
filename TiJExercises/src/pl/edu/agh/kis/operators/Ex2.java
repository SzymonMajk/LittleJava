package pl.edu.agh.kis.operators;

/**
 * Przedstawienie aliasingu
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex2 {

	/**
	 * Pole do przedstwaienia aliasingu
	 */
	float f;
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		Ex2 first = new Ex2();
		Ex2 second = new Ex2();
		Ex2 third = new Ex2();
		
		first.f = (float)24.52;
		second = first;
		third.f = first.f;
		
		System.out.println("Najpierw mamy trzy wartosci zmiennych: " 
		+ first.f  + " " + second.f + " " + third.f);
		System.out.println("i teraz zwiekszymy pierwszej" +
			" o jeden, i zaobserwujemy czy zmienily sie ktores inne" );
		
		first.f += 1;
		
		System.out.println(first.f + " " + second.f + " " + third.f 
				+ " " + "Jak widać, "
				+ "nie zmieniła się trzecia zmienna, a to dlatego, że"
				+ " ta druga była skopiowana płytko, a ta trzecia głęboko");
	}
}
/* Output 15% match
Najpierw mamy trzy wartosci zmiennych: 24.52 24.52 24.52
i teraz zwiekszymy pierwszej o jeden, i zaobserwujemy czy zmienily sie ktores inne
25.52 25.52 24.52 Jak widać, nie zmieniła się trzecia zmienna, a to dlatego, że ta druga była skopiowana płytko, a ta trzecia głęboko
*///:~