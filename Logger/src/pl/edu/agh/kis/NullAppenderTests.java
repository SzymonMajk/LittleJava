package pl.edu.agh.kis;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;


public class NullAppenderTests {

	@Test
	public void testSendNext() {
		NullAppender n = new NullAppender();
		//przygotowujemy strumień wyjścia do testów
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		n.sendNext("Przykład");
		
		//sprawdzamy czy to co trafiałoby na wyjście zgadza się z założeniami
		assertEquals("", outContent.toString());
	}

	@Test
	public void testClear() {
		NullAppender n = new NullAppender();
		//przygotowujemy strumień wyjścia do testów
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		n.clear();
		
		//sprawdzamy czy to co trafiałoby na wyjście zgadza się z założeniami
		assertEquals("", outContent.toString());
		
		System.out.print("DoNotClear");
		
		n.clear();
		
		//sprawdzamy czy to co trafiałoby na wyjście zgadza się z założeniami
		assertEquals("DoNotClear", outContent.toString());
	}

}
