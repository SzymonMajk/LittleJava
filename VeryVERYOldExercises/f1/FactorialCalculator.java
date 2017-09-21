package pl.edu.agh.kis.f1;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import pl.edu.agh.kis.f1.FactorialUtils;
import pl.edu.agh.kis.f1.FileUtils;

/**
 * Pierwsza wersja obliczania silni z podanych w pliku liczb. Wykonywana
 * sekwencyjnie w jednym watku.
 * 
 */
public class FactorialCalculator {

	private static final String LINE_COMMENT_PREFIX = "#";
	private static final String INPUT_FILE_ENCODING = "UTF-8";

	public static void main(String[] args) throws IOException {
		// sprawdzenie czy program zostal poprawnie wywolany
		if (args.length != 1) {
			printUsageInfo();
			System.exit(1);
		}

		String inputFilePath = args[0];

		List<String> factorialsToCalculate = FileUtils.readLinesFromFile(
				inputFilePath, INPUT_FILE_ENCODING);
		
		long startTime = System.nanoTime();

		// wlasciwa czesc obliczen i zapisu do pliku wynikow
		processFactorials(factorialsToCalculate);

		long endTime = System.nanoTime();

		long time = endTime - startTime;
		System.out.println("Wersja 1 - czas wykonywania obliczen: " + time / 1000000
				+ " [ms]");
	}

	
	private static void printUsageInfo() {
		System.out
				.println("Musisz podac jako argument wywolania sciezke do pliku zawierajaego liczby,"
						+ "z ktorych nalezy wyliczyc silnie.");
		System.out.println();
	}

	/**
	 * Wylicza silnie z podanych liczb i zapisuje wyniki do plikow.
	 * 
	 * @param factorialsToCalculate
	 *            lista lini zawierajacych liczby (jedna w lini) dla ktorych ma
	 *            byc wyliczona silnia. Jesli linia zaczyna sie od znaku '#' to
	 *            jest ona traktowana jako komentarz
	 */
	private static void processFactorials(List<String> factorialsToCalculate)
			throws IOException {

		for (String ln : factorialsToCalculate) {
			// jezeli linia zaczyna sie od znaku komentarza to jest ignorowana
			if (ln.trim().startsWith(LINE_COMMENT_PREFIX)) {
				continue;
			}

			// zamiana napisu na liczbe typu long
			long n = Long.parseLong(ln);

			// oblczenie silni
			BigInteger factorial = FactorialUtils.calcFactorial(n);

			// zapis wyniku do pliku
			String fileName = "n" + n + ".txt";
			try {
				FileUtils.saveBigInt(fileName, factorial);
			} catch (IOException e) {
				System.out.println("Wystapil problem z zapisem pliku: "
						+ fileName);
				e.printStackTrace();
				throw e;
			}
		}
	}

}
