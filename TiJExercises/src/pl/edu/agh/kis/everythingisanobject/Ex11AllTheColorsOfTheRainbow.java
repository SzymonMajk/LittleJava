package pl.edu.agh.kis.everythingisanobject;

/** 
 * Program skompilowany z kawałków
 * @author Szymon
 * @version 1.7a
 */
public class Ex11AllTheColorsOfTheRainbow {

	private int numberOfColors;
	
	public void changeColor(int numberOfNewColor)
	{
		numberOfColors = numberOfNewColor;
	}
	public int getColor()
	{
		return numberOfColors;
	}
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 *  Tutaj do ex14!!!
		*<ol>
		 * <li>Element pierwszy
		 * <li>Element drugi
		 * <li>Element trzeci
		 *</ol>
	 */	
	static public void main(String[] args)
	{
		Ex11AllTheColorsOfTheRainbow all = new Ex11AllTheColorsOfTheRainbow();
		
		all.changeColor(5);
		System.out.println(all.getColor());
	}
}
/* Output 15% match
5
*///:~
