package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class FileAppenderTests {

	@Test
	public void testSendNext() throws IOException {
		FileAppender f = new FileAppender("JUNITTESTLOGSENDNEXT");
		
		//czytamy z pliku by sprawdzać czy zawartość jest równa oczekiwanej
		File testLog = new File("logs/JUNITTESTLOGSENDNEXT");

		f.sendNext("");
		f.sendNext("First Test Log");
		f.sendNext("Second Test Log");

		String line = "";
		BufferedReader inRead = new BufferedReader(new FileReader(testLog));

		line = inRead.readLine();
		assertEquals("",line);
		
		line = inRead.readLine();
		assertEquals("First Test Log",line);
			
		line = inRead.readLine();
		assertEquals("Second Test Log",line);
		
		//sprzątamy po testach
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}
			
		}
		
	}

	@Test
	public void testClear() throws IOException {
		FileAppender f = new FileAppender("JUNITTESTLOGCLEAR");
		
		//czytamy z pliku by sprawdzać czy zawartość jest równa oczekiwanej
		File testLog = new File("logs/JUNITTESTLOGCLEAR");

		f.sendNext("");
		f.sendNext("First Test Log");
		f.sendNext("Second Test Log");

		String line = "";
		BufferedReader inRead = new BufferedReader(new FileReader(testLog));

		line = inRead.readLine();
		assertEquals("",line);
		
		line = inRead.readLine();
		assertEquals("First Test Log",line);
			
		line = inRead.readLine();
		assertEquals("Second Test Log",line);
		
		f.clear();
		
		BufferedReader inRead2 = new BufferedReader(new FileReader(testLog));
		line = inRead2.readLine();
		assertEquals(null,line);
		
		//sprzątamy po testach
		inRead.close();
		inRead2.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}
			
		}
	}

	@Test
	public void testFileAppenderString() {
		FileAppender f = new FileAppender("JUNITTESTLOGCON");
		
		//sprawdzamy czy utworzono plik
		File testLog = new File("logs/JUNITTESTLOGCON");

		//a niech coś tam zrobi!
		f.clear();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}
		}
		else
		{
			fail("Test not created");
		}
	}

}
