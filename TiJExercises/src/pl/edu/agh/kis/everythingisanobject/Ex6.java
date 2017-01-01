package pl.edu.agh.kis.everythingisanobject;

/** 
 *  Druga wersja programu skompilowanego z kawałków
 * @author Szymon
 * @version 1.7a
 */
public class Ex6 {

	/**
	 * Funkcja zwiększa dwukrotną wartość otrzymanego argumentu
	 * @param s otrzymany tekst
	 * @return powiększona dwukrotnie długość tekstu
	 */
	int storage(String s)
	{
		return s.length()*2;
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	static public void main(String[] args)
	{
		Ex6 cw = new Ex6();
		
		System.out.println("Wielkość słowa 'Tekst' = " + cw.storage("Tekst"));
	}
}
/* Output 100% match
Wielkość słowa 'Tekst' = 10
*///:~