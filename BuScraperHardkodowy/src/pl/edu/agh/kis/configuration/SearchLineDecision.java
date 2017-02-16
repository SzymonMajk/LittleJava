package pl.edu.agh.kis.configuration;

/**
 * Implementacja wspieraj¹ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udostêpnia metodê pozwalaj¹c¹ na sprawdzenie poprawnoœci linijki
 * zawieraj¹cej dane wyszukania oraz wpisanie jej do kolejki linijek, je¿eli oka¿e siê
 * poprawna.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class SearchLineDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdziæ poprawnoœæ linijki wyszukañ. W tym celu sprawdza
	 * czy posiada ona dok³adnie szeœæ znaków dwukropka oraz czy nie jest null'em.
	 * @param lineOnset s³owo kluczowe oznaczaj¹ce linijkê wyszukania.
	 * @param lineContent zawartoœæ dla danego s³owa kluczowego wyszukania.
	 * @param configurator obiekt Configurator, w którym zostan¹ zesk³adowane poprawne
	 * 		linijki wyszukania
	 * @return zwraca prawdê je¿eli poprawne linijki zosta³y w poprawny sposób zesk³adowane
	 * 		fa³sz je¿eli okaza³y siê niepoprawne.
	 */
	@Override
	public boolean saveConfigurationLineContent(String lineOnset, String lineContent, 
			Configurator configurator) {

		try {			
			String[] splited = lineContent.split(":");
			
			if(splited.length == 6)
			{
				configurator.addLineToSearch(lineContent);
			}
			else
			{
				return false;
			}
			
		} catch (NullPointerException e)
		{
			return false;
		}
		
		return true;
	}

}
