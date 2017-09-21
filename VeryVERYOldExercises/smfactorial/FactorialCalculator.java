package pl.edu.agh.kis.smfactorial;

import java.math.BigInteger;

/**
 * Klasa obliczająca silnie, wykorzystująca zadane sposoby cashowania
 * oraz używania kolejki priorytetowej
 * @author Szymon Majkut
 *
 */
public class FactorialCalculator {

	/**
	 * Referencja wskazująca na zadany sposób implementacji casha wartości
	 */
	private Cashable cashImplementation;
	/**
	 * Referencja wskazująca na zadany sposób implementacji kolejki zleceń
	 */
	private Queueable queueImplementation;
	
	/**
	 * Dodaje nowe zlecenie do kolejki zleceń
	 * @param i wartość która ma zostać dodana
	 */
	void add(int i)
	{
		if(!queueImplementation.exist(i))
		{
			queueImplementation.add(i);
		}
	}
	
	/**
	 * Zadaniem funkcji jest zwrócenie indeksu liczby dla której
	 * jesteśmy w stanie znaleźć pierwszą mniejszą od zadanej w cashu
	 * @param n liczba dla której ma zostać odnaleziona pierwsza mniejsza
	 * @return indeks liczby która jest pierwszą mniejszą od zadanego parametru
	 */
	private int getFirstLowerIndex(int n)
	{
		for(int i = n; i > 1; --i)
		{
			if(cashImplementation.exist(i))
			{
				return i;
			}
		}
		//jeżeli dotąd nic nie znaleźliśmy to zwróćmy 2
		return 1;
	}
	
	/**
	 * Funkcja ma za zadanie odnaleźć lub obliczyć wartość silni dla zadanej liczby
	 * @param n liczba dla której liczymy silnię
	 * @return wartość silni dla zadanej liczby
	 */
	private BigInteger calculateFactorial(int n)
	{
		int iterator = getFirstLowerIndex(n);
		BigInteger result;
		if (cashImplementation.exist(iterator))
		{
			result = cashImplementation.get(iterator);
		}
		else
		{
			result = new BigInteger("1");
		}
		
		for (int i = iterator+1; i <= n; ++i)
		{
			result = result.multiply(new BigInteger("" + i));
		}
		
		cashImplementation.add(n,result);
		
		return result;
	}
	
	/**
	 * Przeprowadza obliczenia dla elementów znajdujących się w kolejce
	 * @return String w którym kolejne pary liczba = wartość znajdują się
	 * w nowych liniach
	 */
	String calculate()
	{
		//dopóki kolejka nie jest pusta zwracamy kolejne wyniki
		String result = "";
		
		while (!queueImplementation.isEmpty())
		{
			int toCalculate = queueImplementation.poll();
			result += toCalculate + " = " + calculateFactorial(toCalculate) + "\n";
		}
			
		return result;
	}
	
	/**
	 * Konstruktor przyjmujący obiekty implementujące odpowiednie interfejsy
	 * @param c - obiekt przyjmujący interfejs składowania obiektów
	 * @param q - obiekt przyjmujący interfejs kolejkowania zleceń
	 */
	FactorialCalculator(Cashable c, Queueable q)
	{
		cashImplementation = c;
		queueImplementation = q;
	}
	
	/**
	 * kilka testów
	 * @param args nieużywane
	 */
	public static void main(String[] args) {
		/*
		NFactorialCalculator n1 = new NFactorialCalculator(new SimpleCash(), new SimpleQueue());
		n1.add(5);
		n1.add(3);
		n1.add(6);
		System.out.println(n1.calculate());
		*//*
		FactorialCalculator n2 = new FactorialCalculator(new TempCash(), new SimpleQueue());
		n2.add(5);
		n2.add(3);
		n2.add(5);
		n2.add(8);
		n2.add(12);
		n2.add(125);
		
		for(int i = 44; i > 0; --i)
		{
			n2.add(i);
			if(i % 15 == 0)
			{
				n2.calculate();
			}
		}

		System.out.println(n2.calculate());*/
		FactorialCalculator n3 
			= new FactorialCalculator(new PriorityQueueCash(), new SimpleQueue());
	
		for(int i = 0; i < 999; ++i)
		{
			n3.add(i);
		}

		System.out.println(n3.calculate());
		
	}

}
