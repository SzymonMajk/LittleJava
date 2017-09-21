package pl.edu.agh.kis.rozdz2;

/**
 * Java domyślnie inicjalizuje zmienne wartościami nullo-podobnymi
 * @author Szymon Majkut
 *
 */
public class Cw1 {

	public int intNotInit;
	public char charNotInit;
	
	/**
	 * @param args - argumenty programu, nie używane
	 */
	public static void main(String[] args) {
		
		Cw1 c = new Cw1();
		System.out.print(c.intNotInit + " and " + c.charNotInit);
	}
	/* Output (100%match)
	 * 0 and 
	 *///:~
//	 czyli char inicjalizowany znakiem spacji

}
