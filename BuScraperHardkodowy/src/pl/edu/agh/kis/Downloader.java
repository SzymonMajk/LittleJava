package pl.edu.agh.kis;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interfejs, kt�rego implementacje bior� udzia� przy pracy w�tk�w DownloadThread
 * umo�liwiaj�c udost�pnienie odpowiednich strumieni wej�cia o wyj�cia, nie k�opocz�c
 * w�tk�w DownloadThread o spos�b w jaki strumienie ��cz� si� z odpowiednimi danymi
 * co daje dobr� elastyczno�� do testowania DownloadThread na plikach
 * @author Szymon Majkut
 * version 1.3
 *
 */
public interface Downloader {

	/**
	 * Funkcja odpowiada za utworzenie strumieni oraz ich w�a�ciwe pod��czenie
	 * informuje o powodzeniu swojego dzia�ania
	 * @return informacja o powodzeniu tworzenia strumieni
	 */
	boolean createStreams();
	
	/**
	 * Funkcja ma za zadanie zwraca� strumie� wyj�ciowy przechowywany w implementacji
	 * interfejsu
	 * @return strumie� wyj�ciowy przechowywany w implementacji interfejsu
	 */
	OutputStream getOutputStream();
	
	/**
	 * Funkcja ma za zadanie zwraca� strumie� wej�ciowy przechowywany w implementacji
	 * interfejsu
	 * @return strumie� wej�ciowy przechowywany w implementacji interfejsu
	 */
	InputStream getInputSteam();
}
