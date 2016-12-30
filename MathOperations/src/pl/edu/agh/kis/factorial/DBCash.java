package pl.edu.agh.kis.factorial;

import java.sql.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.TreeSet;

public class DBCash implements Cashable {
	
	private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWD = "";
    
    Connection dbConnection;
    
	/**
	 * Przechowuje liczby i wyniki ich oblicze�
	 */
	private HashMap<Integer, BigInteger> calculatedNumbersWithResults =
			new HashMap<Integer, BigInteger>();
	
	/**
	 * Przechowuje liczby, dla kt�rych jeste�my w stanie znalezc wynik
	 */
	private TreeSet<Integer> calculatedNumbers = new TreeSet<Integer>();
	
	/**
	 * Dodaje jednocześnie do mapy i do zbioru, aby można było
	 * łatwo sprawdzić czy obiekt jest w mapie, a również łatwo pobrać
	 * jeśli znajduje się takowy w zbiorze
	 */
	public void add(int i, BigInteger b) {
		
		//łączymy z bazą danych i dodajemy
	}

	/**
	 * Najprostrze pobranie wartości z mapy
	 * @param i liczba dla której mamy zwrócić wartość
	 */
	public BigInteger get(int i) {
		
		//Zakładamy, że użytkownik już sprawdził czy istnieje i z bazy myk
		return new BigInteger("1");
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
		return false;
	}
	
	private void tableCreate()
	{
		//tworzenie tablicy
        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            stmt.executeUpdate("create table IF NOT EXISTS factorial" +
            		"(id INT auto_increment, number INT, "
            		+ " result ARRAY");

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
		// problem w tym pakiecie, dlatego sprawdzanie odbędzie się tam 
		// gdzie zadania z bazy danych
        Class.forName("org.h2.Driver");

        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD); 
        
		DBCash sC = new DBCash(conn);
		
		sC.add(5,new BigInteger("3"));
		sC.add(5,new BigInteger("5"));
		sC.add(5,new BigInteger("6"));
		sC.add(3,new BigInteger("5"));
		
		conn.close();
		
	}

}
