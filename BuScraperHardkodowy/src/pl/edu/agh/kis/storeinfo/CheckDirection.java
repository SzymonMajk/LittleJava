package pl.edu.agh.kis.storeinfo;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla sprawdzania poprawnoœci nazwy
 * kierunku.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckDirection implements CheckInformations {

	/**
	 * Funkcja ma za zadanie sprawdziæ czy otrzymane dane mog¹ zostaæ zapisane jako
	 * kierunek linii. Sprawdzamy czy otrzymana dana nie jest null'em, czy
	 * nie jest pusta oraz czy zawiera wyra¿enie "Do".
	 * @param infoDirection dane domniemane o mo¿liwoœæ zostania zapisanymi jako kierunek
	 * 		linii.
	 * @return wartoœæ prawdy w przypadku spe³nienia za³o¿eñ kierunku linii lub wartoœæ 
	 * fa³szu, je¿eli dane nie przejd¹ walidacji.
	 */
	@Override
	public boolean checkInformation(String infoDirection) 
	{
		return (infoDirection != null && !infoDirection.equals("") 
				&& infoDirection.contains("Do"));
	}

}
