package pl.edu.agh.kis.operators;

/**
 * System szesnastkowy, oraz ósemkowy z prymitywem long
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex8 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		long l16 = 0x42af;
		long l8 = 05325;
		
		System.out.println(Long.toBinaryString(l16));
		System.out.println(Long.toBinaryString(l8));
		System.out.println("Inkrementacja:");

		++l16;
		++l8;
		
		System.out.println(Long.toBinaryString(l16));
		System.out.println(Long.toBinaryString(l8));
		System.out.println("l16 = 116 + l8 i l8 = 2 * l8 % l16");
		
		l16 = 116 + l8;
		l8 = 2 * l8 % l16;
		
		System.out.println(Long.toBinaryString(l16));
		System.out.println(Long.toBinaryString(l8));
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