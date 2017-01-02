package pl.edu.agh.kis.controlingexecution;

/**
 * Prosty przykłady wykorzytania instrukcji switch w dwóch wariantach,
 * pierwszy wybiera tylko konkretną instrukcję i kończy switcha przy pomocy
 * break, drugi nie ma break, więc wykona wszystkie przypadki poniżej jeszcze
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex8 {
	
	/** Punkt wyjścia do klasy i aplikacji
	*  @param args tablica ciągów argumentów wywołania
	*/	
	static public void main(String[] args)
	{
		for(int i = 0; i < 10; ++i)
		{
			switch (i)
			{
				case 0: System.out.println("Opcja 0"); break;
				case 1: System.out.println("Opcja 1"); break;
				case 2: System.out.println("Opcja 2"); break;
				default: System.out.println("Opcja def"); break;
			}
		}
		
		for(int i = 0; i < 10; ++i)
		{
			switch (i)
			{
				case 0: System.out.println("Opcja 0"); 
				case 1: System.out.println("Opcja 1"); 
				case 2: System.out.println("Opcja 2"); 
				default: System.out.println("Opcja def"); 
			}
		}
		
	}
}
/* Output 100% match
Opcja 0
Opcja 1
Opcja 2
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
Opcja 0
Opcja 1
Opcja 2
Opcja def
Opcja 1
Opcja 2
Opcja def
Opcja 2
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
Opcja def
*///:~