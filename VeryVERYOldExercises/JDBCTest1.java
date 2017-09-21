package pl.edu.agh.kis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class JDBCTest1 {
    private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWD = "";

    public static void main(String[] args) throws Exception {
        // wczytanie sterownika bazy danych (z pliku h2-[wersja].jar)
        Class.forName("org.h2.Driver");

        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);        

        File in = new File("/home/szymek/pliki/in");
        File out = new File("/home/szymek/pliki/out");
        
        FileReader rIn = new FileReader(in);
        FileWriter rOut = new FileWriter(out);
        
        BufferedReader bRIn = new BufferedReader(rIn);
        
        // kod aplikacji
        //insertEmployee(conn, "Jan", "Kowalski");
        // wersja 2
        
        /*
        Date dateOfBirth = createDate(1990, 4, 21);
        *//*
        insertEmployeeV2(conn,"AAAAndzrej", "JAkCInaImie", dateOfBirth);  
        
        */
        /*Do ćwiczenia 1.3.1*/
        /*
        Date dateOfMoney1 = createDate(1997, 5, 26);
        Date dateOfMoney2 = createDate(1998, 8, 21);
        Date dateOfMoney3 = createDate(1996, 12, 4);
        insertSalary(conn,3,new BigDecimal(532.21),dateOfMoney1);
        insertSalary(conn,2,new BigDecimal(533.21),dateOfMoney2);
        insertSalary(conn,3,new BigDecimal(432.21),dateOfMoney3);
        */
        /*------------------*/
        
        /*Do ćwiczenia 1.3.2*/
        /*
        insertEmployeeWithSalary(conn,"Basia","Stanczyk",dateOfBirth,5,new BigDecimal(5235.32));
        insertEmployeeWithSalary(conn,"Jasia","Stalczyk",dateOfBirth,4,new BigDecimal(3235.32));
        */
        /*-------------------*/
        
        /*Do ćwiczenia 1.4.1*/
        /*
        printEmployeeList(getEmployeeList(conn));
        */
        /*-------------------*/
        
        /*Do ćwiczenia 1.5.1*/
        /*
        printEmployeeList(findEmployeeBySurname(
                conn, "Stalczyk"));
        */
        /*-------------------*/
        
        /*Do ćwiczenia 1.6.1*/
        /*
        printEmployeeList(getEmployeeList(conn));
        removeEmployee(conn, 1354);
        printEmployeeList(getEmployeeList(conn));
        */
        /*-------------------*/
        
        /*Do ćwiczenia 1.7.1*/
        /*
        System.out.println(getNoOfEmployees(conn));
        */
        /*-------------------*/
        
        /*Zadanie 1*/
        /*
        System.out.println(employeeWithSalaryHistory(conn,
        8).getEmployeeWithSalaries());
        */
        /*-------------------*/
        
        /*Zadanie 1.2*/
        /*
        for(int i = 0; i < JDBCTest1.getNoOfEmployees(conn);++i)
        {
            System.out.println(employeeWithLatestSalary(conn,
            		i+1).getEmployeeWithSalaries());
        }
        */
        /*-------------------*/
        
        /*Zadanie 2.0*/
        //Branie lini z pliku
        /*
        ArrayList<String> badLines = new ArrayList<String>();
        
        try {
        	
        	String lineFromFile;
        	int numberOfLines = 0;
        	
        	while((lineFromFile = bRIn.readLine()) != null)
        	{
        		++numberOfLines;
        		
        		//musi być wcześniej, no bo zwraca nast, czyli tego którego dodamy teraz
        		int employeeId = getNextEmployeeId(conn);
        		
        		//przygotowujemy linię do przepisania
            	String[] dividedLine = lineFromFile.split(" ");
            	
            	//dopóki nie trafimy na nowego poprawnego pracownika, to przerywamy
        		if(dividedLine.length != 3)
        		{
        			//można np. spisywać tutaj wadliwe linijki z info
        			badLines.add("Błąd 1 w linii: " + numberOfLines);
        			continue;
        		}
            	
            	String firstName = dividedLine[0];
            	String surname = dividedLine[1];
        		
            	
            	String[] dividedTime = dividedLine[2].split("-");
            	
            	if(dividedTime.length != 3)
            	{
            		//można np. spisywać tutaj wadliwe linijki z info
        			badLines.add("Błąd 2 w linii: " + numberOfLines);
            		continue;
            	}
            	
            	Date dateOfBirth = createDate( Integer.parseInt(dividedTime[0]),
            			Integer.parseInt(dividedTime[1]),Integer.parseInt(dividedTime[2]) );
            	
            	// test
        		//System.out.println(lineFromFile);
            	insertEmployeeV2(conn,firstName, surname,dateOfBirth);
        		
	            while((lineFromFile = bRIn.readLine()) != null)
	            {
	            	++numberOfLines;
	            	
	            	if(lineFromFile.equals("end"))
	            	{
	            		break;
	            	}
	            		
	                dividedLine = lineFromFile.split(" ");
	                
	                if(dividedLine.length != 2)
	                {
	                	//można np. spisywać tutaj wadliwe linijki z info
	        			badLines.add("Błąd 3 w linii: " + numberOfLines);
	                	break;
	                }
	                	
	                dividedTime = dividedLine[1].split("-");
	                	
	                if(dividedTime.length != 3)
	                {
	                	//można np. spisywać tutaj wadliwe linijki z info
	        			badLines.add("Błąd 4 w linii: " + numberOfLines);
	                	break;
	                }

		            BigDecimal amount = new BigDecimal(Double.parseDouble(dividedLine[0]));
		                	
		            dividedTime = dividedLine[1].split("-");
		            Date dateOfSalary = createDate( Integer.parseInt(dividedTime[0]),
		            		Integer.parseInt(dividedTime[1]),Integer.parseInt(dividedTime[2]) );
		            // test
	                // System.out.println(lineFromFile);
		            insertSalary(conn,employeeId,amount,dateOfSalary);
            	}
        	}
	            //Możemy wypisać błędy:
        	
            for(String s : badLines)
            {
            	System.out.println(s);
            }
            
        	
        } finally {
            if (bRIn != null) {
            	bRIn.close();
            }
        
        }
        */
        /*Zadanie 3.0*/
        //Teraz już tylko pętla i zapisanie stringów do pliku!
        
        /*Wypisujemy pracownika, zapmiętujemy jego id i wypisujemy wszystkie pensje
         * o podanym id*/
        /*
            try {
            	for(int i = JDBCTest1.getNextEmployeeId(conn)-JDBCTest1.getNoOfEmployees(conn)
            			; i < JDBCTest1.getNextEmployeeId(conn);++i)
                {
            		//zapisujemy pracownika
            		rOut.write(getStringInfoOfEmployeeById(conn, i)+"\n");
            		//zappisujemy jego pencje
            		ArrayList<String> salaryList = getStringofSalaryById(conn,i);
            		for(String s : salaryList)
            		{
            			rOut.write(s+"\n");
            		}
            		//dodajemy znak końca osoby z pensjami
            		rOut.write("end\n");
            		//czyścimy listę dla pensji ewentualnego nowego pracownika
            		salaryList.clear();
            		
            		
                }
            } finally {
                if (rOut != null) {
                	rOut.close();
                }
            
            }
        */
        /*-------------------*/
        
        // zamkniecie polaczenia z baza
        conn.close();
        System.out.println("Done.");
    }

	private static void insertEmployee(Connection dbConnection, String firstName, String surname) {

        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            // uwaga - w tej wersji nie wstawiamy daty urodzenia 

            stmt.executeUpdate("insert into pracownik (imie,nazwisko) "

                    + "values( '" + firstName + "', '" + surname + "')");

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


    private static void insertEmployeeV2(Connection dbConnection, String firstName, String surname, Date dateOfBirth) {

        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                  .prepareStatement("insert into pracownik (imie,nazwisko,data_urodzenia) "
                            + "values(?,?,?)");

            stmt.setString(1, firstName);
            stmt.setString(2, surname);
            stmt.setDate(3, dateOfBirth);

            stmt.executeUpdate();

            

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
    
    private static Date createDate(int year, int month, int day) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        Date date = new Date(calendar.getTime().getTime());
        return date;
    }   
    
    /*
     * Ćw 1.3.1
     */
    private static void insertSalary(Connection dbConnection, int idEmployee, BigDecimal amount, Date since) {

        PreparedStatement stmt = null;
        try {
        	
            stmt = dbConnection
                  .prepareStatement("insert into pensja (id_pracownika,kwota,od) "
                            + "values(?,?,?)");

            stmt.setInt(1, idEmployee);
            stmt.setBigDecimal(2, amount);
            stmt.setDate(3, since);

            stmt.executeUpdate();

            

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
    
    /*
     * Ćw 1.3.2
     */
    private static void insertEmployeeWithSalary(Connection dbConnection, String firstName, 
    			String surname, Date dateOfBirth, BigDecimal amount) {
  
        //dodajemy pracownika
         insertEmployeeV2(dbConnection,firstName,surname,dateOfBirth);
         
         //Pobieramy z bazy id wstawianej osoby
         int idEmployee = getNextEmployeeId(dbConnection);
         
        //Teraz dodajemy pensje
        insertSalary(dbConnection,idEmployee,amount,
        		new Date(Calendar.getInstance().getTime().getTime()));

    }
    
    /*
     * Ćw 1.4.1
     */
    
 // zwraca liste wszystkich pracownikow w bazie
    private static java.util.List<Employee> getEmployeeList(
            Connection dbConnection) {
        java.util.List<Employee> employeeList = new ArrayList<Employee>();

        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("select id,imie,nazwisko,data_urodzenia from pracownik");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	/*
                Employee e = new Employee();
                // pobranie pol po numerze kolumny
                e.setId(rs.getInt(1));
                e.setFirstName(rs.getString(2));
                // pobranie pol po nazwie kolumny
                e.setSurname(rs.getString("nazwisko"));
                e.setDateOfBirth(rs.getDate("data_urodzenia"));
                employeeList.add(e);
                */
                employeeList.add(createEmployee(rs));
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
        return employeeList;
    }
    
    private static Employee createEmployee(ResultSet rs) throws SQLException
    {
    	Employee e = new Employee();
        // pobranie pol po numerze kolumny
        e.setId(rs.getInt(1));
        e.setFirstName(rs.getString(2));
        // pobranie pol po nazwie kolumny
        e.setSurname(rs.getString("nazwisko"));
        e.setDateOfBirth(rs.getDate("data_urodzenia"));
        return e;
    }
    
    private static void printEmployeeList(java.util.List<Employee> employees)

    {
    	for(Employee e : employees)
    	{
    		System.out.println(e.getEmployee());
    	}
    }
    
    /* Ćw 1.5.1 */
    
 // wyszukuje pracownikow w bazie po nazwisku
    private static java.util.List<Employee> findEmployeeBySurname(
            Connection dbConnection, String surname) {
        java.util.List<Employee> employeeList = new ArrayList<Employee>();

        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("select id,imie,nazwisko,data_urodzenia from pracownik where nazwisko like ?");
            stmt.setString(1, surname);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // tworzenie pracownika na podstawie biezacego rekordu
                Employee e = createEmployee(rs);
                employeeList.add(e);
                // tylko dla testu
                System.out.println("Pracownik: " + e.getFirstName() + " "
                        + e.getSurname() + " id: " + e.getId());
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
        return employeeList;
    }
    
    /* Cw 1.6.1 */
    
 // usuniecie pracownika o okreslonym id
    private static void removeEmployee(Connection dbConnection, int employeeId) {
        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("delete from pracownik where id=?");
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
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
    
    /* Cw 1.7.1 */
    
 // pobieranie liczby wszystkich pracownikow w bazie
    private static int getNoOfEmployees(Connection dbConnection) {
        int noOfEmplyees = -1;

        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("select count(*) from pracownik");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                noOfEmplyees = rs.getInt(1);
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
        return noOfEmplyees;
    }
    
    /* Zadanie 1*/
    
    private static EmployeeWithSalaries 
    	createEmployeeWithSalaries(ResultSet rs) throws SQLException
    {
    	EmployeeWithSalaries e = new EmployeeWithSalaries();
        // pobranie pol po numerze kolumny
        e.setId(rs.getInt(1));
        e.setFirstName(rs.getString(2));
        // pobranie pol po nazwie kolumny
        e.setSurname(rs.getString("nazwisko"));
        e.setDateOfBirth(rs.getDate("data_urodzenia"));

        return e;
    }
    
    private static void addSalaryToEmpWithSal(EmployeeWithSalaries e,ResultSet rs) throws SQLException
    {
    	e.addSalary(rs.getBigDecimal(1),rs.getDate(2));
    }
    
    private static EmployeeWithSalaries employeeWithSalaryHistory(
    						Connection dbConnection, int employeeId) {

        PreparedStatement stmt = null;
        EmployeeWithSalaries e = new EmployeeWithSalaries();
        try {
            stmt = dbConnection
                    .prepareStatement("select id,imie,nazwisko,data_urodzenia from pracownik where id like ?");
            stmt.setInt(1, employeeId);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	// tworzenie pracownika na podstawie biezacego rekordu
                e = createEmployeeWithSalaries(rs);
            }          

            //dodawanie do pracownika jego wszystkich pensji
            
         	stmt = dbConnection
                    .prepareStatement("select kwota,od from pensja where id_pracownika like ?");
         	stmt.setInt(1, employeeId);
         	rs = stmt.executeQuery();
         	
            while (rs.next()) {	
                addSalaryToEmpWithSal(e,rs);
            }
        } catch (SQLException ech) {
            ech.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
            // zwalnianie zasobow
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ech) {
                    ech.printStackTrace();
                }
            }
        }
        return e;
    }
    
    /* Zadanie 1.2 */
    
    //...
    
    /* Zadanie 2 */
    
    //...
    
    /* Zadanie 3 */
    
    /*
     * Pomocnicza funkcja, która zwraca Stringa użytkownika o zadanym id
     * wraz z jego ostatnią pensją
     */
    /*
    private static EmployeeWithSalaries employeeWithLatestSalary(
			Connection dbConnection, int employeeId) {

		PreparedStatement stmt = null;
		EmployeeWithSalaries e = new EmployeeWithSalaries();
		try {
			stmt = dbConnection
			    .prepareStatement("select id,imie,nazwisko,data_urodzenia from pracownik where id like ?");
			stmt.setInt(1, employeeId);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next())
			{
			// tworzenie pracownika na podstawie biezacego rekordu
				e = createEmployeeWithSalaries(rs);
			}          
			
			//Wyciągamy tylko najnowszą pensję dla pracownika
			stmt = dbConnection
			    .prepareStatement("SELECT Kwota, od FROM PENSJA where id_pracownika like ? " +
			    		"and  rownum = 1 order by Od desc");
			stmt.setInt(1, employeeId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {	
				addSalaryToEmpWithSal(e,rs);
			}
		} catch (SQLException ech) {
			ech.printStackTrace();
		} finally {
			// w kazdym wypadku jesli stmt nie null to go zamknij -
			// zwalnianie zasobow
			if (stmt != null) {
			try {
				    	stmt.close();
				} catch (SQLException ech) {
				    ech.printStackTrace();
				}
			}	
		}
		return e;
	}
    */
    
    /*Zadanie 2*/
    
    static public boolean lineIsCorrect(String l)
    {
    	//sprawdzamy czy linia jest poprawna
    	
    	String[] dividedLine = l.split(" ");
    	if(dividedLine.length == 5)
    	{
    		String[] dividedTime = dividedLine[2].split("-");
    		if(dividedTime.length == 3)
    		{
    			dividedTime = dividedLine[4].split("-");
    			if(dividedTime.length == 3)
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public static int getNextEmployeeId(Connection dbConnection)
    {
    	int lastEmployeeId = 0;

        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("SELECT max(id) from pracownik");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	lastEmployeeId = rs.getInt(1)+1;
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
        return lastEmployeeId;
    }
    
    // wyszukuje pracownikow w bazie po id
    private static String getStringInfoOfEmployeeById(
            Connection dbConnection, int employeeId) {
    	String result = "";
    	
        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("select imie,nazwisko,data_urodzenia from pracownik where id like ?");
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getString(1) + " " + rs.getString(2) +
            			" " + rs.getString(3);
            }
        } catch (SQLException ech) {
            ech.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
            // zwalnianie zasobow
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ech) {
                    ech.printStackTrace();
                }
            }
        }
        return result;
    }
    /*Pomocnicza funkcja zwracająca listę stringów zawierających dane każdego salary*/
    
    private static ArrayList<String> getStringofSalaryById(
            Connection dbConnection, int employeeId)
    {
    	ArrayList<String> result = new ArrayList<String>();
    	
        PreparedStatement stmt = null;
        try {
            stmt = dbConnection
                    .prepareStatement("select kwota,od from pensja where id_pracownika like ?");
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // tworzenie pracownika na podstawie biezacego rekordu
            	String line = rs.getBigDecimal(1) + " " + rs.getDate(2);
            	result.add(line);
            }
        } catch (SQLException ech) {
            ech.printStackTrace();
        } finally {
            // w kazdym wypadku jesli stmt nie null to go zamknij -
            // zwalnianie zasobow
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ech) {
                    ech.printStackTrace();
                }
            }
        }
    	
    	return result;
    }
}
