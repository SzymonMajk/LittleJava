package pl.edu.agh.kis.reusingclasses;

/**
 * Przeciążanie metody dziedziczonej z bazowej zamiast jej przesłonięcia
 * Więc mamy dostęp do wszystkich czterech metod
 * @author Szymon Majkut
 *
 */
public class Ex13 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */
	public static void main(String[] args) 
	{
		FourthMethod m = new FourthMethod();
		
		m.ant(1);
		m.ant('c');
		m.ant("cc");
		m.ant(1.0f);
	}
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
/* Output (100%match)
 * 1
 * c
 * cc
 * 1.0
 *///:~