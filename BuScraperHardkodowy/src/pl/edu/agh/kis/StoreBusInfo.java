package pl.edu.agh.kis;

import java.util.Map;

/**
 * Interfejs ma zapewniæ metodê, dziêki której implementuj¹cego go klasy, bêd¹ w stanie
 * zapisywaæ w sobie ustalony sposób, wszystkie informacje, podawane jako paramtr odpowiedniej
 * metody, sposób sk³adowania oraz jego obs³uga nale¿y klasy implementuj¹cej interfejs
 * @author Szymon Majkut
 * @version 1.3
 *
 */
public interface StoreBusInfo {

	/**
	 * Zadaniem funkcji implementuj¹cej bêdzie zesk³adowanie informacji podanych jako
	 * parametr, sposób sk³adowania bêdzie zale¿ny od podanej implementacji
	 * @param allInformations String zawieraj¹cy wszystkie informacje, wczeœniej wyodrêbnione
	 */
	public void storeInfo(Map<String,String> allInformations);
}
