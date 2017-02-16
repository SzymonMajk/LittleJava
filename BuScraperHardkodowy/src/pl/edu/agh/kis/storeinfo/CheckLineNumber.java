package pl.edu.agh.kis.storeinfo;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla sprawdzania poprawnoœci numeru
 * linii.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckLineNumber implements CheckInformations {

	/**
	 * Funkcja ma za zadanie sprawdziæ czy otrzymane dane mog¹ zostaæ zapisane jako
	 * numer linii. Sprawdzamy czy otrzymana dana nie jest null'em, czy
	 * nie jest pusta oraz czy sk³ada siê z minimalnie jednej i maksymalnie trzech cyfr.
	 * @param infoLineNumber dane domniemane o mo¿liwoœæ zostania zapisanymi jako numer
	 * 		linii.
	 * @return wartoœæ prawdy w przypadku spe³nienia za³o¿eñ numeru linii lub wartoœæ 
	 * fa³szu, je¿eli dane nie przejd¹ walidacji.
	 */
	@Override
	public boolean checkInformation(String infoLineNumber)
	{
		return (infoLineNumber != null && !infoLineNumber.equals("") 
				&& infoLineNumber.matches("^\\d{1,3}$"));
	}

}
