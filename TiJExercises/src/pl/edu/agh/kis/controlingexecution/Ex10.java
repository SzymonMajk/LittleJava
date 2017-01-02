package pl.edu.agh.kis.controlingexecution;

/**
 * Klasa poszukująca liczb wampirzych
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex10 {

	/** Punkt wyjścia do klasy i aplikacji
	*  @param args parametry programu, w tym przypadku liczba elementów ciągu
	*/	
	public static void main(String[] args)
	{
		int[] digits = new int[4];
			
		for (int i = 1000; i < 10000; ++i)
		{
				
			digits[0] = i / 1000;
			digits[1] = i % 1000 / 100;
			digits[2] = i % 100 / 10;
			digits[3] = i % 10; /*kolejne cyfry liczby*/
				
		test:
			for(int j = 0; j < 3; ++j)
			{
				for(int k = 0; k < 3; ++k)
				{
					if(k == j)
						continue; // zabezpieczenia, żeby nie wziąć dwa razy tej samej cyfry
						
					for(int l = 0; l < 4; ++l)
					{
						if ((l == k) || ( l == j))
							continue;
							
						for(int m = 0; m < 4; ++m)
						{
							if ((m == k) || ( m == j) || ( l == m))
								continue;
								
							if (i == ( (digits[j]*10+digits[k])*(digits[l]*10+digits[m]) ) )
							{
								System.out.println("" + i + " = " + digits[j] + digits[k] + " * " + digits[l] +digits[m]);
								break test; // żeby nie szukać drugi raz tej samej liczby
							}
								
							break;
								
						}
							
					}
						
				}
					
			}
				
		}
			
	}
}
/* Output 100% match
1260 = 21 * 60
1395 = 93 * 15
1435 = 41 * 35
1530 = 51 * 30
1827 = 21 * 87
2187 = 81 * 27
6880 = 86 * 80
*///:~