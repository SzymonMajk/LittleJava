package pl.edu.agh.kis.moreoperations;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class AddTests {

	@Test
	public void testName() {
		assertEquals("Dodaję!", new Add().getName());
	}

	@Test
	public void testOperation() {
		assertEquals(new BigInteger(""+10), 
				new Add().operation(new BigInteger(""+5),new BigInteger(""+5)));
		assertEquals(new BigInteger(""+0), 
				new Add().operation(new BigInteger(""+0),new BigInteger(""+0)));
		assertEquals(new BigInteger(""+1), 
				new Add().operation(new BigInteger(""+0),new BigInteger(""+1)));
		assertEquals(new BigInteger(""+1), 
				new Add().operation(new BigInteger(""+1),new BigInteger(""+0)));
		assertEquals(new BigInteger(""+-5), 
				new Add().operation(new BigInteger(""+-10),new BigInteger(""+5)));
		assertEquals(new BigInteger(""+0), 
				new Add().operation(new BigInteger(""+1),new BigInteger(""+-1)));
		assertEquals(new BigInteger(""+0), 
				new Add().operation(new BigInteger(""+-2147483647),new BigInteger(""+2147483647)));
		assertEquals(new BigInteger("2147483648"), 
				new Add().operation(new BigInteger(""+2147483647),new BigInteger(""+1)));
	}

}
