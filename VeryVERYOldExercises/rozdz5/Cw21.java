package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw21 */

/**
 * Przegląd wartości wyliczenia Banknotes
 * @author Szymon Majkut
 *
 */
public class Cw21 {
	
	
	public static void main(String...args)
	{
		for(Banknotes i : Banknotes.values())
		{
			System.out.println(i.name() + " miejsce: " + i.ordinal());
		}
	}
	/* Output (100%match)
	 * DYCHA miejsce: 0
	 * DWIEDYCHY miejsce: 1
	 * PIECDYCH miejsce: 2
	 * STOWA miejsce: 3
	 * DWIESTOWY miejsce: 4
	 * PIECSTOW miejsce: 5 
	 *///:~
}