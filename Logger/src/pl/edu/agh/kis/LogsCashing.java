package pl.edu.agh.kis;

/**
 * Posiada metody potrzebne do zaimplementowania sposobu składowania logów
 * przed wysłaniem ich do docelowego zródła, sprawdzenia ilości logów
 * oraz wyczyszczenia ich, przesyłając do obiektu Append
 * @author Szymon Majkut
 *
 */
public interface LogsCashing {

	/**
	 * Implementująca metoda ma umieścić logów w cashu
	 * @param s log, który ma zostać dodany w postaci ciągu znaków
	 */
	void addLog(String s);
	
	/**
	 * Ma zwrócić stan zawartości casha logów
	 * @return zwraca wartośc logiczną stanu kontenera
	 */
	boolean isEmpty();
	
	/**
	 * Implementująca funkcja ma zwrócić zawartość casha
	 * @return log w postaci ciągu znaków do dalszej obróbki
	 */
	String pollLog();
	
}