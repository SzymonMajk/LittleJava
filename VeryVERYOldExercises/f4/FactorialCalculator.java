package pl.edu.agh.kis.f4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.kis.f1.FileUtils;

/**
 * Wersja programu z uzyciem wzorca Producent-Konsument na bazie zwyklej (nie
 * wielowatkowej) kolejki.
 * (nie dziala poprawnie!)
 */
public class FactorialCalculator {

	private static final String LINE_COMMENT_PREFIX = "#";
	private static final String INPUT_FILE_ENCODING = "UTF-8";
	private static final int NO_OF_CONSUMERS = 6;

	private static ArrayList<Long> dataToCalcQueue = new ArrayList<>();

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

		// utworzenie watkow konsumentow
		createConsumers(dataToCalcQueue);

		// uruchomienie producenta x 10
		for (int i = 0; i < 10; ++i) {
			processFactorials(factorialsToCalculate);
		}

		// utworzenie watkow konsumentow
		// createConsumers(dataToCalcQueue);
		
	}

	private static void printUsageInfo() {
		System.out
				.println("Musisz podac jako argument wywolania sciezke do pliku zawierajaego liczby,"
						+ "z ktorych nalezy wyliczyc silnie.");
		System.out.println();
	}

	private static void createConsumers(ArrayList<Long> dataQueue) {
		long startTime = System.nanoTime();

		for (int i = 0; i < NO_OF_CONSUMERS; ++i) {
			FactorialConsumer.Builder builder = new FactorialConsumer.Builder(
					dataQueue).name("watek " + i).startTime(startTime);

			Runnable consumerRunnable = builder.build();
			Thread consumerThread = new Thread(consumerRunnable);
			consumerThread.start();
		}
	}

	/**
	 * Wstawia liczby, z ktorych ma zostac obliczona silnia do kolejki.
	 * 
	 * @param factorialsToCalculate
	 *            lista lini zawierajacych liczby (jedna w lini) dla ktorych ma
	 *            byc wyliczona silnia. Jesli linia zaczyna sie od znaku '#' to
	 *            jest ona traktowana jako komentarz
	 */
	private static void processFactorials(List<String> factorialsToCalculate)
			throws IOException, InterruptedException {

		for (String ln : factorialsToCalculate) {
			// jezeli linia zaczyna sie od znaku komentarza to jest ignorowana
			if (ln.trim().startsWith(LINE_COMMENT_PREFIX)) {
				continue;
			}

			// zamiana napisu na liczbe typu long
			long n = Long.parseLong(ln);

			dataToCalcQueue.add(n);
		}

		System.out.println("Wstawiono wszystkie wartosci do kolejki");

	}

}
