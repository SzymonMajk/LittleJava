package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw13 */

/**
 * Usuwanie obiektu z polem wspólnym
 * @author Szymon Majkut
 *
 */
public class Cw13 {
	
	
	public static void main(String[] args) 
	{
		Shared shared = new Shared();
		Composing[] composing = { new Composing(shared),
				new Composing(shared), new Composing(shared),
				new Composing(shared), new Composing(shared) };
		for(Composing c : composing)
		{
			c.dispose();
		}
	}
	/* Output (33%match)
	 * Tworzenie Shared 0
	 * Tworzenie Composing 0
	 * Tworzenie Composing 1
	 * Tworzenie Composing 2
	 * Tworzenie Composing 3
	 * Tworzenie Composing 4
	 * Usuwanie Composing 0
	 * Usuwanie Composing 1
	 * Usuwanie Composing 2
	 * Usuwanie Composing 3
	 * Usuwanie Composing 4
	 * Usuwanie Shared 0
	 *///:~
}

class Shared {
	boolean readyToDie = true;
	private int refcount = 0;
	private static long counter = 0;
	private final long id = counter++;
	public Shared()
	{
		System.out.println("Tworzenie " + this);
	}
	public void addRef()
	{
		refcount++;
		readyToDie = true;
	}
	protected void dispose()
	{
		if(--refcount == 0)
		{
			System.out.println("Usuwanie " + this);
		}
		if(refcount == 0)
		{
			readyToDie = true;
		}
	}
	protected void finalize()
	{
		if(!readyToDie)
		{
			System.out.println("B³¹d w obiegu");
		}
	}
	public String toString() { return "Shared " + id;}
}

class Composing {
	private Shared shared;
	private static long counter = 0;
	private final long id = counter++;
	public Composing(Shared shared)
	{
		System.out.println("Tworzenie " + this);
		this.shared = shared;
		this.shared.addRef();
	}
	protected void dispose() {
		System.out.println("Usuwanie " + this);
		shared.dispose();
	}
	public String toString() { return "Composing " + id; }
}