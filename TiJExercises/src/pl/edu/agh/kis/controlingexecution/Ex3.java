package pl.edu.agh.kis.controlingexecution;

import java.util.Random;

/**
 * Pętla nieskończona - przykład
 * od kolejnej
 * @author Szymon
 * @version 1.2
 *
 */
public class Ex3 {
	
	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		Random generator = new Random();
		

		for(;;)
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
/* Output 1% match
-517291784 jest mniejsza od -12517204
-1750486993 jest mniejsza od 213592902
-1186374549 jest mniejsza od 1516761274
725481917 jest mniejsza od 1163006751
1686553258 jest wieksza od -642407329
1675928581 jest wieksza od -1015285881
-404152473 jest wieksza od -1707404189
922093914 jest mniejsza od 1704731075
1585193846 jest wieksza od -1660971763
-1887862784 jest mniejsza od 260864520
1793092548 jest mniejsza od 2021581836
-1485038521 jest mniejsza od -379837959
385213319 jest mniejsza od 1214230683
-800522277 jest mniejsza od 805221230
-175270474 jest mniejsza od 2001207775
1376176730 jest wieksza od -18208906
1206034876 jest wieksza od 220667795
-681378009 jest mniejsza od -587355745
409028455 jest wieksza od -2019368730
992834685 jest wieksza od -1410606220
1497251366 jest wieksza od 1422951843
1095966662 jest wieksza od -200848314
1053805427 jest wieksza od -1606451822
466146093 jest mniejsza od 2130192687
381504654 jest wieksza od -1310801378
*///:~