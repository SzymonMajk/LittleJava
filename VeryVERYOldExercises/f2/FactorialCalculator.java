package pl.edu.agh.kis.f2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.kis.f1.FileUtils;

/**
 * Wersja programu z uzyciem watkow tworzonych recznie na bazie obiektow z klasy
 * dziedziczacej z Thread.
 */
public class FactorialCalculator {

	private static final String LINE_COMMENT_PREFIX = "#";
	private static final String INPUT_FILE_ENCODING = "UTF-8";

	public static void main(String[] args) throws IOException,
			InterruptedException {
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
		System.out.println("Wersja 2 - czas wykonywania obliczen: " + time
				/ 1000000 + " [ms]");
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
			throws IOException, InterruptedException {

		ArrayList<Thread> threads = new ArrayList<>();

		for (String ln : factorialsToCalculate) {
			// jezeli linia zaczyna sie od znaku komentarza to jest ignorowana
			if (ln.trim().startsWith(LINE_COMMENT_PREFIX)) {
				continue;
			}

			// zamiana napisu na liczbe typu long
			long n = Long.parseLong(ln);

			// utworzenie obiektu watku
			Thread factorialThread = new FactorialThread(n);
			threads.add(factorialThread);

			// UWAGA - zeby uruchomic watek nalezy wywolac jego metode start(),
			// wywolanie bezposrednio metody run() spowoduje ze wykona sie ona
			// ale w watku glownym.
			// NIE TAK: factorialThread.run();

			// uruchomienie metod run() w nowym watku
			factorialThread.start();
		}

		// zaczekanie, az wszystkie watki sie skoncza - bez tego zle bedzie
		// dzialal pomiar czasu w metodzie main()
		for (Thread t : threads) {
			t.join();
		}

	}

}
