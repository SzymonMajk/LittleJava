package pl.edu.agh.kis.polymorphism;

/**
 * Rodent z obiektem wspólnym Shared
 * @author Szymon
 *
 */
public class Ex14 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args) 
	{
		Shared s = new Shared();
		Rodent3[] composing = { new Hamster3(s),
				new Rodent3(s), new Hamster3(s) };
		for(Rodent3 c : composing)
		{
			c.dispose();
		}
	}
}

class Rodent3 {
	private Shared shared;
	void gnaw() { System.out.println("Gryzoń gryzie!"); }
	Rodent3(Shared s) { 
		System.out.println("Tworzę Rodent!"); 
		shared = s;
		shared.addRef();
		}
	protected void dispose()
	{
		System.out.println("Usuwanie Rodent");
		shared.dispose();
	}
}

class Mouse3 extends Rodent3 {
	private Shared shared;
	int j = 3;
	@Override void gnaw() { System.out.println("Mysz gryzie!"); }
	Mouse3(Shared s) { 
		super(s);
		System.out.println("Tworzę Mouse!"); 
		shared = s;
		shared.addRef();
	}
	protected void dispose()
	{
		System.out.println("Usuwanie Mouse");
		shared.dispose();
		super.dispose();
	}
}

class Hamster3 extends Rodent3  {
	private Shared shared;
	int k = 4;
	@Override void gnaw() { System.out.println("Chomik gryzie!"); }
	Hamster3(Shared s) { 
		super(s);
		System.out.println("Tworzę Mouse!"); 
		shared = s;
		shared.addRef();
	}
	protected void dispose()
	{
		System.out.println("Usuwanie Hamster");
		shared.dispose();
		super.dispose();
	}
}
/* Output (33%match)
 * Tworzenie Shared 0
 * Tworzę Rodent!
 * Tworzę Mouse!
 * Tworzę Rodent!
 * Tworzę Rodent!
 * Tworzę Mouse!
 * Usuwanie Hamster
 * Usuwanie Rodent
 * Usuwanie Rodent
 * Usuwanie Hamster
 * Usuwanie Rodent
 * Usuwanie Shared 0
 *///:~