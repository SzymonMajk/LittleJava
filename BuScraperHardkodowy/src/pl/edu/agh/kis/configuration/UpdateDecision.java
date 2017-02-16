package pl.edu.agh.kis.configuration;

import java.util.Locale;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udostêpnia metodê pozwalaj¹c¹ na sprawdzenie poprawnoœci
 * decyzji dotycz¹cej aktualizacji.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class UpdateDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdziæ poprawnoœæ decyzji dotycz¹cej aktualizacji. Sprowadza
	 * s³owo podane w drugim argumencie do ma³ych liter i porównuje z napisem true.
	 * @param lineOnset s³owo kluczowe decyzji dotycz¹cej aktualizacji.
	 * @param lineContent zawartoœæ dla s³owa kluczowego dotycz¹cego aktualizacji.
	 * @param configurator obiekt Configurator, w którym zostan¹ zesk³adowane poprawne dane
	 * 		sprawdzone uprzednio przez funkcjê.
	 * @return zwraca prawdê je¿eli poprawne dane zosta³y w poprawny sposób zapisane w obiekcie
	 * 		Configurator, zwróci fa³sz je¿eli dane nie przesz³y walidacji.
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
