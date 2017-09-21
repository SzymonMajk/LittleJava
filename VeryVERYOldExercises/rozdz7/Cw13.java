package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw13 */

/**
 * Przeciążanie metody dziedziczonej z bazowej zamiast jej przesłonięcia
 * Więc mamy dostęp do wszystkich czterech metod
 * @author Szymon Majkut
 *
 */
public class Cw13 {

	
	public static void main(String[] args) 
	{
		FourthMethod m = new FourthMethod();
		
		m.ant(1);
		m.ant('c');
		m.ant("cc");
		m.ant(1.0f);
	}
	/* Output (100%match)
	 * 1
	 * c
	 * cc
	 * 1.0
	 *///:~
}
class ThreeMethods {
	
	void ant(int i)
	{
		System.out.println(i);
	}
	void ant(char c)
	{
		System.out.println(c);
	}
	void ant(String s)
	{
		System.out.println(s);
	}
}
class FourthMethod extends ThreeMethods {
	void ant(float f)
	{
		System.out.println(f);
	}
}
