package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw6 */

/**
 * W konstruktorze pochodnej musimy podawać konstruktor bazowej
 * w pierwszej instrukcji, a jeżeli są tylko sparametryzowane,
 * to musimy użyć tych sparametryzowanych, czyli podać argumenty
 * @author Szymon Majkut
 *
 */
public class Cw6 {
	
	public static void main(String[] args) 
	{
		Chess x = new Chess();
	}
	/* Output (100%match)
	 *///:~
}

//: c06:Chess.java
//Inheritance, constructors and arguments.
//From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
//www.BruceEckel.com. See copyright notice in CopyRight.txt.

class Game {
Game(int i) {
 System.out.println("Game constructor");
}
}

class BoardGame extends Game {
BoardGame(int i) {
 //super(i); teraz kompilator stwierdzi że nie zna konstruktora Game
 System.out.println("BoardGame constructor");
}
}

class Chess extends BoardGame {

Chess() {

 System.out.println("Chess constructor");
 super(11); // czyli jednak musi być pierwszą instrukcją
}


} ///:~
