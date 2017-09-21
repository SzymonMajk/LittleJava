package pl.edu.agh.kis.crawler;

import java.sql.*;

public class DatabaseVisitedPages implements VisitedPages {
 
    Connection conn;
	
	public boolean pageAlreadyVisited(String pageString) {
		PreparedStatement stmt = null;
		boolean visited = false;
        try {
            stmt = conn
            		.prepareStatement("select url from visited where url like ?");
            stmt.setString(1, pageString);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                visited = rs.getString(1).equals(pageString);
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
		return visited;
	}

	public void addVisitedPage(String pageString) {
		insertUrl(conn,pageString);
	}
	
	private static void insertUrl(Connection dbConnection, String url) {

        PreparedStatement stmt = null;

        try {

            stmt = dbConnection
                    .prepareStatement("insert into visited (url) "
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

	
	public DatabaseVisitedPages(Connection c)
	{
		conn = c;
		
		Statement stmt = null;

        try {

            stmt = conn.createStatement();

            stmt.executeUpdate("create table IF NOT EXISTS visited(id INT auto_increment, url varchar(900))");

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
