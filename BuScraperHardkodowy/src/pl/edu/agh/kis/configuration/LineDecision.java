package pl.edu.agh.kis.configuration;

/**
 * Interfejs zapewniaj¹cy mo¿liwoœæ skorzystania z wzorca projektowego strategii
 * w obiekcie Configurator. Jego implementacje zapewniaj¹ metodê saveConfigurationLineContent,
 * która pozwala na sprawdzenie poprawnoœci zawartoœci danego s³owa kluczowego w zale¿noœci
 * od wartoœci tego s³owa kluczowego.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface LineDecision {

	/**
	 * Funkcja ma za zadanie sprawdziæ poprawnoœæ danych otrzymanych za s³owem kluczowym,
	 * a gdy oka¿¹ siê poprawne w sposób zale¿ny od implementacji zesk³adowaæ dane w pamiêci
	 * obiektu Configurator.
	 * @param lineOnset s³owo kluczowe.
	 * @param lineContent zawartoœæ dla danego s³owa kluczowego, w pliku konfiguracyjnym,
	 * 		to co znajduje siê po znaku =.
	 * @param configurator obiekt Configurator, w którym zostan¹ zesk³adowane poprawne dane
	 * 		sprawdzone uprzednio przez funkcjê.
	 * @return zwraca prawdê je¿eli poprawne dane zosta³y w poprawny sposób zapisane w obiekcie
	 * 		Configurator, zwróci fa³sz je¿eli dane nie przesz³y walidacji.
	 */
	public boolean saveConfigurationLineContent(String lineOnset, String lineContent,
			Configurator configurator);
}