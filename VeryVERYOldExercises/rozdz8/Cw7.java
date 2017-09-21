package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw7 */

/**
 * Przes³oniêcie metody toString i jej polimorficzne zachowanie
 * @author Szymon Majkut
 *
 */
public class Cw7 {


	public static void main(String[] args) 
	{
		System.out.println(new NowyInstrument());

		Music3.tune(new NowyInstrument());
	}
	/* Output (33%match)
	 * Nowy Woodwind
	 * Nowy Woodwind.play() MIDDLE_C
	 *///:~
}
