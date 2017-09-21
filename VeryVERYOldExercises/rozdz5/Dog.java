package pl.edu.agh.kis.rozdz5;
//Cw5
/**: Object/pl/edu/agh/kis/rozdz5/Dog */

/**
 * Klasa prezentująca możliwość przeciążania metody niestatycznej
 * @author Szymon Majkut
 *
 */
public class Dog {
	
	void bark()
	{
		System.out.println("Zwykły szczek!");
	}
	void bark(int i)
	{
		System.out.println("Szczek do " + i + "-tej potęgi!");		
	}
	void bark(char c)
	{
		System.out.println(c + " szczek!");		
	}

	public static void main(String[] args)
	{
		int a = 5;
		char b = 'c';
		
		Dog emil = new Dog();
		
		emil.bark();
		emil.bark(a);
		emil.bark(b);
	}
	/* Output (100%match)
	 * Zwykły szczek!
	 * Szczek do 5-tej potęgi!
	 * c szczek!
	 *///:~

}
