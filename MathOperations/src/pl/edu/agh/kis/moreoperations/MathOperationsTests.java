package pl.edu.agh.kis.moreoperations;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class MathOperationsTests {

	@Test
	public void testMain() {
		
		//przygotowujemy strumień wyjścia do testów
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		String[] s1 = {"Add","5","5"};
		MathOperations.main(s1);
		String[] s2 = {"Multiply","5","5"};
		MathOperations.main(s2);
		String[] s3 = {"Power","5","5"};
		MathOperations.main(s3);
		String[] s4 = {"Multiplyy","5","5"};
		MathOperations.main(s4);
		String[] s5 = {"Multiply","5"};
		MathOperations.main(s5);
		String[] s6 = {"Multiply","5g","5h"};
		MathOperations.main(s6);
		String[] s7 = {"Multiply","5","5","anotherText"};
		MathOperations.main(s7);
		
		//sprawdzamy czy to co trafiałoby na wyjście zgadza się z założeniami
		assertEquals("Wykonuje Dodaję!\n10\nWykonuje Mnożę!\n25\n" +
				"Wykonuje Podnoszę do potęgi!\n3125\n" +
				"Podałeś niewłaściwą nazwę operacji\n" +
				"Podałeś niewłaściwą liczbę argumentów!\n" +
				"Liczby zostały podane nieprawidłowo!\n" +
				"Wykonuje Mnożę!\n25\n", outContent.toString());
	}

}
