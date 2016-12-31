package pl.edu.agh.kis;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleLogCashTests {

	@Test
	public void testAddLog() {
		SimpleLogCash cash = new SimpleLogCash();
		assertTrue(cash.isEmpty());
		
		cash.addLog("Test Log");
		assertFalse(cash.isEmpty());
		
		for(int i = 0; i < 10000; ++i)
		{
			cash.addLog("Test Log"+i);
		}
		
		assertFalse(cash.isEmpty());
		
		for(int i = 0; i < 10001; ++i)
		{
			cash.pollLog();
		}	
		
		assertTrue(cash.isEmpty());
	}

	@Test
	public void testIsEmpty() {
		SimpleLogCash cash = new SimpleLogCash();
		assertTrue(cash.isEmpty());
		
		cash.addLog("Test Log");
		assertFalse(cash.isEmpty());
		
		cash.pollLog();
		assertTrue(cash.isEmpty());
	}

	@Test
	public void testPollLog() {
		SimpleLogCash cash = new SimpleLogCash();
		assertTrue(cash.isEmpty());
		
		cash.addLog("Test Log");
		assertFalse(cash.isEmpty());
		
		for(int i = 0; i < 10000; ++i)
		{
			cash.addLog("Test Log"+i);
		}
		
		assertFalse(cash.isEmpty());
		
		for(int i = 0; i < 10001; ++i)
		{
			cash.pollLog();
		}	
		
		assertTrue(cash.isEmpty());
		
		//test co się stanie gdy chcemy wyciągać z pustego
		assertEquals(null,cash.pollLog());
	}
}
