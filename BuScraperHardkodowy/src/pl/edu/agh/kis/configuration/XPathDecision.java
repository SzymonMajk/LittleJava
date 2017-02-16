package pl.edu.agh.kis.configuration;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udostêpnia metodê pozwalaj¹c¹ na sprawdzenie poprawnoœci
 * wyra¿enia XPath.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class XPathDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdziæ poprawnoœæ wyra¿enia XPath. Sprawdza czy nie mamy 
	 * do czynienia z null'em.
	 * @param lineOnset s³owo kluczowe wyra¿enia XPath
	 * @param lineContent wyra¿enie XPath dla danego s³owa kluczowego XPath
	 * @param configurator obiekt Configurator, w którym zostan¹ zesk³adowane poprawne dane
	 * 		sprawdzone uprzednio przez funkcjê.
	 * @return zwraca prawdê je¿eli poprawne dane zosta³y w poprawny sposób zapisane w obiekcie
	 * 		Configurator, zwróci fa³sz je¿eli dane nie przesz³y walidacji.
	 */
	@Override
	public boolean saveConfigurationLineContent(String lineOnset,String lineContent,
			Configurator configurator) {
		
		try {
			configurator.addXPath(lineOnset, lineContent);
		} catch (NullPointerException e)
		{
			return false;
		}
		
		return true;
	}

}
