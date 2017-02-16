package pl.edu.agh.kis.storeinfo;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla sprawdzania poprawno�ci numeru
 * linii.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckLineNumber implements CheckInformations {

	/**
	 * Funkcja ma za zadanie sprawdzi� czy otrzymane dane mog� zosta� zapisane jako
	 * numer linii. Sprawdzamy czy otrzymana dana nie jest null'em, czy
	 * nie jest pusta oraz czy sk�ada si� z minimalnie jednej i maksymalnie trzech cyfr.
	 * @param infoLineNumber dane domniemane o mo�liwo�� zostania zapisanymi jako numer
	 * 		linii.
	 * @return warto�� prawdy w przypadku spe�nienia za�o�e� numeru linii lub warto�� 
	 * fa�szu, je�eli dane nie przejd� walidacji.
	 */
	@Override
	public boolean checkInformation(String infoLineNumber)
	{
		return (infoLineNumber != null && !infoLineNumber.equals("") 
				&& infoLineNumber.matches("^\\d{1,3}$"));
	}

}
