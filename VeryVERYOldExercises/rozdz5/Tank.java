package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Tank */

//Cw12

/**
 * Klasa z warunkiem zakończenia - sprawdzanie stanu zbiornika
 * @author Szymon Majkut
 *
 */
public class Tank {

	boolean isFull = false;
	
	void fill()
	{
		isFull = false;
	}
	
	void empty()
	{
		isFull = true;
	}
	
	public void finalize()
	{
		if(isFull)
		{
			System.out.println("Najpierw wylej!");
		}
	}
	
	public static void main(String[] args) 
	{
		Tank t1 = new Tank();
		t1.fill();
		Tank t2 = new Tank();	
		t2 = t1;
		Tank t3 = new Tank();
		t3.fill();
		t1 = t3;
		t2 = t3;
		Tank t4 = new Tank();
		Tank t5 = new Tank();
		t4 = t5;
		
		System.gc();
	}
	/* Output (100%match)
	 * 
	 *///:~
}
