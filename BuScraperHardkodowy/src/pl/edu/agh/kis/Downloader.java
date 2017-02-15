package pl.edu.agh.kis;

import java.io.OutputStream;
import java.io.InputStream;

/**
 * Interfejs, którego implementacje bior¹ udzia³ przy pracy w¹tków DownloadThread
 * umo¿liwiaj¹c udostêpnienie odpowiednich strumieni wejœcia oraz wyjœcia, nie k³opocz¹c
 * w¹tków DownloadThread o sposób w jaki strumienie ³¹cz¹ siê z odpowiednimi danymi
 * co daje dobr¹ elastycznoœæ do testowania DownloadThread przy u¿yciu plików jak
 * i mo¿liwoœæ dobrego odseparowania logiki w¹tków pobieraj¹cych od niskopoziomowej
 * komunikacji z serverem.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface Downloader {

	/**
	 * Funkcja odpowiada za utworzenie strumieni oraz ich w³aœciwe pod³¹czenie
	 * informuje o powodzeniu swojego dzia³ania. Deklaruje mo¿liwoœæ wyrzucania
	 * wyj¹tków, mo¿liwych do wyrzucenia przede wszystkim przy tworzeniu obiektów
	 * umo¿liwiaj¹cych komunikacjê sieciow¹.
	 * @param hostName nazwa hosta na rzecz którego inicjujemy obiekt
	 * @return informacja o powodzeniu tworzenia strumieni
	 */
	public boolean initDownloader(String hostName);
	
	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wyjœciowy przechowywany w implementacji
	 * interfejsu.
	 * @return strumieñ wyjœciowy przechowywany w implementacji interfejsu
	 */
	public OutputStream getOutputStream();
	
	/**
	 * Funkcja ma za zadanie zwracaæ strumieñ wejœciowy przechowywany w implementacji
	 * interfejsu.
	 * @return strumieñ wejœciowy przechowywany w implementacji interfejsu
	 */
	public InputStream getInputSteam();
}
