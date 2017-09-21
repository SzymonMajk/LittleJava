package pl.edu.agh.kis.zadanie3;

public class Cw8 {
	static public void main(String[] args)
	{
		for(int i = 0; i < 10; ++i)
		{
			switch (i)
			{
				case 0: System.out.println("Opcja 0"); break;
				case 1: System.out.println("Opcja 1"); break;
				case 2: System.out.println("Opcja 2"); break;
				default: System.out.println("Opcja def"); break;
			}
		}
		
		for(int i = 0; i < 10; ++i)
		{
			switch (i)
			{
				case 0: System.out.println("Opcja 0"); 
				case 1: System.out.println("Opcja 1"); 
				case 2: System.out.println("Opcja 2"); 
				default: System.out.println("Opcja def"); 
			}
		}
		
		/*Od razu widać różnicę po wynikach, no break usuwamy :d*/
	}
}
