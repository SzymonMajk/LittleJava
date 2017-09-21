package pl.edu.agh.kis.watki;

public class Test3 {

	public static void main(String[] args)
	{
		C a = new C("jeden");
		C b = new C("dwa");
		
		a.start();
		b.start();
		while(true)
		{
			System.out.println("c");
		}
	}
}

class C extends Thread {
	
	String s;
	
	public void run()
	{
		while(true)
		{
			System.out.print(s);
		}
	}
	C(String s)
	{
		this.s = s;
	}
}