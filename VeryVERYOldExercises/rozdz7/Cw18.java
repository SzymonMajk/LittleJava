package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw18 */
import java.util.*;
/**
 * Różnica między statycznym i niestatycznym polem finalnym
 * @author Szymon Majkut
 *
 */
public class Cw18 {
	
	public static void main(String[] args) 
	{
		StaticFinalInt n1 = new StaticFinalInt();
		StaticFinalInt n2 = new StaticFinalInt();
		
		System.out.println(n1);
		System.out.println(n2);
	}
	/* Output (100%match)
	 * 5 3
	 * 5 10
	 *///:~
}
class StaticFinalInt {
	
	private static Random rand = new Random(37);
	static final int LOOK_LIKE_C_STYLE = rand.nextInt(20);
	final int simpleValue = rand.nextInt(20);
	public String toString()
	{
		return "" + LOOK_LIKE_C_STYLE + " " + simpleValue;
	}
}
