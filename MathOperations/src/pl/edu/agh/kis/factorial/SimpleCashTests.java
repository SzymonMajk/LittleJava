package pl.edu.agh.kis.factorial;

import static org.junit.Assert.*;

import java.math.BigInteger;
import org.junit.Test;

public class SimpleCashTests {

	@Test
	public void testAdd() {
		SimpleCash s = new SimpleCash();
		
		assertFalse(s.exist(5));
		s.add(5,new BigInteger("120"));
		assertTrue(s.exist(5));
	}

	@Test
	public void testGet() {
		SimpleCash s = new SimpleCash();
				
		assertFalse(s.exist(5));
		s.add(5,new BigInteger("120"));
		assertTrue(s.exist(5));
		s.get(5);
		assertTrue(s.exist(5));
				
		//Zwraca null gdy nie ma obiektu
		assertEquals(null,s.get(6));
	}

	@Test
	public void testExist() {
		SimpleCash s = new SimpleCash();
		
		s.add(5,new BigInteger("120"));
		assertTrue(s.exist(5));
		assertFalse(s.exist(4));
		
		//Nawet get ma nie wyciągnąć obiektu
		s.get(5);
		assertFalse(s.exist(4));
		assertTrue(s.exist(5));
	}

}
