package pl.edu.agh.kis.watki;

public class Test1 {

	public static void main(String[] args)
	{
		A a = new A();
		B b = new B();
		
		a.start();
		b.start();
		while(true)
		{
			System.out.println("c");
		}
	}
}

class A extends Thread {
	public void run()
	{
		while(true)
		{
			System.out.print("a");
		}
	}
}

class B extends Thread {
	public void run()
	{
		while(true)
		{
			System.out.print("b");		
		}
	}
}