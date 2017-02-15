package pl.edu.agh.kis.storeinfo;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckDirection implements CheckInformations {

	/**
	 * 
	 */
	@Override
	public boolean checkInformation(String infoDirection) 
	{
		return (infoDirection != null && !infoDirection.equals("") 
				&& infoDirection.contains("Do"));
	}

}
