package pl.edu.agh.kis;

/**
 * Zadaniem interfejsu jest udost�pni� funkcje pobrania zawarto�ci, dodania
 * zawarto�ci oraz sprawdzenia stanu synchronizowanego
 * bufora ( pusty/pe�ny), na kt�rym oprzemy metod� producent-konsument pomocn�
 * przy wydobywaniu danych z internetu.
 * @author Szymon Majkut
 * @version 1.4
 *
 */
public interface PagesBuffer {

	/**
	 * Implementacja ma zwr�ci� informacj� czy bufor jest pusty.
	 * @return informacja logiczna czy bufor jest pusty
	 */
	public boolean isEmpty();
	
	/**
	 * Implementacja ma zwr�ci� informacj� czy bufor jest pe�ny.
	 * @return informacja logiczna czy bufor jest pe�ny
	 */
	public boolean isFull();
	
	/**
	 * Implementacja ma za zadanie doda� stron� do niezape�nionego bufora, u�ytkonik
	 * jest zobligowany do wcze�niejszego wykonania implementacji metody isFull().
	 * @throws InterruptedException wyj�tek pojawia si� przy pr�bie nieprawid�owego wybudzenia
	 * @param pageHTML Kod �r�d�owy strony HTML, kt�ry umieszczamy w niezape�nionym buforze
	 */
	public void addPage(String pageHTML) throws InterruptedException;
	
	/**
	 * Implementacja ma za zadanie pobra� oraz usun�� stron� z niepustego bufora, u�ytkonik
	 * jest zobligowany do wcze�niejszego wykonania implementacji metody isEmpty().
	 * @throws InterruptedException wyj�tek pojawia si� przy pr�bie nieprawid�owego wybudzenia
	 * @return Kod �r�d�owy strony HTML, kt�ry zosta� pobrany z bufora
	 */
	public String takePage() throws InterruptedException;
}
