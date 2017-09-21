package pl.edu.agh.kis.zadania2;

import java.util.*;

public class Cw7 {

	static public void main(String[] args)
	{
		Random rand = new Random(47);

		for(int i = 0; i<10; ++i) // żeby sprawdzić losowość
		{
			int randomNumber = rand.nextInt(2);
			
			System.out.println("Dam Ci ostatnią szansę, rzuć monetą: <Rzucasz monetą>");
			if ( randomNumber == 1)
			{
				System.out.println("Wypadła reszka i daruję Ci życie...");
			}
			else
			{
				System.out.println("Wypadł orzeł, to ja wygrywam Twoją śmierć...");
			}
		}
	}
}
