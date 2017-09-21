package pl.edu.agh.kis.rozdz7;

/**: Object/pl/edu/agh/kis/rozdz7/Cw1 */

/**
 * Przedstawienie specyficznej kompozycji publicznej
 * @author Szymon Majkut
 *
 */
public class Cw14 {
	
	public static void main(String[] args) 
	{
		 Car car = new Car();
		 car.engine.service();
	}
	/* Output (100%match)
	 * Service from Engine!
	 *///:~
}

//From Car.java to my Cw14
//: c06:Car.java
//Composition with public objects.
//From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
//www.BruceEckel.com. See copyright notice in CopyRight.txt.

class Engine {
public void start() {}
public void rev() {}
public void stop() {}
public void service() { System.out.println("Service from Engine!"); }
}

class Wheel {
public void inflate(int psi) {}
}

class Window {
public void rollup() {}
public void rolldown() {}
}

class Door {
public Window window = new Window();
public void open() {}
public void close() {}
}

class Car {
public Engine engine = new Engine();
public Wheel[] wheel = new Wheel[4];
public Door
 left = new Door(),
 right = new Door(); // 2-door
public Car() {
 for(int i = 0; i < 4; i++)
   wheel[i] = new Wheel();
}

}