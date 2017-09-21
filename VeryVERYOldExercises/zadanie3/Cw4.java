package pl.edu.agh.kis.zadanie3;

import java.util.Random;

public class Cw4 {
	public static void main(String[] args)
	{
		Random generator = new Random();
		
		Takisposob:
		for(int i = 2; i < 1000; ++i)
		{
			for(int j = 2; j < 1000; ++j)
			{
				if(( i % j == 0) && (i != j))
					continue Takisposob;
			}
			System.out.println(i + " ");
		}
		
	}
}
