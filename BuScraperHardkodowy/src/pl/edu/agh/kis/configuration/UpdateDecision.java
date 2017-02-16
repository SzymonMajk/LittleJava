package pl.edu.agh.kis.configuration;

import java.util.Locale;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udost�pnia metod� pozwalaj�c� na sprawdzenie poprawno�ci
 * decyzji dotycz�cej aktualizacji.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class UpdateDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdzi� poprawno�� decyzji dotycz�cej aktualizacji. Sprowadza
	 * s�owo podane w drugim argumencie do ma�ych liter i por�wnuje z napisem true.
	 * @param lineOnset s�owo kluczowe decyzji dotycz�cej aktualizacji.
	 * @param lineContent zawarto�� dla s�owa kluczowego dotycz�cego aktualizacji.
	 * @param configurator obiekt Configurator, w kt�rym zostan� zesk�adowane poprawne dane
	 * 		sprawdzone uprzednio przez funkcj�.
	 * @return zwraca prawd� je�eli poprawne dane zosta�y w poprawny spos�b zapisane w obiekcie
	 * 		Configurator, zwr�ci fa�sz je�eli dane nie przesz�y walidacji.
	 */
	@Override
	public boolean saveConfigurationLineContent(String lineOnset, String lineContent, 
			Configurator configurator) {

		try {
			if(lineContent.toLowerCase(Locale.getDefault()).equals("true"))
				configurator.setUpdateData(true);
			else
				configurator.setUpdateData(false);
		} catch (NullPointerException e)
		{
			return false;
		}
		
		return true;
	}

}
