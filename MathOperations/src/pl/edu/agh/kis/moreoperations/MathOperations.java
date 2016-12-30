package pl.edu.agh.kis.moreoperations;
/**: Object/pl/edu/agh/kis/smmoreoperations/MathOperations */

import java.math.BigInteger;
import java.util.regex.Pattern;

/** Główna klasa odpowiedzialna za możliwość przeprowadzenia operacji dodawanie,
 * mnożenie oraz podnoszenia do potęgi dla dużych liczb w swoim mainie, dodatkowo
 * będą dziedziczyć po niej konkretne operacje implementujące interfejs Operation,
 * @author Szymon Majkut
 * @version 1.2
 * */
public abstract class MathOperations implements Operation {

	/**
	 * Funkcja obsługująca aplikację, sprawdza czy podano właściwe dane, tworzy
	 * odpowiednie obiekty do przeprowadzenia odpowiednich operacji na dobrych danych
	 * @param args pierwszy parametr odpowiada za nazwę operacji, dwa kolejne
	 * to pierwszy i drugi argument wybranej operacji
	 */
	public static void main(String[] args)
	{
		if(args.length < 3)
		{
			System.out.println("Podałeś niewłaściwą liczbę argumentów!");
			return;
		}
		
		Pattern bigNumberRegex = Pattern.compile("-?\\d+$");
		
		if(!(bigNumberRegex.matcher(args[1]).find() && 
				bigNumberRegex.matcher(args[2]).find()))
		{
			System.out.println("Liczby zostały podane nieprawidłowo!");
			return;
		}
		
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
			System.out.println("Podałeś niewłaściwą nazwę operacji");
		}
		
		
	}/* Output (10%match)
	Dodaję!
	24
	*///:~

}