package pl.edu.agh.kis;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interfejs, którego implementacje bior¹ udzia³ przy pracy w¹tków DownloadThread
 * umo¿liwiaj¹c udostêpnienie odpowiednich strumieni wejœcia o wyjœcia, nie k³opocz¹c
 * w¹tków DownloadThread o sposób w jaki strumienie ³¹cz¹ siê z odpowiednimi danymi
 * co daje dobr¹ elastycznoœæ do testowania DownloadThread na plikach
 * @author Szymon Majkut
 * version 1.3
 *
 */
public interface Downloader {

	/**
	 * Funkcja odpowiada za utworzenie strumieni oraz ich w³aœciwe pod³¹czenie
	 * informuje o powodzeniu swojego dzia³ania
	 * @return informacja o powodzeniu tworzenia strumieni
	 */
	boolean createStreams();
	
	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wyjœciowy przechowywany w implementacji
	 * interfejsu
	 * @return strumieñ wyjœciowy przechowywany w implementacji interfejsu
	 */
	OutputStream getOutputStream();
	
	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wejœciowy przechowywany w implementacji
	 * interfejsu
	 * @return strumieñ wejœciowy przechowywany w implementacji interfejsu
	 */
	InputStream getInputSteam();
}
