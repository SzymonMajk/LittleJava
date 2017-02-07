package pl.edu.agh.kis;

/**
 * Zadaniem interfejsu jest udostêpniæ funkcje pobrania zawartoœci, dodania
 * zawartoœci oraz sprawdzenia stanu synchronizowanego
 * bufora ( pusty/pe³ny), na którym oprzemy metodê producent-konsument pomocn¹
 * przy wydobywaniu danych z internetu.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public interface PagesBuffer {

	/**
	 * Implementacja ma zwróciæ informacjê czy bufor jest pusty.
	 * @return informacja logiczna czy bufor jest pusty
	 */
	public boolean isEmpty();
	
	/**
	 * Implementacja ma zwróciæ informacjê czy bufor jest pe³ny.
	 * @return informacja logiczna czy bufor jest pe³ny
	 */
	public boolean isFull();
	
	/**
	 * Implementacja ma za zadanie dodaæ stronê do niezape³nionego bufora, u¿ytkonik
	 * jest zobligowany do wczeœniejszego wykonania implementacji metody isFull().
	 * @throws InterruptedException wyj¹tek pojawia siê przy próbie nieprawid³owego wybudzenia
	 * @param pageHTML Kod Ÿród³owy strony HTML, który umieszczamy w niezape³nionym buforze
	 */
	public void addPage(String pageHTML) throws InterruptedException;
	
	/**
	 * Implementacja ma za zadanie pobraæ oraz usun¹æ stronê z niepustego bufora, u¿ytkonik
	 * jest zobligowany do wczeœniejszego wykonania implementacji metody isEmpty().
	 * @throws InterruptedException wyj¹tek pojawia siê przy próbie nieprawid³owego wybudzenia
	 * @return Kod Ÿród³owy strony HTML, który zosta³ pobrany z bufora
	 */
	public String takePage() throws InterruptedException;
}
