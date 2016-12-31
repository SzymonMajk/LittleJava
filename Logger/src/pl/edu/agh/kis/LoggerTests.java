package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

public class LoggerTests {

	@Test
	public void testSetPriorityLevel() throws IOException {
		//Plikowym będzie najłatwiej sprawdzać, najpierw poziom Info
		Logger l = new Logger(new FileAppender("LOGGERLEVELJUNITEST1"));
		BufferedReader inRead;

		//czytamy z pliku by sprawdzać czy zawartość jest równa oczekiwanej
		File testLog = new File("logs/LOGGERLEVELJUNITEST1");

		l.setPriorityLevel(0);

		l.info("First Log","Log");
		l.execute();
		
		String line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST2"));
		testLog = new File("logs/LOGGERLEVELJUNITEST2");
		
		l.setPriorityLevel(0);

		l.warning("First Log","Log");
		l.execute();
		
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST3"));
		testLog = new File("logs/LOGGERLEVELJUNITEST3");
		
		l.setPriorityLevel(0);

		l.error("First Log","Log");
		l.execute();
		
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//Teraz dla poziomu Warning
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST4"));
		testLog = new File("logs/LOGGERLEVELJUNITEST4");
				
		l.setPriorityLevel(1);

		l.info("First Log","Log");
		l.execute();
				
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
				
		line = inRead.readLine();
		assertEquals(null,line);
				
		inRead.close();
				
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
				
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST5"));
		testLog = new File("logs/LOGGERLEVELJUNITEST5");
		
		l.setPriorityLevel(1);

		l.warning("First Log","Log");
		l.execute();
		
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST6"));
		testLog = new File("logs/LOGGERLEVELJUNITEST6");
		
		l.setPriorityLevel(1);

		l.error("First Log","Log");
		l.execute();
		
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}		
		//zamykamy strumienie i usuwamy pliki
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//Teraz dla poziomu Error
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST7"));
		testLog = new File("logs/LOGGERLEVELJUNITEST7");
				
		l.setPriorityLevel(2);

		l.info("First Log","Log");
		l.execute();
				
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
				
		line = inRead.readLine();
		assertEquals(null,line);
				
		inRead.close();
				
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
				
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST8"));
		testLog = new File("logs/LOGGERLEVELJUNITEST8");
		
		l.setPriorityLevel(2);

		l.warning("First Log","Log");
		l.execute();
		
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//kolejny przypadek
		l.changeAppender(new FileAppender("LOGGERLEVELJUNITEST9"));
		testLog = new File("logs/LOGGERLEVELJUNITEST9");
		
		l.setPriorityLevel(2);

		l.error("First Log","Log");
		l.execute();
		
		line = "";
		inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}		
		//zamykamy strumienie i usuwamy pliki
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
		
		//Teraz kilka testów dla podania błędnych wartości Priority
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l.setPriorityLevel(3);
		assertEquals("Podano błędny priority Level!\n", outContent.toString());

		l.setPriorityLevel(-1);
		assertEquals("Podano błędny priority Level!\nPodano błędny priority Level!\n",
				outContent.toString());
	}

	@Test
	public void testChangeAppender() throws IOException {
		//Tworzymy obiekt logger z nullappenderem
		
		Logger l = new Logger(new NullAppender());
		
		//zmieniamy appender na konsolowy
		l.changeAppender(new ConsoleAppender());
		
		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły na konsolę
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l.info("First Log","Log");
		l.warning("Second Log","Log");
		l.error("Third Log","Log");
		l.execute();
		
		assertNotEquals("", outContent.toString());
		
		l.changeAppender(new FileAppender("LOGGERAPPENDERJUNITEST"));
		
		//czytamy z pliku by sprawdzać czy zawartość jest równa oczekiwanej
		File testLog = new File("logs/LOGGERAPPENDERJUNITEST");

		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły do pliku
		l.info("First Log","Log");
		l.warning("Second Log","Log");
		l.error("Third Log","Log");
		l.execute();
		
		String line = "";
		BufferedReader inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		//zamykamy strumienie i usuwamy pliki
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
	public void testInfo() {
		Logger l = new Logger(new ConsoleAppender());
		
		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły na konsolę
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l.execute();
		assertEquals("", outContent.toString());

		l.error("Test Log","Log");
		l.execute();
		
		assertNotEquals("", outContent.toString());
	}

	@Test
	public void testWarning() {
		Logger l = new Logger(new ConsoleAppender());
		
		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły na konsolę
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l.execute();
		assertEquals("", outContent.toString());

		l.error("Test Log","Log");
		l.execute();
		
		assertNotEquals("", outContent.toString());
	}

	@Test
	public void testError() {
		Logger l = new Logger(new ConsoleAppender());
		
		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły na konsolę
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l.execute();
		assertEquals("", outContent.toString());

		l.error("Test Log","Log");
		l.execute();
		
		assertNotEquals("", outContent.toString());	
	}

	@Test
	public void testExecute() {
		Logger l = new Logger(new ConsoleAppender());
		
		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły na konsolę
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l.execute();
		assertEquals("", outContent.toString());

		l.info("First Log","Log");
		l.warning("Second Log","Log");
		l.error("Third Log","Log");
		l.execute();
		
		assertNotEquals("", outContent.toString());
	}

	@Test
	public void testLoggerAppends() throws IOException {
		//Tworzymy dwa obiekty logger i patrzymy czy wypisuje się
		//w odpowiednich miejscach
		
		Logger l1 = new Logger(new ConsoleAppender());
		
		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły na konsolę
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
		
		l1.info("First Log","Log");
		l1.warning("Second Log","Log");
		l1.error("Third Log","Log");
		l1.execute();
		
		assertNotEquals("", outContent.toString());
		
		Logger l2 = new Logger(new FileAppender("LOGGERAPPENDERJUNITEST"));
		
		//czytamy z pliku by sprawdzać czy zawartość jest równa oczekiwanej
		File testLog = new File("logs/LOGGERAPPENDERJUNITEST");

		//tworzymy trzy logi, wysyłamy je i sprwadzamy czy trafiły do pliku
		l2.info("First Log","Log");
		l2.warning("Second Log","Log");
		l2.error("Third Log","Log");
		l2.execute();
		
		String line = "";
		BufferedReader inRead = new BufferedReader(new FileReader(testLog));
		
		line = inRead.readLine();
		assertNotEquals(null,line);
		
		//zamykamy strumienie i usuwamy pliki
		inRead.close();
		
		if(testLog.exists())
		{
			if(!(testLog.delete()))
			{
				fail("Test files not deleted");
			}	
		}
	}

}
