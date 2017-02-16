package pl.edu.agh.kis.configuration;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udost�pnia metod� pozwalaj�c� na sprawdzenie poprawno�ci
 * wyra�enia XPath.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class XPathDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdzi� poprawno�� wyra�enia XPath. Sprawdza czy nie mamy 
	 * do czynienia z null'em.
	 * @param lineOnset s�owo kluczowe wyra�enia XPath
	 * @param lineContent wyra�enie XPath dla danego s�owa kluczowego XPath
	 * @param configurator obiekt Configurator, w kt�rym zostan� zesk�adowane poprawne dane
	 * 		sprawdzone uprzednio przez funkcj�.
	 * @return zwraca prawd� je�eli poprawne dane zosta�y w poprawny spos�b zapisane w obiekcie
	 * 		Configurator, zwr�ci fa�sz je�eli dane nie przesz�y walidacji.
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
