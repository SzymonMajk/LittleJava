package pl.edu.agh.kis;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class TaskManagerTest {

	@Test
	public void testPut() {
		TaskManager manager = new TaskManager();
		manager.put(new Task(0,"999",5,5,"Post","host"));
		
		assertEquals(true,manager.hasNextTask());
		
		assertEquals(0,manager.getNextTask().getId());
		
		manager.removeTask(0);
		
		assertEquals(false,manager.hasNextTask());
		
		for(int i = 0; i < 100; ++i)
		{
			manager.put(new Task(i,"999",5,5,"Post","host"));
		}
		
		for(int i = 0; i < 100; ++i)
		{
			assertEquals(i,manager.getNextTask().getId());
			manager.removeTask(i);
		}
		
		assertEquals(false,manager.hasNextTask());
	}
	
	@Test
	public void testRemove() {
		TaskManager manager = new TaskManager();
		
		assertEquals(false,manager.hasNextTask());

		manager.removeTask(0);

		assertEquals(false,manager.hasNextTask());

		for(int i = 0; i < 100; ++i)
		{
			manager.removeTask(i);

		}
		
		assertEquals(false,manager.hasNextTask());
		
		manager.put(new Task(0,"999",5,5,"Post","host"));
		
		assertEquals(true,manager.hasNextTask());
		
		assertEquals(0,manager.getNextTask().getId());
		
		manager.removeTask(1);
		
		assertEquals(true,manager.hasNextTask());

		manager.removeTask(-1);
		
		assertEquals(true,manager.hasNextTask());
		
		manager.removeTask(0);
		
		assertEquals(false,manager.hasNextTask());

	}
}
