package pl.edu.agh.kis.f4;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import pl.edu.agh.kis.f1.FactorialUtils;
import pl.edu.agh.kis.f1.FileUtils;

public class FactorialConsumer implements Runnable {

	private ArrayList<Long> dataQueue;
	private final String threadName;
	long startTime;

	/**
	 * Budowniczy - pozwala elastycznie konfigurowac tworzony obiekt
	 */
	public static class Builder {
		private final ArrayList<Long> dataQueue;
		private String threadName = "thread";
		private long startTime = System.nanoTime();

		public Builder(ArrayList<Long> dataQueue) {
			this.dataQueue = dataQueue;
		}

		public Builder name(String name) {
			threadName = name;
			return this;
		}

		public Builder startTime(long startTime) {
			this.startTime = startTime;
			return this;
		}

		public FactorialConsumer build() {
			return new FactorialConsumer(this);
		}
	}

	private FactorialConsumer(Builder b) {
		this.dataQueue = b.dataQueue;
		this.threadName = b.threadName;
		this.startTime = b.startTime;
	}

	@Override
	public void run() {
		while (true) {
			if (dataQueue.isEmpty()) {
				// czekaj az bedzie cos do przetwarzania
				continue;
			}

			Long n = null;
			try {
				// pobranie nastepnej wartosci z kolejki - UWAGA PROBLEMY!
				n = dataQueue.get(0);
			} catch (IndexOutOfBoundsException e) {
				continue;
			}

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
			}

			long time = System.nanoTime() - startTime;

			// usuniecie przetworzonej wartosci
			dataQueue.remove(0);

			// wyswietl info
			System.out.println("[" + threadName + "] " + "obliczanie " + n
					+ "! - czas " + time / 1000000 + " [ms]");
		}

	}

}
