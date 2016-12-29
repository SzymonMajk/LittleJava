package pl.edu.agh.kis.moreoperations;
/**: Object/pl/edu/agh/kis/smmoreoperations/MathOperations */

import java.math.BigInteger;

/** Klasa implementuje interfejs odpowiedzialny za 
 * @author Szymon Majkut
 * @version 1.1
 * */
public abstract class MathOperations implements Operation {

	/**
	 * Punkt wyjśćia aplikacji
	 * @param args pierwszy parametr odpowiada za nazwę operacji, dwa kolejne
	 * odpowiadają za wynik
	 */
	public static void main(String[] args)
	{
		BigInteger first = new BigInteger(args[1]);
		BigInteger second = new BigInteger(args[2]);
			
		if (args[0].equals("Multiply") )
		{
			Apply.procces(new Multiply(),first,second);
		}
		else if (args[0].equals("Add"))
		{
	
			Apply.procces(new Add(),first,second);
		}
		else if (args[0].equals("Power") )
		{

			Apply.procces(new Power(),first,second);
		}
		else
		{
			System.out.println("Podałeś złe parametry...");
		}
		
		
	}/* Output (10%match)
	Dodaję!
	24
	*///:~

}