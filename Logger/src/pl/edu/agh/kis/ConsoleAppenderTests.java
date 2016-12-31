package pl.edu.agh.kis;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

public class ConsoleAppenderTests {

	@Test
	public void testSendNext() {
		ConsoleAppender n = new ConsoleAppender();
		//przygotowujemy strumień wyjścia do testów
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		n.sendNext("First Log");
		
		//sprawdzamy czy to co trafiałoby na wyjście zgadza się z założeniami
		assertEquals("First Log\n", outContent.toString());	

		n.sendNext("Second Log");
		assertEquals("First Log\nSecond Log\n", outContent.toString());	
	}
	@Test
	public void testClear() {
		ConsoleAppender n = new ConsoleAppender();
		//przygotowujemy strumień wyjścia do testów
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		n.clear();
		assertEquals("\n\n", outContent.toString());	

		n.sendNext("Test Log");
		
		//sprawdzamy czy to co trafiałoby na wyjście zgadza się z założeniami
		assertEquals("\n\nTest Log\n", outContent.toString());	
	}
}
