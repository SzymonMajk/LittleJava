package pl.edu.agh.kis.factorial;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Test;

public class FactorialCalculatorTests {

	@Test
	public void testAdd() throws ClassNotFoundException, SQLException {
		
		/*Pierwszy przypadek SimpleCash, SimpleQueue*/
		FactorialCalculator n1 = new FactorialCalculator(new SimpleCash(),
				new SimpleQueue());
		assertEquals("",n1.calculate());
		n1.add(2);
		assertEquals("2 = 2\n",n1.calculate());

		/*Drugi przypadek TempCash, SimpleQueue*/
		FactorialCalculator n2 = new FactorialCalculator(new TempCash(),
				new SimpleQueue());
		assertEquals("",n2.calculate());
		n2.add(2);
		assertEquals("2 = 2\n",n2.calculate());
		
		/*Trzeci przypadek PriorityQueueCash, SimpleQueue*/
		FactorialCalculator n3 = new FactorialCalculator
				(new PriorityQueueCash(), new SimpleQueue());
		assertEquals("",n3.calculate());
		n3.add(2);
		assertEquals("2 = 2\n",n3.calculate());
		
		/*Czwarty przypadek DBCash, SimpleQueue*/
		/*Najpierw tworzymy połączenie z bazą danych*/
		Class.forName("org.h2.Driver");
	    final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	    final String DB_USER = "sa";
	    final String DB_PASSWD = "";
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);

	    DBCash d = new DBCash(conn);
	    
		FactorialCalculator n4 = new FactorialCalculator(d,
				new SimpleQueue());
		assertEquals("",n4.calculate());
		n4.add(2);
		assertEquals("2 = 2\n",n4.calculate());

        //sprzątanie po testach
        d.freeCash();
		conn.close();
	}

	@Test
	public void testCalculate() throws ClassNotFoundException, SQLException {
		/*Pierwszy przypadek SimpleCash, SimpleQueue*/
		FactorialCalculator n1 = new FactorialCalculator(new SimpleCash(),
				new SimpleQueue());
		assertEquals("",n1.calculate());
		n1.add(0);
		assertEquals("0 = 1\n",n1.calculate());

		/*Drugi przypadek TempCash, SimpleQueue*/
		FactorialCalculator n2 = new FactorialCalculator(new TempCash(),
				new SimpleQueue());
		assertEquals("",n2.calculate());
		n2.add(0);
		assertEquals("0 = 1\n",n2.calculate());
		
		/*Trzeci przypadek PriorityQueueCash, SimpleQueue*/
		FactorialCalculator n3 = new FactorialCalculator
				(new PriorityQueueCash(), new SimpleQueue());
		assertEquals("",n3.calculate());
		n3.add(0);
		assertEquals("0 = 1\n",n3.calculate());
		
		/*Czwarty przypadek DBCash, SimpleQueue*/
		/*Najpierw tworzymy połączenie z bazą danych*/
		Class.forName("org.h2.Driver");
	    final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	    final String DB_USER = "sa";
	    final String DB_PASSWD = "";
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);

	    DBCash d = new DBCash(conn);
	    
		FactorialCalculator n4 = new FactorialCalculator(d,
				new SimpleQueue());
		assertEquals("",n4.calculate());
		n4.add(0);
		assertEquals("0 = 1\n",n4.calculate());

        //sprzątanie po testach
        d.freeCash();
		conn.close();
	}	

}
