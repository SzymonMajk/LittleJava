package pl.edu.agh.kis.zadanie3;

public class Cw6 {
	static int test(int testval, int begin, int end)
	{
		if ((testval <= end) && testval >= begin)
			return 1;
		return 0;
	}
	
	public static void main(String[] args)
	{
		System.out.println(test(5,2,6));
		System.out.println(test(5,2,4));
	}
}
