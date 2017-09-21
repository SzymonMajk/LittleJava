package pl.edu.agh.kis.crawler;

/**
 * Interfejs robota do wyciągania treści stron, w najogólniejszej idei robot ma
 * posiadać jakiegoś typu kontener na linki, potrafić wyciągać informacje przy pomocy
 * linków oraz umieć obsłużyć odpowiednio swoją własną główną pętle - czyli obsłużyć
 * wyciąganie linków oraz ich analizowanie
 * @author Szymon Majkut
 *
 */
public interface CrawlerV2 {

	/**
	 * Zadaniem funkcji implementującej jest wykorzystanie zawartości ze strony
	 * @param link - element pobrany ze strony w postaci ciągu znaków
	 */
	void useFindedElement(String element);
	
	/**
	 * Zadaniem funkcji jest obsługa głównej pętli programu, musi zapewnić
	 * kontrolę nad pobieraniem nowy linków oraz wykorzystywaniem tych
	 * znajdujących się już w kontenerze Crawlera
	 * @param startLink - adres otrzymany jako parametr programu
	 */
	void startGetting(String startLink);
	
	/**
	 * Zadaniem funkcji jest  wyciągnięcie wszystkich linków ze strony podanej w linku
	 * oraz zawartości określanej przez samego Crawlera
	 * @param link - adres strony do przeszukania
	 */
	void searchPage(String link);
}
