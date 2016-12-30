package pl.edu.agh.kis.moreoperations;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class MultiplyTests {

	@Test
	public void testGetName() {
		assertEquals("Mnożę!", new Multiply().getName());
	}

	@Test
	public void testOperation() {
		assertEquals(new BigInteger(""+25), 
				new Multiply().operation(new BigInteger(""+5),new BigInteger(""+5)));
		assertEquals(new BigInteger(""+0), 
				new Multiply().operation(new BigInteger(""+0),new BigInteger(""+0)));
		assertEquals(new BigInteger(""+0), 
				new Multiply().operation(new BigInteger(""+0),new BigInteger(""+1)));
		assertEquals(new BigInteger(""+0), 
				new Multiply().operation(new BigInteger(""+1),new BigInteger(""+0)));
		assertEquals(new BigInteger(""+-50), 
				new Multiply().operation(new BigInteger(""+-10),new BigInteger(""+5)));
		assertEquals(new BigInteger(""+-1), 
				new Multiply().operation(new BigInteger(""+1),new BigInteger(""+-1)));
		assertEquals(new BigInteger("-4000000000"), 
				new Multiply().operation(new BigInteger("2000000000"),new BigInteger(""+-2)));
		assertEquals(new BigInteger("4000000000"), 
				new Multiply().operation(new BigInteger("2000000000"),new BigInteger(""+2)));
	}

}
