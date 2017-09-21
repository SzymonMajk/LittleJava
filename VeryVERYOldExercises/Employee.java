package pl.edu.agh.kis;

import java.sql.*;

public class Employee {
    protected int id;
    protected String firstName;
    protected String surname;
    protected Date dateOfBirth;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }    
    public String getEmployee()
    {
    	return firstName + " " + surname + " " +
    			id + " " + dateOfBirth;
    }
}