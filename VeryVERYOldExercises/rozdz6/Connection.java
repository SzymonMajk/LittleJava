package pl.edu.agh.kis.rozdz6;
/**: Object/pl/edu/agh/kis/rozdz6/Connection */

public class Connection {

	int a = 5;
	
	public static Connection getConnection()
	{
		return new Connection();
	}
	
	private Connection()
	{
		//nic
	}
}
