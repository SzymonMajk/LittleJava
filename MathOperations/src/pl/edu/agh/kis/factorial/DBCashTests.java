package pl.edu.agh.kis.factorial;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Test;

//Uwaga!, nie uruchamiamy testów, gdy mamy coś cennego w cashu!

public class DBCashTests {

	@Test
	public void testAdd() throws ClassNotFoundException, SQLException {
		/*Najpierw tworzymy połączenie z bazą danych*/
		Class.forName("org.h2.Driver");
	    final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	    final String DB_USER = "sa";
	    final String DB_PASSWD = "";
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
		
        DBCash sC = new DBCash(conn);
	
        sC.add(2, new BigInteger("2"));
        sC.add(3, new BigInteger("6"));
        sC.add(4, new BigInteger("24"));
        
        assertTrue(sC.exist(2));
        assertTrue(sC.exist(3));
        assertTrue(sC.exist(4));
        
        assertFalse(sC.exist(1));
        assertFalse(sC.exist(5));
        assertFalse(sC.exist(0));
        
        //sprzątanie po testach
        sC.freeCash();
		conn.close();
	}

	@Test
	public void testGet() throws SQLException, ClassNotFoundException {
		Class.forName("org.h2.Driver");
	    final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	    final String DB_USER = "sa";
	    final String DB_PASSWD = "";
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
		
        DBCash sC = new DBCash(conn);
		
        assertEquals(new BigInteger("0"),sC.get(2));
        
        sC.add(2, new BigInteger("2"));
        
        assertEquals(new BigInteger("2"),sC.get(2));
        
        //sprzątanie po testach
        sC.freeCash();
		conn.close();
	}

	@Test
	public void testExist() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
	    final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	    final String DB_USER = "sa";
	    final String DB_PASSWD = "";
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
		
        DBCash sC = new DBCash(conn);
		
        assertFalse(sC.exist(0));
        assertFalse(sC.exist(1));
        assertFalse(sC.exist(2));
        
        sC.add(1, new BigInteger("1"));
        
        assertFalse(sC.exist(0));
        assertTrue(sC.exist(1));
        assertFalse(sC.exist(2));
        
        //sprzątanie po testach
        sC.freeCash();
		conn.close();
	}
}
