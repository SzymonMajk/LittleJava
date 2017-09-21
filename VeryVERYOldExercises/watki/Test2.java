package pl.edu.agh.kis.watki;

public class Test2 {

	public static void main(String[] args)
	{
		A2 a = new A2();
		B2 b = new B2();
		
		new Thread(a).start();
		new Thread(b).start();
		while(true)
		{
			System.out.println("c");
		}
	}
}

class A2 implements Runnable {
	public void run()
	{
		while(true)
		{
			System.out.print("a");
		}
	}
}

class B2 implements Runnable {
	public void run()
	{
		while(true)
		{
			System.out.print("b");		
		}
	}
}