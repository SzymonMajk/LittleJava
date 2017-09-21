package pl.edu.agh.kis.zadaniainterfejsy;

public class Cw13 implements inter2{
	
	public void met1()
	{
		System.out.println("inter1");
	}
	public void met12()
	{
		System.out.println("inter12");
	}
	public void met11()
	{
		System.out.println("inter11");
	}
	public void met2()
	{
		System.out.println("inter2");
	}
	static public void main(String[] args)
	{
		Cw13 a = new Cw13();
		a.met1();
		a.met12();
		a.met11();

		a.met2();
	}
}
