package pl.edu.agh.kis;

import java.util.Map;

/**
 * Interfejs ma zapewni� metod�, dzi�ki kt�rej implementuj�cego go klasy, b�d� w stanie
 * zapisywa� w sobie ustalony spos�b, wszystkie informacje, podawane jako paramtr odpowiedniej
 * metody, spos�b sk�adowania oraz jego obs�uga nale�y klasy implementuj�cej interfejs
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public interface StoreBusInfo {

	/**
	 * Zadaniem funkcji implementuj�cej b�dzie zesk�adowanie informacji podanych jako
	 * parametr, spos�b sk�adowania b�dzie zale�ny od podanej implementacji
	 * @param allInformations String zawieraj�cy wszystkie informacje, wcze�niej wyodr�bnione
	 */
	public void storeInfo(Map<String,String> allInformations);
}
