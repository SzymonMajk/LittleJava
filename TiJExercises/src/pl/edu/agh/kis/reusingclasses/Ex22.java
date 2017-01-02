package pl.edu.agh.kis.reusingclasses;

/**
 * Nieudana próba dziedziczenia klasy finalnej
 * @author Szymon Majkut
 *
 */
public class Ex22 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) {
		
	}
}

final class FinalClass {
	void f() { System.out.println("Metoda f() klasy finalnej"); }
}
//Kompilator poinformuje o błędzie
/*
class TryToExtendFinalClass extends FinalClass {
	//Kompilator poinformuje o błędzie
	}*/
/* Output (100%match)
 *///:~