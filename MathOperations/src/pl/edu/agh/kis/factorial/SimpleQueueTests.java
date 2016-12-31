package pl.edu.agh.kis.factorial;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimpleQueueTests {

	@Test
	public void testAdd() {
		SimpleQueue s = new SimpleQueue();
		
		assertTrue(s.isEmpty());
		s.add(5);
		assertFalse(s.isEmpty());
	}

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testPoll() throws NullPointerException{
		// given
		SimpleQueue s = new SimpleQueue();
		
		assertTrue(s.isEmpty());
		s.add(5);
		assertFalse(s.isEmpty());
		s.poll();
		assertTrue(s.isEmpty());
		
        thrown.expect(NullPointerException.class);
        
        // when
        s.poll();
 
        // then
        fail("This method should throw NullPointerException");
	}

	@Test
	public void testExist() {
		SimpleQueue s = new SimpleQueue();
		
		s.add(5);
		assertTrue(s.exist(5));
		assertFalse(s.exist(4));
		
		s.poll();
		assertFalse(s.exist(4));
		assertFalse(s.exist(5));
	}

	@Test
	public void testIsEmpty() {
		SimpleQueue s = new SimpleQueue();
		
		assertTrue(s.isEmpty());
		s.add(5);
		assertFalse(s.isEmpty());
		s.poll();
		assertTrue(s.isEmpty());
	}

}
