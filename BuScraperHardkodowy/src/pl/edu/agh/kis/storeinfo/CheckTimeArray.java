package pl.edu.agh.kis.storeinfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla sprawdzania poprawno�ci linii
 * zawieraj�cej poprawn� lini� z czasami.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckTimeArray implements CheckInformations {


	/**
	 Sprawdzamy czy po rozdzieleniu wzgl�dem znaku dwukropka
	 * ilo�� utworzonych element�w odpowiada liczbie 24 lub liczbie 8, oraz czy w danych
	 * znajduje si� znak inny ni� znak bia�y lub cyfra.
	 * @param informations dane domniemane o mo�liwo�� zostania zapisanymi jako poprawna
	 * 		linia czas�w.
	 * @return warto�� prawdy w przypadku spe�nienia za�o�e� linii czas�w lub warto�� 
	 * fa�szu, je�eli dane nie przejd� walidacji.
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
	 * Funkcja ma za zadanie sprawdzi� czy otrzymane dane mog� zosta� zapisane jako
	 * poprawna linia czas�w. Sprawdzamy czy po rozdzieleniu wzgl�dem znaku dwukropka
	 * ilo�� utworzonych element�w odpowiada liczbie 24 lub liczbie 8, oraz czy w danych
	 * znajduje si� znak inny ni� znak bia�y lub cyfra.
	 * @param informations dane domniemane o mo�liwo�� zostania zapisanymi jako poprawna
	 * 		linia czas�w.
	 * @return warto�� prawdy w przypadku spe�nienia za�o�e� linii czas�w lub warto�� 
	 * fa�szu, je�eli dane nie przejd� walidacji.
	 */
	@Override
	public boolean checkInformation(String informations) 
	{
		return (informations != null && checkIfOnlyDigits(informations));
	}
}
