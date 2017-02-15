package pl.edu.agh.kis.storeinfo;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckLineNumber implements CheckInformations {

	/**
	 * 
	 */
	@Override
	public boolean checkInformation(String infoLineNumber)
	{
		return (infoLineNumber != null && !infoLineNumber.equals("") 
				&& infoLineNumber.matches("^\\d{1,3}$"));
	}

}
