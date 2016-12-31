package pl.edu.agh.kis.factorial;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class TempCashTests {

	@Test
	public void testAdd() {
		TempCash s = new TempCash();
		
		assertFalse(s.exist(5));
		s.add(5,new BigInteger("120"));
		assertTrue(s.exist(5));		}

	@Test
	public void testGet() {
		TempCash s = new TempCash();
		
		assertFalse(s.exist(5));
		s.add(5,new BigInteger("120"));
		assertTrue(s.exist(5));
		s.get(5);
		assertTrue(s.exist(5));

		//Jeśli nie ma czegoś w kolejce, to zwraca 0
		assertFalse(s.exist(6));
        assertEquals(null,s.get(6));	}

	@Test
	public void testExist() {
		TempCash s = new TempCash();
		
		s.add(5,new BigInteger("120"));
		assertTrue(s.exist(5));
		assertFalse(s.exist(4));
		
		//Nawet get ma nie wyciągnąć obiektu
		s.get(5);
		assertFalse(s.exist(4));
		assertTrue(s.exist(5));		}

}
