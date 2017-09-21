package pl.edu.agh.kis.zadania2;

public class Cw11 {

	static void stringCompare(String s1, String s2)
	{
		System.out.print("Popatrzmy na: ");
		System.out.println(s1 + " " + s2);
		
		System.out.print(s1 + " == " + s2 + " -> ");
		System.out.println(s1==s2);
		System.out.println(s1 + " eq " + s2 + " -> " + s1.equals(s2));
		
		System.out.println();
		
	}
	static public void main(String[] args)
	{
		String s1 = "Kot";
		String s2 = "Pies";
		String s3 = "Koza";
		String s4 = s1;
		
		stringCompare(s1,s2);
		stringCompare(s1,s3);
		stringCompare(s3,s2);
		stringCompare(s1,s4);
		stringCompare(s1,"Kot");

	}
}
