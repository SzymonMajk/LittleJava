package pl.edu.agh.kis;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Cw133 {
    private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWD = "";

    public static void main(String[] args) throws Exception {
        // wczytanie sterownika bazy danych (z pliku h2-[wersja].jar)
        Class.forName("org.h2.Driver");

        Connection conn = null;        
        int N = 0;
        
        if(Integer.parseInt(args[0]) > 0)
        {
        	N = Integer.parseInt(args[0]);
        }
        // kod aplikacji
        //insertEmployee(conn, "Jan", "Kowalski");
        // wersja 2
        Date dateOfBirth = createDate(1990, 4, 21);

        /*Do ćwiczenia 1.3.3*/
        
        //Wariant 1
        long timeFirst = GregorianCalendar.getInstance().getTimeInMillis();
       
        //caly czas w petli
        for(int i = 0; i < N; i++)
        {
	        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
	        insertEmployeeWithSalary(conn,"Basia","Stanczyk",dateOfBirth,5+i,new BigDecimal(5235.32));
    
	        conn.close();
        }
        //i sprawdzamy czas
        timeFirst = GregorianCalendar.getInstance().getTimeInMillis() - timeFirst;
        
        //Wariant 2
        long timeSecond = GregorianCalendar.getInstance().getTimeInMillis();
        
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
        
        //i w petli dodajemy
        for(int i = 0; i < N; i++)
        {
	        insertEmployeeWithSalary(conn,"Basia","Stanczyk",dateOfBirth,5+i,new BigDecimal(5235.32));
        }
        conn.close();
        
        //i sprwadzamy czas
        timeSecond = GregorianCalendar.getInstance().getTimeInMillis() - timeSecond;
        /*-------------------*/
        
        System.out.println("Wariant 1 zajął - " + timeFirst);
        System.out.println("Wariant 2 zajął - " + timeSecond);

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
    
    private static void insertEmployeeWithSalary(Connection dbConnection, String firstName, 
    			String surName, Date dateOfBirth, int idEmployee, BigDecimal amount) {
  
        //dodajemy pracownika
         insertEmployeeV2(dbConnection,firstName,surName,dateOfBirth);

        //Teraz dodajemy pensje
        insertSalary(dbConnection,idEmployee,amount,
        		new Date(Calendar.getInstance().getTime().getTime()));

    }
}
