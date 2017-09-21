package pl.edu.agh.kis.logger;

/**
 * Implementacja klasy odpowiedzialnej za wysyłanie logów na konsolę
 * @author Szymon Majkut
 *
 */
public class ConsoleAppender implements Appends {

	/**
	 * Funkcja wysyła loga na konsolę
	 */
	public void sendNext(String s) {
		System.out.println(s);
	}
	
	/**
	 * Oddzielamy poprzednią treść od logów które będziemy wypisywać
	 */
	public void clear()
	{
		System.out.print("\n\n");
	}

}