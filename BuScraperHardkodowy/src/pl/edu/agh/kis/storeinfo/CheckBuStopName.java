package pl.edu.agh.kis.storeinfo;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckBuStopName implements CheckInformations {
	
	/**
	 * 
	 */
	@Override
	public boolean checkInformation(String infoBuStopName)
	{
		return (infoBuStopName != null && !infoBuStopName.equals(""));
	}
}