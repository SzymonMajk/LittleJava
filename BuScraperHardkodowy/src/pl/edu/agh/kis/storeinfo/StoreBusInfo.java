package pl.edu.agh.kis.storeinfo;

import java.util.Map;

/**
 * Interfejs ma zapewni� metod�, dzi�ki kt�rej implementuj�cego go klasy, b�d� w stanie
 * zapisywa� w ustalony spos�b, wszystkie informacje, zebrane w posta� mapy podanej jako 
 * parametr metody storeInfo. Sam spos�b sk�adowania oraz obs�ugiwanie sytuacji 
 * problematycznych zale�y od implementacji interfejsu.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface StoreBusInfo {

	/**
	 * Zadaniem funkcji implementuj�cej b�dzie zesk�adowanie informacji podanych w mapie
	 * z argumentu. Implementacja sama musi przeprowadzi� walidacj� danych, a przypadku
	 * ich poprawno�ci zapisa� je w ustalony przez siebie spos�b.
	 * @param allInformations mapa w kt�rej klucz okre�la z jakiego typu danymi mamy
	 * 		do czynienia implementacja StoreBusInfo rozpoznaje informacje po warto�ci
	 * 		tego klucza, natomiast same informacje znajduj� si� w warto�ci dla danego klucza.
	 */
	public void storeInfo(Map<String,String> allInformations);
}
