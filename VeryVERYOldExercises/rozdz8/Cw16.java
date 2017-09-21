package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw16 */

/**
 * Metoda zmiany stanów przy kompozycji przez nadstawianie nowych obiektów
 * @author Szymon Majkut
 *
 */
public class Cw16 {
	
	
	public static void main(String[] args) 
	{
		Starship s = new Starship();
		s.getStatus();
		s.change();
		s.getStatus();
	}
	/* Output (33%match)
	 * Status1
	 * Status2
	 *///:~
}

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