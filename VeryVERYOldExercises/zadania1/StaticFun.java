package pl.edu.agh.kis.zadania1;

// cw 7

public class StaticFun {
	
	static void incr()
	{
		StaticTest.i++;
	}
	
	static public void main(String[] args)
	{
		StaticFun sf = new StaticFun();
		
		System.out.println("Przed inkrementowaniem: " + StaticTest.i);
		
		//odwołanie przez obiek
		
		sf.incr();
		

		System.out.println("Po inkrementowaniu przez obiekt: " + StaticTest.i);
		
		//odwołanie przez klase
		
		StaticFun.incr();
		

		System.out.println("Po inkrementowaniu przez klasę: " + StaticTest.i);
	}
}