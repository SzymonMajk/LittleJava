package pl.edu.agh.kis.logger;

/**
 * Posiada metody potrzebne do zaimplementowania sposobu składowania logów
 * przed wysłaniem ich do docelowego zródła, sprawdzenia ilości logów
 * oraz wyczyszczenia ich, przesyłając do obiektu Append
 * @author Szymon Majkut
 *
 */
public interface LogsCashing {

	/**
	 * Implementująca metoda ma dodać odpowiednio loga
	 * @param s log, który ma zostać dodany w postaci ciągu znaków
	 */
	void addLog(String s);
	
	/**
	 * Ma zwrócić stan zawartości kontenera
	 * @return zwraca wartośc logiczną stanu kontenera
	 */
	boolean isEmpty();
	
	/**
	 * Implementująca funkcja ma zwrócić zawartość kontenera
	 * @return log w postaci ciągu znaków do dalszej obróbki
	 */
	String pollLog();
	
}