package pl.edu.agh.kis.configuration;

/**
 * Implementacja wspieraj�ca wzorzec strategii dla zdobywania danych z pliku konfiguracyjnego
 * w obiekcie Configurator. Udost�pnia metod� pozwalaj�c� na sprawdzenie poprawno�ci linijki
 * zawieraj�cej dane wyszukania oraz wpisanie jej do kolejki linijek, je�eli oka�e si�
 * poprawna.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class SearchLineDecision implements LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdzi� poprawno�� linijki wyszuka�. W tym celu sprawdza
	 * czy posiada ona dok�adnie sze�� znak�w dwukropka oraz czy nie jest null'em.
	 * @param lineOnset s�owo kluczowe oznaczaj�ce linijk� wyszukania.
	 * @param lineContent zawarto�� dla danego s�owa kluczowego wyszukania.
	 * @param configurator obiekt Configurator, w kt�rym zostan� zesk�adowane poprawne
	 * 		linijki wyszukania
	 * @return zwraca prawd� je�eli poprawne linijki zosta�y w poprawny spos�b zesk�adowane
	 * 		fa�sz je�eli okaza�y si� niepoprawne.
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
