package pl.edu.agh.kis;

/**
 * System nie wysyłania logów nigdzie, nadaje się np. do testów, aby
 * nie mieszać logów z testów z logami z prawidłowego działania aplikacji
 * @author Szymon Majkut
 *
 */
public class NullAppender implements Appends {

	/**
	 * Funkcja nie wykonuje efektów ubocznych, jej istnienie jest tylko symboliczne
	 * @param s przysłany log w postaci Stringa, nie używany
	 */
	public void sendNext(String s) {
		//nothing
	}

	/**
	 * Funkcja nie wykonuje efektów ubocznych, jej istnienie jest tylko symboliczne
	 */
	public void clear() {
		//nothing
	}
}
