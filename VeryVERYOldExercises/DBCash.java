package pl.edu.agh.kis;

import java.sql.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class DBCash {
	
	private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWD = "";
    
    Connection dbConnection;
	
	/**
	 * Dodaje jednocześnie do mapy i do zbioru, aby można było
	 * łatwo sprawdzić czy obiekt jest w mapie, a również łatwo pobrać
	 * jeśli znajduje się takowy w zbiorze
	 */
	public void add(int i, BigInteger b) {
		
		//łączymy z bazą danych i dodajemy
        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            //zmień żeby się nie duplikowały!!!
            stmt.executeUpdate("insert into factorial (number,result) "

                    + "values( '" + i + "', '" + b + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
            // zwalnianie zasobow
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	}

	/**
	 * Najprostrze pobranie wartości z mapy
	 * @param i liczba dla której mamy zwrócić wartość
	 */
	public BigInteger get(int i) {
		
		//Zakładamy, że użytkownik już sprawdził czy istnieje i z bazy myk
		BigInteger result = new BigInteger("1");
		
	    PreparedStatement stmt = null;
	    try {
	        stmt = dbConnection
	                .prepareStatement("Select Result from Factorial where Number like ?");
	        stmt.setInt(1, i);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            if(rs != null)
	            {
	            	result = new BigInteger((rs.getString(1)));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // w kazdym wypadku jesli stmt nie null to go zamknij -
	        // zwalnianie zasobow
	        if (stmt != null) {
	            try {
	                stmt.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	       
	    }
		
		return result;
	}

	/**
	 * Funkcja sprawdza czy obiekt istnieje w cashu
	 * @param i liczba która istnienie chcemy sprawdzić
	 */
	public boolean exist(int i) {
		/*
		 * Sprawdza obecnośc elementu w zbiorze jeżeli element
		 * jest w zbiorze, to musi znajdować się również w mapie
		 */
			boolean result = false;
		    PreparedStatement stmt = null;
		    try {
		        stmt = dbConnection
		                .prepareStatement("Select Number from Factorial where Number like ?");
		        stmt.setInt(1, i);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            if(rs != null)
		            {
		            	result = true;
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        // w kazdym wypadku jesli stmt nie null to go zamknij -
		        // zwalnianie zasobow
		        if (stmt != null) {
		            try {
		                stmt.close();
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        }
		       
		    }
		    
		return result;
	}
	
	private void tableCreate()
	{
		//tworzenie tablicy
        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            stmt.executeUpdate("create table IF NOT EXISTS factorial(id INT auto_increment, number INT, "
            		+ " result varchar(900000000000000))");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
            // zwalnianie zasobow
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	public DBCash(Connection conn)
	{
		dbConnection = conn;
		
		//najpierw tworzymy tabelę jeśli jeszcze jej nie ma
		tableCreate();
		
	}

	/**
	 * Kilka testów
	 * @param args nie używamy
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// wczytanie sterownika bazy danych (z pliku h2-[wersja].jar)
        Class.forName("org.h2.Driver");

        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD); 
        
		DBCash sC = new DBCash(conn);
		
		sC.add(2,new BigInteger("2"));
		sC.add(3,new BigInteger("6"));
		sC.add(4,new BigInteger("24"));
		sC.add(5,new BigInteger("120"));
		
		if(sC.exist(4))
		{
			System.out.println(sC.get(4));
		}
		
		if(sC.exist(6))
		{
			System.out.println(sC.get(6));
		}
		
		conn.close();
		
	}

}
