package pl.edu.agh.kis.polymorphism;

/**
 * Metoda zmiany stanów przy kompozycji przez nadstawianie nowych obiektów
 * @author Szymon
 *
 */
public class Ex16 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Starship s = new Starship();
		s.getStatus();
		s.change();
		s.getStatus();
	}
}
/**
 * Proste klasy umożliwiającej zmianę stanu błędu statku kosmicznego
 * @author szymek
 *
 */
class AlertStatus {
	private int status = 0;
	public int getStatus() { return status; }
	AlertStatus(int i) { status = i; System.out.println("Status" + status);}
	AlertStatus() { status = 1; System.out.println("Status" + status);}
}

class Starship {
	AlertStatus al = new AlertStatus();
	public void change() { al = new AlertStatus(2); }
	public void getStatus() { al.getStatus(); }
}
/* Output (100%match)
 * Status1
 * Status2
 *///:~