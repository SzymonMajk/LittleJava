package pl.edu.agh.kis.zadania2;

public class Cw8 {

	public static void main(String[] args)
	{
		long l16 = 0x42af;
		long l8 = 05325;
		
		l16++;
		l8--;
		
		l16 = 116 + l8;
		l8 = 2 * l8 % l16;
		
		System.out.println(Long.toBinaryString(l16));
		System.out.println(Long.toBinaryString(l8));
	}
}
