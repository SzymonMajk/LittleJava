package pl.edu.agh.kis.controlingexecution;

/**
 * Sprawdzenie czy wartość zawieta się w przedziale
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex6 {
	
	/**
	 * Funkcja sprawdza czy liczba znajduje się w przedziale
	 * @param testval testowana liczba
	 * @param begin początek przedziału
	 * @param end koniec przedziału
	 * @return
	 */
	static String test(int testval, int begin, int end)
	{
		if(begin > end)
		{
			int switcher = begin;
			begin = end;
			end = switcher;
		}
		if ((testval <= end) && testval >= begin)
			return "Yes";
		return "No";
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	*  @param args tablica ciągów argumentów wywołania
	*/	
	public static void main(String[] args)
	{
		System.out.println(test(5,2,6));
		System.out.println(test(5,2,4));
	}
}
/* Output 100% match
Yes
No
*///:~