package pl.edu.agh.kis.zadania2;

//aliasing

public class Cw2 {

	float f;
	
	public static void main(String[] args)
	{
		Cw2 first = new Cw2();
		Cw2 second = new Cw2();
		Cw2 third = new Cw2();
		
		first.f = (float)24.52;
		second = first;
		third.f = first.f;
		
		System.out.println("Najpierw mamy trzy wartosci zmiennych: " 
		+ first.f  + " " + second.f + " " + third.f);
		System.out.println("i teraz zwiekszymy pierwszej" +
			" o jeden, i zaobserwujemy czy zmienily sie ktores inne" );
		
		first.f += 1;
		
		System.out.println(first.f + " " + second.f + " " + third.f 
				+ " " + "Jak widać, "
				+ "nie zmieniła się trzecia zmienna, a to dlatego, że"
				+ " ta druga była skopiowana płytko, a ta trzecia głęboko");
	}
}
