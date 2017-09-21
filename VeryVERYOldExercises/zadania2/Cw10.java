package pl.edu.agh.kis.zadania2;

public class Cw10 {

	static int FIRST = 0xaaaaaaaa;
	static int SECOND = 0x55555555;
	
	public static void main(String[] args)
	{
		System.out.println("F = " + Integer.toBinaryString(FIRST));
		System.out.println("S = " + Integer.toBinaryString(SECOND));
		System.out.println("~F = " + Integer.toBinaryString(~FIRST));
		System.out.println("~S = " + Integer.toBinaryString(~SECOND));
		
		System.out.println("F & S = " + Integer.toBinaryString(FIRST&SECOND));
		System.out.println("F | S = " + Integer.toBinaryString(FIRST|SECOND));
		System.out.println("F ^ S = " + Integer.toBinaryString(FIRST^SECOND));
	}
}
