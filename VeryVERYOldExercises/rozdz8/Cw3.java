package pl.edu.agh.kis.rozdz8;

/**: Object/pl/edu/agh/kis/rozdz7/Cw3 */

/**
 * Zabawa z przes³anianiem
 * @author Szymon Majkut
 *
 */
public class Cw3 {

	public static RandomShapeGenerator gen = new RandomShapeGenerator();
	
	public static void main(String[] args) 
	{
		Shape[] s = new Shape[9];
		for(int i = 0; i < s.length; ++i)
		{
			s[i] = gen.next();
		}
		for(Shape ss : s)
		{
			ss.nieprzeslaniana();
			ss.przeslonietaWCircle();
			ss.przeslonietaWszedzie();
		}
	}
	/* Output (33%match)
	 * Triangle mnie przes³ania! nie tylko ona!
	 * Triangle mnie przes³ania! nie tylko ona!
	 * Square mnie przes³ania! nie tylko ona!
	 * Triangle mnie przes³ania! nie tylko ona!
	 * Square mnie przes³ania! nie tylko ona!
	 * Triangle mnie przes³ania! nie tylko ona!
	 * Square mnie przes³ania! nie tylko ona!
	 * Triangle mnie przes³ania! nie tylko ona!
	 * Circle mnie przes³ania!
	 * Circle mnie przes³ania! nie tylko ona!
	 *///:~
}