package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

/**
 * Kilka prostych testów dla bufora stron
 * @author Szymon Majkut
 * @version 1.1b
 *
 */
public class BlockingQueuePagesBufferTest {

	/**
	 * Sprawdzamy czy nasza funkcja informuj¹ca o pustoœci dobrze podaje
	 */
	@Test
	public void testIsEmpty() {

		final int size = 5;
		
		//Przygotowanie obiektów
		BlockingQueuePagesBuffer b = new BlockingQueuePagesBuffer(size, new NullAppender());
		
		assertEquals(true,b.isEmpty());
		
		b.addPage("Page");
		
		assertEquals(false,b.isEmpty());
		
		b.takePage();
		
		assertEquals(true,b.isEmpty());

		for(int i = 0; i < size; ++i)
		{
			b.addPage("Page");
			assertEquals(false,b.isEmpty());
		}
		
		for(int i = 0; i < size; ++i)
		{
			assertEquals(false,b.isEmpty());
			b.takePage();
		}

		assertEquals(true,b.isEmpty());
	}

	/**
	 * Sprawdzamy czy nasza funkcja informuj¹ca o pe³noœci dobrze podaje
	 */
	@Test
	public void testIsFull() {

		final int size = 5;
		
		//Przygotowanie obiektów
		BlockingQueuePagesBuffer b = new BlockingQueuePagesBuffer(size, new NullAppender());
		
		assertEquals(false,b.isFull());
		
		b.addPage("Page");
		
		assertEquals(false,b.isFull());
		
		b.takePage();
		
		assertEquals(false,b.isFull());

		for(int i = 0; i < size; ++i)
		{
			assertEquals(false,b.isFull());
			b.addPage("Page");
		}
		
		assertEquals(true,b.isFull());
		
		for(int i = 0; i < size; ++i)
		{
			b.takePage();
			assertEquals(false,b.isFull());
		}

		assertEquals(false,b.isFull());
	}

	
	@Test
	public void testAddPage() throws InterruptedException {

		final int size = 5;
		
		//Przygotowanie obiektów
		BlockingQueuePagesBuffer b = new BlockingQueuePagesBuffer(size, new NullAppender());
		
		String result = "";
		
		TestThread first = new TestThread(b) {
			
			@Override
			public void run() 
			{
				for(int i = 0; i < 2; ++i)
				{
					b.addPage("Page"+i);
				}
			}
		};
		
		TestThread second = new TestThread(b) {
			
			@Override
			public void run() 
			{
				for(int i = 3; i > 1; --i)
				{
					b.addPage("Page"+i);
				}
			}
		};
		
		first.start();
		second.start();
		
		first.join();
		second.join();
		
		assertEquals(false,b.isEmpty());
		assertEquals(false,b.isFull());
		
		while(!b.isEmpty())
		{
			result += b.takePage();
		}
		
		assertEquals(false,b.isFull());
		
		assertEquals(true,result.contains("Page0"));
		assertEquals(true,result.contains("Page1"));
		assertEquals(true,result.contains("Page2"));
		assertEquals(true,result.contains("Page3"));
		assertEquals(false,result.contains("Page4"));
	}

	@Test
	public void testTakePage() throws InterruptedException {
		
		final int size = 5;
		
		//Przygotowanie obiektów
		BlockingQueuePagesBuffer b = new BlockingQueuePagesBuffer(size, new NullAppender());
		
		ArrayBlockingQueue<String> result = new ArrayBlockingQueue<String>(size);
		
		TestThread first = new TestThread(b,result) {
			
			@Override
			public void run() 
			{
				for(int i = 0; i < 2; ++i)
				{
					assertEquals(false,b.isEmpty());
					try {
						result.put(b.takePage());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		TestThread second = new TestThread(b,result) {
			
			@Override
			public void run() 
			{
				for(int i = 0; i < 2; ++i)
				{
					assertEquals(false,b.isEmpty());
					try {
						result.put(b.takePage());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		for(int i = 0; i < 4; ++i)
		{
			b.addPage("Page"+i);
		}
		
		first.start();
		second.start();
		
		first.join();
		second.join();
		
		assertEquals(true,b.isEmpty());
		assertEquals(false,b.isFull());
		
		assertEquals(true,result.contains("Page0"));
		assertEquals(true,result.contains("Page1"));
		assertEquals(true,result.contains("Page2"));
		assertEquals(true,result.contains("Page3"));
		assertEquals(false,result.contains("Page4"));
	}
	
	/**
	 * Tworzymy producenta i konsumenta, którzy maj¹ wspó³pracowaæ
	 * @throws InterruptedException 
	 */
	@Test
	public void testTwoThreads() throws InterruptedException {

		final int size = 5;
		
		//Przygotowanie obiektów
		BlockingQueuePagesBuffer b = new BlockingQueuePagesBuffer(size, new NullAppender());
		
		ArrayBlockingQueue<String> result = new ArrayBlockingQueue<String>(size*4);
		
		TestThread consumer = new TestThread(b,result) {
			
			@Override
			public void run() 
			{
				for(int i = 0; i < size*4; ++i)
				{
					try {
						result.put(b.takePage());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
		
		TestThread producer = new TestThread(b,result) {
			
			@Override
			public void run() 
			{
				for(int i = 0; i < size*4; ++i)
				{
					b.addPage("Page");
				}
			}
		};
		
		consumer.start();
		producer.start();
		
		consumer.join();
		producer.join();
		
		assertEquals(true,b.isEmpty());
		assertEquals(false,b.isFull());
		
		
		assertEquals(true,result.contains("Page"));
		assertEquals(size*4,result.size());
	}

}

/**
 * Pomocnicza klasa pomagaj¹ca przy testowaniu
 * @author Szymon Majkut
 * @version 1.0
 *
 */
class TestThread extends Thread {
	
	private BlockingQueuePagesBuffer b;
	
	
	public BlockingQueuePagesBuffer getB() {
		return b;
	}

	public void setB(BlockingQueuePagesBuffer b) {
		this.b = b;
	}
	
	private ArrayBlockingQueue<String> s;
	

	public ArrayBlockingQueue<String> getS() {
		return s;
	}

	public void setS(ArrayBlockingQueue<String> s) {
		this.s = s;
	}
	
	TestThread(BlockingQueuePagesBuffer b)
	{
		this.setB(b);
	}
	
	TestThread(BlockingQueuePagesBuffer b, ArrayBlockingQueue<String> s)
	{
		this.setB(b);
		this.setS(s);
	}
}


