package pl.edu.agh.kis.reusingclasses;

/**
 * Przykład leniwej inicjalizacji, obiekt jest tworzony tylko, jeżeli nie
 * został wcześniej zainicjalizowany, w każdym kolejnym przypadku pole klasy Ex1
 * nie będzie tworzone na nowo, dodatkowo korzystamu w tym przypadku z przesłonięcia
 * funkcji toString, w klasie pomocniczej
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex1 {

	private Need n;
	
	private void printNeed()
	{
		if(n == null)
		{
			n = new Need();
		}
		System.out.println(n);
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Ex1 c = new Ex1();
		c.printNeed();
	}
}

/**
 * Klasa pomocnicza do przykładu, która zostanie 
 * @author szymek
 *
 */
class Need {
	
	private String s;
	
	/**
	 * Konsktuktor domyślny, inicjalizujący pole typu string stałym napisem
	 */
	Need()
	{
		s = "Inicjalizuję klasę składową.";
	}
	
	/**
	 * Przesłonięcie metody klasy Object, która zwróci wartość
	 * pola s, zainicjowanego w konstruktorze domyślnym
	 */
	public String toString()
	{
		return s;
	}
}
/* Output 100% match
Inicjalizuję klasę składową.
*///:~