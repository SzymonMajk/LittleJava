package pl.edu.agh.kis;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class TaskTest {

	@Test
	public void testChangeStatus1() {
		Task newTask = new Task(0,"999","5","5","Post","host");

		assertEquals(0,newTask.getStatus());
		newTask.setStatus(1);
		assertEquals(1,newTask.getStatus());
	}
	
	@Test
	public void testChangeStatus2() {
		Task newTask = new Task(0,"999","5","5","Post","host");

		assertEquals(0,newTask.getStatus());
		newTask.setStatus(2);
		assertEquals(2,newTask.getStatus());
	}
	
	@Test
	public void testChangeStatus3() {
		Task newTask = new Task(0,"999","5","5","Post","host");

		assertEquals(0,newTask.getStatus());
		newTask.setStatus(0);
		assertEquals(0,newTask.getStatus());
	}
	
	@Test
	public void testChangeStatus4() {
		Task newTask = new Task(0,"999","5","5","Post","host");

		assertEquals(0,newTask.getStatus());
		newTask.setStatus(3);
		assertEquals(0,newTask.getStatus());
	}
	
	@Test
	public void testChangeStatus5() {
		Task newTask = new Task(0,"999","5","5","Post","host");

		assertEquals(0,newTask.getStatus());
		newTask.setStatus(-1);
		assertEquals(0,newTask.getStatus());
	}

}
