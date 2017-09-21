package pl.edu.agh.kis.f6;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

import pl.edu.agh.kis.f1.FactorialUtils;
import pl.edu.agh.kis.f1.FileUtils;

public class FactorialConsumer implements Runnable {

	private BlockingQueue<Long> dataQueue;
	private final String threadName;
	long startTime;

	/**
	 * Budowniczy - pozwala elastycznie konfigurowac tworzony obiekt
	 */
	public static class Builder {
		private final BlockingQueue<Long> dataQueue;
		private String threadName = "thread";
		private long startTime = System.nanoTime();

		public Builder(BlockingQueue<Long> dataQueue) {
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
		try {
			while (true) {
				// pobranie nastepnej wartosci z kolejki
				long n = dataQueue.take();
				
				// zakonczenie wykonywania jesli przekazano wartosc < 0
				// if (n < 0) {
				// break;
				// }

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

				// wyswietl info
				System.out.println("[" + threadName + "] " + "obliczanie " + n
						+ "! - czas " + time / 1000000 + " [ms]");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Zakonczono watek [" + threadName + "]");
	}

}
