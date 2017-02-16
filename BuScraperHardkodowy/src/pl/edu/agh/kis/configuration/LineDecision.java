package pl.edu.agh.kis.configuration;

/**
 * Interfejs zapewniaj�cy mo�liwo�� skorzystania z wzorca projektowego strategii
 * w obiekcie Configurator. Jego implementacje zapewniaj� metod� saveConfigurationLineContent,
 * kt�ra pozwala na sprawdzenie poprawno�ci zawarto�ci danego s�owa kluczowego w zale�no�ci
 * od warto�ci tego s�owa kluczowego.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdzi� poprawno�� danych otrzymanych za s�owem kluczowym,
	 * a gdy oka�� si� poprawne w spos�b zale�ny od implementacji zesk�adowa� dane w pami�ci
	 * obiektu Configurator.
	 * @param lineOnset s�owo kluczowe.
	 * @param lineContent zawarto�� dla danego s�owa kluczowego, w pliku konfiguracyjnym,
	 * 		to co znajduje si� po znaku =.
	 * @param configurator obiekt Configurator, w kt�rym zostan� zesk�adowane poprawne dane
	 * 		sprawdzone uprzednio przez funkcj�.
	 * @return zwraca prawd� je�eli poprawne dane zosta�y w poprawny spos�b zapisane w obiekcie
	 * 		Configurator, zwr�ci fa�sz je�eli dane nie przesz�y walidacji.
	 */
	public boolean saveConfigurationLineContent(String lineOnset, String lineContent,
			Configurator configurator);
}