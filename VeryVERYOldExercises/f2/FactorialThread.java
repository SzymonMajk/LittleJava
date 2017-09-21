package pl.edu.agh.kis.f2;

import java.io.IOException;
import java.math.BigInteger;

import pl.edu.agh.kis.f1.FactorialUtils;
import pl.edu.agh.kis.f1.FileUtils;

public class FactorialThread extends Thread {

	// liczba z ktorej bedzie obliczona silnia
	private long n; 

	public FactorialThread(long n) {
		this.n = n;
	}

	@Override
	public void run() {
		// oblczenie silni
		BigInteger factorial = FactorialUtils.calcFactorial(n);

		// zapis wyniku do pliku
		String fileName = "n" + n + ".txt";
		try {
			FileUtils.saveBigInt(fileName, factorial);
		} catch (IOException e) {
			System.out.println("Wystapil problem z zapisem pliku: " + fileName);
			e.printStackTrace();
		}
	}

}
