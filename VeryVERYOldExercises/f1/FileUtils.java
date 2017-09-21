package pl.edu.agh.kis.f1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Funkcje narzedziowe zwiazane z obsluga plikow.
 */
public class FileUtils {

	/**
	 * Zapisuje podana liczbe typu BigInteger jako tekst do pliku o zadanej
	 * sciezce.
	 * 
	 * @param filePath
	 *            sciezka do pliku w ktorym ma zostac zapisana liczba
	 * @param value
	 *            liczba, ktora ma zostac zapisana
	 */
	public static void saveBigInt(String filePath, BigInteger value)
			throws IOException {
		try (PrintStream ps = new PrintStream(new FileOutputStream(filePath))) {
			ps.println(value.toString());
		}
	}

	/**
	 * Wczytuje wszystkie linie z pliku tekstowego o zadanym kodowaniu i zwraca
	 * je jako liste
	 * 
	 * @param filePath
	 *            sciezka do pliku, ktory ma zostac odczytany
	 * @param encoding
	 *            kodowanie pliku
	 * @return liste zawierajaca odczytane linie
	 */
	public static List<String> readLinesFromFile(String filePath,
			String encoding) throws IOException {

		List<String> lineList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), encoding))) {

			String ln;
			while ((ln = br.readLine()) != null) {
				lineList.add(ln);
			}

		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Nieobslugiwane kodowanie: "
					+ encoding);
		}
		return lineList;
	}

}
