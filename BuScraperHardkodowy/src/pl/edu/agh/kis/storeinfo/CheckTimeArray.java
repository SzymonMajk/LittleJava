package pl.edu.agh.kis.storeinfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckTimeArray implements CheckInformations {

	/**
	 * 
	 * @param rawLine
	 * @return
	 */
	private boolean checkIfOnlyDigits(String timeLines)
	{
		int colonNumber = timeLines.split("\n").length;
		
		Pattern pattern = Pattern.compile("[^0-9a-zA-Z\\s:]");
		Matcher matcher = pattern.matcher(timeLines);
		
		return (!matcher.find() 
				&& (colonNumber == 24 || colonNumber == 8));
	}
	
	/**
	 * 
	 */
	@Override
	public boolean checkInformation(String informations) 
	{
		return (informations != null && checkIfOnlyDigits(informations));
	}
}
