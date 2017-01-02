package pl.edu.agh.kis.operators;

/**
 * Przykład użycia konstruktora sparameryzowanego
 * @author Szymon
 * @version 1.6.3
 *
 */
public class Ex5 {

	/** Punkt wyjścia do klasy i aplikacji
	 *  @param args tablica ciągów argumentów wywołania
	 */	
	public static void main(String[] args)
	{
		Dog spot = new Dog("Spot","Hau!");
		Dog scruffy = new Dog("Scruffy","Wrr!");
		
		System.out.println(spot.whoYouAre());
		System.out.println(scruffy.whoYouAre());
	}
}

/**
 * Klasa pomocnicza, będąca w stanie wypisać zawartość dwóch pól,
 * przypisanych w konstruktorze sparametryzowanym
 * @author szymek
 *
 */
class Dog {

	private String name;
	private String says;
	
	/**
	 * Konstruktor sparametryzowany, odpowiedziany za
	 * ustalenie wartości nazwy psa oraz tego co szczeka
	 * @param n ciąg znaków z imieiniem psa
	 * @param s ciąg znaków z tekstem szczekanym przez psa
	 */
	public Dog(String n, String s)
	{
		name = n;
		says = s;
	}
	
	/**
	 * Funkcja dzięki której piers może się przedstawić
	 * @return Zwrot grzeczenościowy z zawartościami pól klasy
	 */
	public String whoYouAre() 
	{
		String st = "I am " + name + " " + says;
		return st;
	}
}
/* Output 15% match
I am Spot Hau!
I am Scruffy Wrr!
*///:~