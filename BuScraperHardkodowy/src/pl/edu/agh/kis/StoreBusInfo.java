package pl.edu.agh.kis;

import java.util.Map;

/**
 * Interfejs ma zapewniæ metodê, dziêki której implementuj¹cego go klasy, bêd¹ w stanie
 * zapisywaæ w sobie ustalony sposób, wszystkie informacje, podawane jako parametr odpowiedniej
 * metody, sposób sk³adowania oraz jego obs³uga nale¿y od klasy implementuj¹cej interfejs.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface StoreBusInfo {

	/**
	 * Zadaniem funkcji implementuj¹cej bêdzie zesk³adowanie informacji podanych jako
	 * parametr. Sama ustala cel, format oraz sposób sk³adowania danych.
	 * @param allInformations mapa w której klucz okreœla pochodzenie oraz przeznaczenie danych
	 *        zawartych w wartoœci pod kluczem
	 */
	public void storeInfo(Map<String,String> allInformations);
}
