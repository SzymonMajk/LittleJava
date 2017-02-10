package pl.edu.agh.kis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

/**
 * Interfejs, którego implementacje bior¹ udzia³ przy pracy w¹tków DownloadThread
 * umo¿liwiaj¹c udostêpnienie odpowiednich strumieni wejœcia oraz wyjœcia, nie k³opocz¹c
 * w¹tków DownloadThread o sposób w jaki strumienie ³¹cz¹ siê z odpowiednimi danymi
 * co daje dobr¹ elastycznoœæ do testowania DownloadThread przy u¿yciu plików jak
 * i mo¿liwoœæ dobrego odseparowania logiki w¹tków pobieraj¹cych od niskopoziomowej
 * komunikacji z serverem.
 * @author Szymon Majkut
 * version 1.4
 *
 */
public interface Downloader {

	/**
	 * Funkcja odpowiada za utworzenie strumieni oraz ich w³aœciwe pod³¹czenie
	 * informuje o powodzeniu swojego dzia³ania. Deklaruje mo¿liwoœæ wyrzucania
	 * wyj¹tków, mo¿liwych do wyrzucenia przede wszystkim przy tworzeniu obiektów
	 * umo¿liwiaj¹cych komunikacjê sieciow¹.
	 * @return informacja o powodzeniu tworzenia strumieni
	 * @throws UnknownHostException wyj¹tki zwi¹zane z nieudanym po³¹czeniem sieciowym
	 * @throws IOException wyj¹tki zwi¹zane z obs³ug¹ wejœcia/wyjœcia
	 */
	public boolean initDownloader() throws IOException;
	
	/**
	 * Zadaniem funkcji jest zamkniêcie strumieni. Deklaruje mo¿liwoœæ wyrzucania
	 * wyj¹tków, mo¿liwych do wyrzucenia przede wszystkim przy zamykaniu strumieni dla
	 * komunikacji sieciowej.
	 * @throws UnknownHostException wyj¹tki zwi¹zane z nieudanym po³¹czeniem sieciowym
	 * @throws IOException wyj¹tki zwi¹zane z obs³ug¹ wejœcia/wyjœcia
	 */
	public void closeStreams() throws UnknownHostException, IOException;
	
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
