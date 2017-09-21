package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw2 */

public class Cw2 {

	public static void main(String[] args)
	{
		Cw2a1 cw2a1 = new Cw2a1();
		Cw2a2 cw2a2 = new Cw2a2();
		
		System.out.println(cw2a1.s);
		System.out.println(cw2a2.s);
		System.out.println("Rozróżnia je moment przeprowadzenia inicjalizacji\n"
				+ "zauważmy, że gdybyśmy chcieli w konstruktorze Cw22 wypisać\n"
				+ "zawartość zmiennej s, wskazywałaby jeszcze na null.");
	}
	/* Output (100%match)
	 * Inicjalizuję Cw21
	 * Inicjalizuję Cw22
	 * Rozróżnia je moment przeprowadzenia inicjalizacji
	 * zauważmy, że gdybyśmy chcieli w konstruktorze Cw22 wypisać
	 * zawartość zmiennej s, wskazywałaby jeszcze na null.
	 *///:~
}
