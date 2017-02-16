package pl.edu.agh.kis.storeinfo;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla sprawdzania poprawno�ci nazwy
 * kierunku.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckDirection implements CheckInformations {

	/**
	 * Funkcja ma za zadanie sprawdzi� czy otrzymane dane mog� zosta� zapisane jako
	 * kierunek linii. Sprawdzamy czy otrzymana dana nie jest null'em, czy
	 * nie jest pusta oraz czy zawiera wyra�enie "Do".
	 * @param infoDirection dane domniemane o mo�liwo�� zostania zapisanymi jako kierunek
	 * 		linii.
	 * @return warto�� prawdy w przypadku spe�nienia za�o�e� kierunku linii lub warto�� 
	 * fa�szu, je�eli dane nie przejd� walidacji.
	 */
	@Override
	public boolean checkInformation(String infoDirection) 
	{
		return (infoDirection != null && !infoDirection.equals("") 
				&& infoDirection.contains("Do"));
	}

}
