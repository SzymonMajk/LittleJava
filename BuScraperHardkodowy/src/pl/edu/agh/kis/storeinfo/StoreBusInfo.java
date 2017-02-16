package pl.edu.agh.kis.storeinfo;

import java.util.Map;

/**
 * Interfejs ma zapewniæ metodê, dziêki której implementuj¹cego go klasy, bêd¹ w stanie
 * zapisywaæ w ustalony sposób, wszystkie informacje, zebrane w postaæ mapy podanej jako 
 * parametr metody storeInfo. Sam sposób sk³adowania oraz obs³ugiwanie sytuacji 
 * problematycznych zale¿y od implementacji interfejsu.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface StoreBusInfo {

	/**
	 * Zadaniem funkcji implementuj¹cej bêdzie zesk³adowanie informacji podanych w mapie
	 * z argumentu. Implementacja sama musi przeprowadziæ walidacjê danych, a przypadku
	 * ich poprawnoœci zapisaæ je w ustalony przez siebie sposób.
	 * @param allInformations mapa w której klucz okreœla z jakiego typu danymi mamy
	 * 		do czynienia implementacja StoreBusInfo rozpoznaje informacje po wartoœci
	 * 		tego klucza, natomiast same informacje znajduj¹ siê w wartoœci dla danego klucza.
	 */
	public void storeInfo(Map<String,String> allInformations);
}
