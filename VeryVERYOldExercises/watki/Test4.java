package pl.edu.agh.kis.watki;

public class Test4 {

	public static void main(String[] args)
	{
		D a = new D("jeden");
		D b = new D("dwa");
		
		a.start();
		b.start();
		while(true)
		{
			System.out.println("c");
		}
	}
}

class D extends Thread {
	
	String s;
	
	public void run()
	{
		while(true)
		{
			System.out.print(s);
		}
	}
	D(String s)
	{
		this.s = s;
	}
}