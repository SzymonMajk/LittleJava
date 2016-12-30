package pl.edu.agh.kis.moreoperations;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class PowerTests {

	@Test
	public void testGetName() {
		assertEquals("Podnoszę do potęgi!", new Power().getName());
	}

	@Test
	public void testOperation() {
		assertEquals(new BigInteger(""+8), 
				new Power().operation(new BigInteger(""+2),new BigInteger(""+3)));
		assertEquals(new BigInteger(""+0), 
				new Power().operation(new BigInteger(""+0),new BigInteger(""+1)));
		assertEquals(new BigInteger(""+1), 
				new Power().operation(new BigInteger(""+0),new BigInteger(""+0)));
		assertEquals(new BigInteger(""+1), 
				new Power().operation(new BigInteger(""+1),new BigInteger(""+0)));
		assertEquals(new BigInteger(""+1), 
				new Power().operation(new BigInteger(""+-1),new BigInteger(""+2)));
		assertEquals(new BigInteger(""+-1), 
				new Power().operation(new BigInteger(""+-1),new BigInteger(""+3)));
		assertEquals(new BigInteger("1073741824"), 
				new Power().operation(new BigInteger(""+2),new BigInteger(""+30)));
		//Kilka przypadków niestandardowych
		assertEquals(new BigInteger(""+0), 
				new Power().operation(new BigInteger(""+-1),new BigInteger(""+-3)));
		assertEquals(new BigInteger(""+0), 
				new Power().operation(new BigInteger(""+-1),new BigInteger("3000000000")));
	}

}
