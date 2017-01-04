package pl.edu.agh.kis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBPageCash implements PageCash {

static Connection conn;

	public boolean pageAlreadyVisited(String pageString) {
		PreparedStatement stmt = null;
		boolean visited = false;
        try {
            stmt = conn
            		.prepareStatement("select url from urls where visited like ?");
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
		insertVisited(pageString);
	}
	
	private static void insertVisited(String url) {

        PreparedStatement stmt = null;

        try {

            stmt = conn
                    .prepareStatement("insert into urls (visited) "
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
	
	public void addPage(String pageString) {
		insertUrl(pageString);

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
			result = getFirst();
			deleteFirst();
		}
		
		return result;
		
	}

	private static String getFirst()
	{
		PreparedStatement stmt = null;
		String result = "";
        try {
            stmt = conn
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
	
	private static void deleteFirst()
	{
		PreparedStatement stmt = null;
        try {
            stmt = conn
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
	
	private static void insertUrl(String url) {

        PreparedStatement stmt = null;

        try {

            stmt = conn
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
	
	public DBPageCash(Connection c)
	{
		
		conn = c;
		
		Statement stmt = null;

        try {

            stmt = conn.createStatement();

            stmt.executeUpdate("create table IF NOT EXISTS urls(id INT auto_increment, " +
            		"url varchar(9000), visited varchar(9000))");

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

	public String listVisited() {
		// TODO Auto-generated method stub
		return null;
	}

	public String listQueue() {
		// TODO Auto-generated method stub
		return null;
	}
}
