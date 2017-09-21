package pl.edu.agh.kis.zadania2;
//Cw 11,12,13
public class Przesuniecia {

	public static void main(String[] args)
	{
		System.out.println("Cw 11:");
		
		int takio = 0x40000000;
		
		for(int i = 0; i<32; i++)
		{
			System.out.println(Integer.toBinaryString(takio));
			takio = takio >> 1;
		}
		
		System.out.println("Cw 12:");	
		
		takio = 0xffffffff;
		
		takio = takio << 1;
		
		for(int i = 0; i<33; i++)
		{
			System.out.println(Integer.toBinaryString(takio));
			takio = takio >>> 1;
		}
		
		System.out.println("Cw 13:");	
		
		char znak = 'c';
		System.out.println("c: " + Integer.toBinaryString((int)znak));
		
		znak = 'd';
		System.out.println("d: " + Integer.toBinaryString((int)znak));
		
		znak = 'e';
		System.out.println("e: " + Integer.toBinaryString((int)znak));
		
		znak = 'q';
		System.out.println("q: " + Integer.toBinaryString((int)znak));
		
		znak = 'w';
		System.out.println("w: " + Integer.toBinaryString((int)znak));
		
		znak = 'e';
		System.out.println("e: " + Integer.toBinaryString((int)znak));
		
		//zauważmy, że c d e zwiekaszalo zawsze o jeden!!
	}
}
