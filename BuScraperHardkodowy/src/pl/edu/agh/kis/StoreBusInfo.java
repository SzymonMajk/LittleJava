package pl.edu.agh.kis;

import java.util.Map;

/**
 * Interfejs ma zapewni� metod�, dzi�ki kt�rej implementuj�cego go klasy, b�d� w stanie
 * zapisywa� w sobie ustalony spos�b, wszystkie informacje, podawane jako parametr odpowiedniej
 * metody, spos�b sk�adowania oraz jego obs�uga nale�y od klasy implementuj�cej interfejs.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface StoreBusInfo {

	/**
	 * Zadaniem funkcji implementuj�cej b�dzie zesk�adowanie informacji podanych jako
	 * parametr. Sama ustala cel, format oraz spos�b sk�adowania danych.
	 * @param allInformations mapa w kt�rej klucz okre�la pochodzenie oraz przeznaczenie danych
	 *        zawartych w warto�ci pod kluczem
	 */
	public void storeInfo(Map<String,String> allInformations);
}
