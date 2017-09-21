package pl.edu.agh.kis.crawler;

/**
 * Interfejs robota do wyciągania treści stron, w najogólniejszej idei robot ma
 * posiadać jakiegoś typu kontener na linki, potrafić wyciągać informacje przy pomocy
 * linków oraz umieć obsłużyć odpowiednio swoją własną główną pętle - czyli obsłużyć
 * wyciąganie linków oraz ich analizowanie
 * @author Szymon Majkut
 *
 */
public interface Crawler {

	/**
	 * Zadaniem funkcji implementującej jest wykorzystanie zawartości
	 * pobranej z otrzymanego w parametrze linku
	 * @param link - adres otrzymany z kontenera
	 */
	void useLinkContent(String link);
	
	/**
	 * Zadaniem funkcji jest obsługa głównej pętli programu, musi zapewnić
	 * kontrolę nad pobieraniem nowy linków oraz wykorzystywaniem tych
	 * znajdujących się już w kontenerze Crawlera
	 * @param startLink - adres otrzymany jako parametr programu
	 */
	void startGetting(String startLink);
	
	/**
	 * Zadaniem funkcji jest jak najszybsze wyciągnięcie wszystkich linków
	 * z podanego linku np. w celu wstawienia ich do kontenera lub pobrania zawartości
	 * @param link - adres otrzymany z kontenera
	 * @return String, w którym znajdują się wszystkie linki, znalezione w danym linku
	 */
	String getAdditionalLinks(String link);
}
