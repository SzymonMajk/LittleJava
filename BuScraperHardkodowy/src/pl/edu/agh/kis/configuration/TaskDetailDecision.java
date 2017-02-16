package pl.edu.agh.kis.configuration;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udostêpnia metodê pozwalaj¹c¹ na sprawdzenie poprawnoœci
 * szczegó³u potrzebnego przy tworzeniu obiektu Task.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class TaskDetailDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdziæ poprawnoœæ szczegó³u potrzebnego przy tworzeniu
	 * obiektu Task. Sprawdza czy nie mamy do czynienia z null'em.
	 * @param lineOnset s³owo kluczowe szczegó³u obiektu Task.
	 * @param lineContent zawartoœæ dla danego s³owa kluczowego obiektu Task.
	 * @param configurator obiekt Configurator, w którym zostan¹ zesk³adowane szczegó³y
	 * 		wyszukania.
	 * @return zwraca prawdê je¿eli poprawne dane zosta³y w poprawny sposób zapisane w obiekcie
	 * 		Configurator, zwróci fa³sz je¿eli dane nie przesz³y walidacji.
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
