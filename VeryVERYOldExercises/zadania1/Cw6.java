package pl.edu.agh.kis.zadania1;

public class Cw6 {

	int wielkosc(String s)
	{
		return s.length()*2;
	}
	
	static public void main(String[] args)
	{
		Cw6 cw = new Cw6();
		
		System.out.println("Wielkość słowa 'Tekst' = " + cw.wielkosc("Tekst"));
	}
}
