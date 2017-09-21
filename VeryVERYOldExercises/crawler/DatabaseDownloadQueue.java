package pl.edu.agh.kis.crawler;

import java.sql.*;

public class DatabaseDownloadQueue implements DownloadQueue {
    
    Connection conn;
	
	public void addPage(String pageString) {
		insertUrl(conn,pageString);

	}

	public boolean isEmpty() {
		PreparedStatement stmt = null;
		boolean empty = true;
        try {
            stmt = conn
            		.prepareStatement("select count(*) from urls");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                empty = rs.getInt(1) == 0;
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
		return empty;
	}

	public String getNextPage() {
		
		String result = "";
		if(!isEmpty())
		{
			result = getFirst(conn);
			deleteFirst(conn);
		}
		
		return result;
		
	}

	private static String getFirst(Connection dbConnection)
	{
		PreparedStatement stmt = null;
		String result = "";
        try {
            stmt = dbConnection
            		.prepareStatement("SELECT * FROM urls ORDER BY id LIMIT 1;");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
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
	
	private static void deleteFirst(Connection dbConnection)
	{
		PreparedStatement stmt = null;
        try {
            stmt = dbConnection
            		.prepareStatement("DELETE FROM urls WHERE ID IN (SELECT MIN(ID) FROM urls)");
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
	
	private static void insertUrl(Connection dbConnection, String url) {

        PreparedStatement stmt = null;

        try {

            stmt = dbConnection
                    .prepareStatement("insert into urls (url) "
                              + "values(?)");

              stmt.setString(1, url);
            
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
	
	public DatabaseDownloadQueue(Connection c)
	{
		
		conn = c;
		
		Statement stmt = null;

        try {

            stmt = conn.createStatement();

            stmt.executeUpdate("create table IF NOT EXISTS urls(id INT auto_increment, url varchar(900))");

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

}
