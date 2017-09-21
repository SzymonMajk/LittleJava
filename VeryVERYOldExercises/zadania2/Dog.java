package pl.edu.agh.kis.zadania2;
//cw5
public class Dog {

	String name;
	String says;
	
	public Dog(String n, String s)
	{
		name = n;
		says = s;
	}
	
	public String whoYouAre() 
	{
		String st = "I am " + name + " " + says;
		return st;
	}
	
	public static void main(String[] args)
	{
		Dog spot = new Dog("Spot","Hau!");
		Dog scruffy = new Dog("Scruffy","Wrr!");
		
		System.out.println(spot.whoYouAre());
		System.out.println(scruffy.whoYouAre());
		
		//teraz cw 6
		
		Dog ref;
		ref = spot;
		
		System.out.println();
		
		System.out.print("Spot + ref ==? : ");
		System.out.println(ref == spot);
		System.out.print("Scruffy + ref ==? : ");
		System.out.println(ref == scruffy);
		System.out.print("Spot + Scruffy ==? : ");
		System.out.println(scruffy == spot);
		System.out.println("Spot + ref eq? : " + ref.equals(spot));
		System.out.println("SCruffy + ref eq? : " + ref.equals(scruffy));
		System.out.println("Spot + Scruffy eq? : " + scruffy.equals(spot));
	}
}
