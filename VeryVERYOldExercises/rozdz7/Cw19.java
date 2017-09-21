package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw19 */

/**
 * Klasa z pustym finalnym odwołaniem do obiektu
 * @author Szymon Majkut
 *
 */
public class Cw19 {
	
	public static void main(String[] args) 
	{
		EmptyStaticFinalInt n1 = new EmptyStaticFinalInt();
		EmptyStaticFinalInt n2 = new EmptyStaticFinalInt(19);
		
		System.out.println(n1.sV.get());
		System.out.println(n2.sV.get());
		
		SimpleValue verySimple = new SimpleValue(6);
		
		//n1.sV = verySimple; kompilator zabroni
	}
	/* Output (100%match)
	 * 20
	 * 19
	 *///:~
}
class SimpleValue {
	private int j;
	public int get()
	{
		return j;
	}
	public void set(int i)
	{
		j = i;
	}
	SimpleValue(int i) { j = i; }
}
class EmptyStaticFinalInt {
	
	public final SimpleValue sV;
	public int getSV()
	{
		return sV.get();
	}
	public void setSV(int i)
	{
		sV.set(i);
	}
	EmptyStaticFinalInt()
	{
		sV = new SimpleValue(20);
	}
	EmptyStaticFinalInt(int i)
	{
		//int a = sV.get(); kompilator zabroni
		sV = new SimpleValue(i);
	}
	
}
