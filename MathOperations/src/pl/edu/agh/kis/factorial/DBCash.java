package pl.edu.agh.kis.factorial;

import java.sql.*;
import java.math.BigInteger;

/**
 * Sposób składowania liczb i wartości w bazie danych, wykorzystując bazę danych
 * JDBC - h2
 * @author Szymon Majkut
 *
 */
public class DBCash implements Cashable {
    
    Connection dbConnection;
	
	/**
	 * Dodaje element do bazy danych, zakładamy że tabela fatorial już istnieje
	 */
	public void add(int i, BigInteger b) {
		
		//najpierw sprawdzamy czy dana wartość już istnieje, by nie tworzyć duplikatów
		if(exist(i))
		{
			return;
		}
		
		//łączymy z bazą danych i dodajemy
		PreparedStatement stmt = null;

        try {

        	stmt = dbConnection
                    .prepareStatement("insert into factorial" +
            		"(number,result) values(?,?)");
        	
            stmt.setInt(1, i);
            stmt.setString(2, b.toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
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
	 * Najprostrze pobranie wartości z bazy danych, użytkownik samodzielnie
	 * musi sprawdzić czy element istnieje
	 * @param i liczba dla której mamy zwrócić wartość
	 */
	public BigInteger get(int i) {
		
		BigInteger result = new BigInteger("0");
		PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("select result from factorial where number like ?");
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if(rs.getString(1) != null)
                {
                	result = new BigInteger(rs.getString(1));
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
		boolean exist = false;
		PreparedStatement stmt = null;
	        try {
	            stmt = dbConnection
	                    .prepareStatement("select result from factorial where number like ?");
	            stmt.setInt(1, i);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                if(rs.getString(1) != null)
	                {
	                	exist = true;
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
		return exist;
	}
	
	/**
	 * Funkcja odpowiada za wysłanie zapytania sql tworzącego tabelę
	 */
	private void tableCreate()
	{
		//tworzenie tablicy
        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            stmt.executeUpdate("create table IF NOT EXISTS factorial" +
            		"(id INT auto_increment, number INT, result VARCHAR(9999999999))");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
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
	 * Konstrukor sparametryzowany, który ma za zadanie wstawić obiekt łączący
	 * z bazą danych oraz jeżeli istnieje taka potrzeba, utworzyć tabelę w bazie
	 * @param conn
	 */
	public DBCash(Connection conn)
	{
		dbConnection = conn;
		
		//najpierw tworzymy tabelę jeśli jeszcze jej nie ma
		tableCreate();
		
	}

	/**
	 * Funkcja głównie do testów, odpowiedzialna za usunięcie zawartości bazy danych
	 * @return 
	 */
	public void freeCash()
	{
		//tworzenie tablicy
        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            stmt.executeUpdate("drop table factorial");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	}

}
