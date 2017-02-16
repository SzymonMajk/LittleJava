package pl.edu.agh.kis.configuration;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udost�pnia metod� pozwalaj�c� na sprawdzenie poprawno�ci
 * szczeg�u potrzebnego przy tworzeniu obiektu Task.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class TaskDetailDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdzi� poprawno�� szczeg�u potrzebnego przy tworzeniu
	 * obiektu Task. Sprawdza czy nie mamy do czynienia z null'em.
	 * @param lineOnset s�owo kluczowe szczeg�u obiektu Task.
	 * @param lineContent zawarto�� dla danego s�owa kluczowego obiektu Task.
	 * @param configurator obiekt Configurator, w kt�rym zostan� zesk�adowane szczeg�y
	 * 		wyszukania.
	 * @return zwraca prawd� je�eli poprawne dane zosta�y w poprawny spos�b zapisane w obiekcie
	 * 		Configurator, zwr�ci fa�sz je�eli dane nie przesz�y walidacji.
	 */
	@Override
	public boolean saveConfigurationLineContent(String lineOnset, String lineContent, 
			Configurator configurator) {

		try {
			configurator.addTasksDetail(lineOnset, lineContent);
		} catch (NullPointerException e)
		{
			return false;
		}
		
		return true;
	}

}
