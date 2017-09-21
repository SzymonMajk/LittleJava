package pl.edu.agh.kis;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeWithSalaries extends Employee {
    
	java.util.List<Salary> salaries = new ArrayList<Salary>();
	
	public String getSalaries() {
		String salariesString = "";
		for(Salary s : salaries)
		{
			salariesString += s.writeDown();
		}
		return salariesString;
    }
    public void addSalary(BigDecimal m, Date d) throws SQLException {
        salaries.add(new Salary(m.doubleValue(),d));
    }
	
	
    public String getEmployeeWithSalaries()
    {
    	return getEmployee() + " " + getSalaries() + "\n";
    }
    
}

class Salary{
	double money = 0;
	Date since;
	
	String writeDown(){
		return money + " " + since + " ";
	}
	
	Salary(double m, Date sin)
	{
		money = m;
		since = sin;
	}
}