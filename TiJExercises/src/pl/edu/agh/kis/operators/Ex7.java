package pl.edu.agh.kis.operators;

import java.util.*;

/**
 * Symulacja rzutu monetą
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex7 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	static public void main(String[] args)
	{
		Random rand = new Random(47);

		for(int i = 0; i<10; ++i) // żeby sprawdzić losowość
		{
			int randomNumber = rand.nextInt(2);
			
			System.out.println("Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>");
			if ( randomNumber == 1)
			{
				System.out.println("Wypadła reszka i daruję Ci życie...");
			}
			else
			{
				System.out.println("Wypadł orzeł, to ja wygrywam Twoją śmierć...");
			}
		}
	}
}
/* Output 15% match
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadła reszka i daruję Ci życie...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadł orzeł, to ja wygrywam Twoją śmierć...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadła reszka i daruję Ci życie...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadł orzeł, to ja wygrywam Twoją śmierć...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadł orzeł, to ja wygrywam Twoją śmierć...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadła reszka i daruję Ci życie...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadł orzeł, to ja wygrywam Twoją śmierć...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadł orzeł, to ja wygrywam Twoją śmierć...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadła reszka i daruję Ci życie...
Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>
Wypadła reszka i daruję Ci życie...
*///:~