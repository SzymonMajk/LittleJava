package pl.edu.agh.kis.rozdz6;
/**: Object/pl/edu/agh/kis/rozdz6/ConnectionManager */

/**
 * Klasa zarządza obiekatami Connection, które wykorzystują
 * wzorzec Singletonu
 * @author Szymon Majkut
 *
 */
public class ConnetctionManager {

	Connection[] conn = {Connection.getConnection(),
			Connection.getConnection(), Connection.getConnection()};
	
	public static void main(String[] args) 
	{
		ConnetctionManager cM = new ConnetctionManager();
		
		for(int i = 0; ; i++)
		{
			if(i >= cM.conn.length)
			{
				System.out.println("Koniec obiektów Connection!");
				break;
			}
			
			System.out.println(cM.conn[i].a);
			
		}

	}

}
