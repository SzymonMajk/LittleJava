package pl.edu.agh.kis.crawler;

/*
 * Interfejs rejestru odwiedzonych stron, 
 * ktory pozwala sprawdzic czy strona o danym adresie byla juz odwiedzana czy nie.
 */
public interface VisitedPages {
    /**
     * Sprawdza czy dana strona byla juz odwiedzana.
     * 
     * @param pageString adres strony do sprawdzenia
     * @return true - strona byla juz wczesniej odwiedzana, false -
     *         w przeciwnym razie
     */ 
     boolean pageAlreadyVisited(String pageString);

    /**
     * Rejestruje strone o podanym adresie jako odwiedzona.
     *
     * @param pageString adres odwiedzonej strony
     */
    void addVisitedPage(String pageString);
}