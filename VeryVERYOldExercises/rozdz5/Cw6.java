package pl.edu.agh.kis.rozdz5;
/**: Object/pl/edu/agh/kis/rozdz5/Cw6 */

/**
 * Klasa prezentująca możliwość przeciążania metody tymi samymi
 * argumentami, ale w innej kolejności
 * @author Szymon Majkut
 *
 */
public class Cw6 {
	
	void bark(int i, char c)
	{
		System.out.println(c + " szczek do " + i + "-tej potęgi! 1!");		
	}
	void bark(char c, int i)
	{
		System.out.println(c + " szczek do " + i + "-tej potęgi! 2!");		
	}

	public static void main(String[] args)
	{
		int a = 5;
		char b = 'c';
		Cw6 c = new Cw6();
		
		c.bark(a,b);
		c.bark(b,a);
	}
	/* Output (100%match)
	 * c szczek do 5-tej potęgi! 1!
	 * c szczek do 5-tej potęgi! 2!
	 *///:~

}
