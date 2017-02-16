package pl.edu.agh.kis.storeinfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla sprawdzania poprawnoœci linii
 * zawieraj¹cej poprawn¹ liniê z czasami.
 * @author Szymon Majkut
 * @version %I%, %G%
 */
public class CheckTimeArray implements CheckInformations {


	/**
	 Sprawdzamy czy po rozdzieleniu wzglêdem znaku dwukropka
	 * iloœæ utworzonych elementów odpowiada liczbie 24 lub liczbie 8, oraz czy w danych
	 * znajduje siê znak inny ni¿ znak bia³y lub cyfra.
	 * @param informations dane domniemane o mo¿liwoœæ zostania zapisanymi jako poprawna
	 * 		linia czasów.
	 * @return wartoœæ prawdy w przypadku spe³nienia za³o¿eñ linii czasów lub wartoœæ 
	 * fa³szu, je¿eli dane nie przejd¹ walidacji.
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
	 * Funkcja ma za zadanie sprawdziæ czy otrzymane dane mog¹ zostaæ zapisane jako
	 * poprawna linia czasów. Sprawdzamy czy po rozdzieleniu wzglêdem znaku dwukropka
	 * iloœæ utworzonych elementów odpowiada liczbie 24 lub liczbie 8, oraz czy w danych
	 * znajduje siê znak inny ni¿ znak bia³y lub cyfra.
	 * @param informations dane domniemane o mo¿liwoœæ zostania zapisanymi jako poprawna
	 * 		linia czasów.
	 * @return wartoœæ prawdy w przypadku spe³nienia za³o¿eñ linii czasów lub wartoœæ 
	 * fa³szu, je¿eli dane nie przejd¹ walidacji.
	 */
	@Override
	public boolean checkInformation(String informations) 
	{
		return (informations != null && checkIfOnlyDigits(informations));
	}
}
