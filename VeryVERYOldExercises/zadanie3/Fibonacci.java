package pl.edu.agh.kis.zadanie3;
//Cw9
public class Fibonacci {

	public static void main(String[] args)
	{
		int f =  Integer.parseInt(args[0]);
		
		if ( f >= 2)
		{
			System.out.print("1 1");
		}
		
		if (f > 2)
		{
			int prev = 1;
			int prevPrev = 1;
			int result = 2;
			
			for(int i = 3; i <= f; ++i)
			{
				result = prev + prevPrev;
				System.out.print(" " + result);
				prevPrev = prev;
				prev = result;
			}
			
		}
		
	}
}
