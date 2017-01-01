package pl.edu.agh.kis.everythingisanobject;
//: initialization/Ex16.java
//Demonstration of both constructor
//and ordinary method Ex16.

/**
 * Klasa odpowiedzialna za posadzenie i sprawdzenie drzewa
 * @author Szymon Majkut
 * @version 1.72
 */
class Tree {
	
	private int height;
	
	/**
	 * Konstruktor domyślny, tworzy drzewo o zerowej wysokości
	 */
	Tree() {
		 System.out.println("Planting a seedling");
		 height = 0;
	}
	
	/**
	 * Konstruktor sparametryzowany, tworzy drzewo o ustalonej wysokości
	 * @param initialHeight wartość początkowa wysokości drzewa
	 */
	Tree(int initialHeight) {
		 height = initialHeight;
		 System.out.println("Creating new Tree that is " +
		   height + " feet tall");
	}	
	
	/**
	 * Zwraca informację o wyoskości drzewa na strumień wyjściowy
	 */
	void info() {
		 System.out.println("Tree is " + height + " feet tall");
	}
	
	/**
	 * Przeciążona wersja metody info()
	 * Zwraca informację o wyoskości drzewa na strumień wyjściowy,
	 * dodając komentarz z parametru na początku
	 * @param s komentarz dotyczący drzewa
	 */
	void info(String s) {
		 System.out.println(s + ": Tree is " + height + " feet tall");
	}
}

/**
 * Klasa służy do prezentacji możliwości klasy Tree
 * @author Szymon Majkut
 * @version 1.72
 *
 */
public class Ex16 {
	
	/**
	 * Punkt wyjścia aplikacji, test tworzenia pięciu drzew i wypisania ich
	 * zawartości przez dwie przeciążone metody
	 * @param args
	 */
	public static void main(String[] args) {
		 for(int i = 0; i < 5; i++) {
		     Tree t = new Tree(i);
		     t.info();
		     t.info("overloaded method");
	 }
 
	/**
	 *  Konstrukor
	 */
	 new Tree();
}	
} /* Output:
Creating new Tree that is 0 feet tall
Tree is 0 feet tall
overloaded method: Tree is 0 feet tall
Creating new Tree that is 1 feet tall
Tree is 1 feet tall
overloaded method: Tree is 1 feet tall
Creating new Tree that is 2 feet tall
Tree is 2 feet tall
overloaded method: Tree is 2 feet tall
Creating new Tree that is 3 feet tall
Tree is 3 feet tall
overloaded method: Tree is 3 feet tall
Creating new Tree that is 4 feet tall
Tree is 4 feet tall
overloaded method: Tree is 4 feet tall
Planting a seedling
*///:~
