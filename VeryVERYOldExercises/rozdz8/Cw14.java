package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw14 */

/**
 * Rodent z obiektem wsp�lnym Shared
 * @author Szymon Majkut
 *
 */
public class Cw14 {
	
	
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
	/* Output (33%match)
	 * Tworzenie Shared 0
	 * Tworz� Rodent!
	 * Tworz� Mouse!
	 * Tworz� Rodent!
	 * Tworz� Rodent!
	 * Tworz� Mouse!
	 * Usuwanie Hamster
	 * Usuwanie Rodent
	 * Usuwanie Rodent
	 * Usuwanie Hamster
	 * Usuwanie Rodent
	 * Usuwanie Shared 0
	 *///:~
}

class Rodent3 {
	private Shared shared;
	void gnaw() { System.out.println("Gryzo� gryzie!"); }
	Rodent3(Shared s) { 
		System.out.println("Tworz� Rodent!"); 
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
		System.out.println("Tworz� Mouse!"); 
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
		System.out.println("Tworz� Mouse!"); 
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