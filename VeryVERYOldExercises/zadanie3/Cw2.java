package pl.edu.agh.kis.zadanie3;

import java.util.Random;

public class Cw2 {

	
	public static void main(String[] args)
	{
		Random generator = new Random();
		

		for(int i = 0; i < 25; ++i)
		{
			int a, b;
			a = generator.nextInt();
			b = generator.nextInt();
			
			if (a == b)
			{
				System.out.println("Liczby: " + a + " i " + b + " są równe" );
			}
			else if (a > b)
			{
				System.out.println(a + " jest wieksza od " + b);				
			}
			else
			{
				System.out.println(a + " jest mniejsza od " + b );
			}
		}
		
	}
}
