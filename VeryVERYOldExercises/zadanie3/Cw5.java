package pl.edu.agh.kis.zadanie3;

public class Cw5 {


		static int FIRST = 0xaaaaaaaa;
		static int SECOND = 0x55555555;
		
		static void myToBinaryString(int n)
		{
			char[] tab = new char[32];
			boolean negativSafety = true;
			
			for(int i = 32; i > 0; --i)
			{
				tab[32 - i] = (n & (1 << i)) == 0 ? '0' : '1';
			}
			
			for(int i = 0; i < 32; ++i)
			{

				if(negativSafety && tab[i] == '1')
				{
					negativSafety = false;
				}
				System.out.print(tab[i]);
			}
			if(negativSafety)
			{
				System.out.print('0');
			}
			System.out.println();
		}
		
		public static void main(String[] args)
		{
			System.out.print("F = "); 
			myToBinaryString(FIRST);
			System.out.print("S = "); 
			myToBinaryString(SECOND);
			System.out.print("~F = "); 
			myToBinaryString(~FIRST);
			System.out.print("~S = "); 
			myToBinaryString(~SECOND);
			
			System.out.print("F & S = "); 
			myToBinaryString(FIRST&SECOND);
			System.out.print("F | S = "); 
			myToBinaryString(FIRST|SECOND);
			System.out.print("F ^ S = "); 
			myToBinaryString(FIRST^SECOND);
		}
	
}
