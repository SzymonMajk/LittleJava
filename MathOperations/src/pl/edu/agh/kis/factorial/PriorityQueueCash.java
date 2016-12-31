package pl.edu.agh.kis.factorial;

import java.math.BigInteger;
import java.util.PriorityQueue;

public class PriorityQueueCash implements Cashable {

	/**
	 * Kolejka prioryterowa, w której wyższy priorytet
	 * mają starsze wartości obliczone
	 */
	PriorityQueue<TimePackedNumber> operationResults 
		= new PriorityQueue<TimePackedNumber>();
	
	/**
	 * Przetrzymuje długość życia samego Casha
	 * przy tworzeniu przypisujemy mu wartość 1
	 */
	long cashLifeSpan = 1;
	
	private boolean enoughtMemoryForNumber(BigInteger b)
	{
		return Runtime.getRuntime().freeMemory() > b.byteValue();
	}
	
	private void removeOldest()
	{
		operationResults.poll();
	}
	
	/*
	 * Funkcja zwiększa czas trwania dla każdego obiektu w kolejce,
	 * jak i dla samego casha
	 * Wiemy, że z pewnością życie żadnego obiektu przechowywanego nie może być
	 * większe niż życie casha, dlatego wystarczy sprawdzić dla niego
	 */
	private void incLife()
	{
		if(cashLifeSpan == Long.MAX_VALUE)
		{
			handleIncLifeOverflow();
		}
		else
		{
			++cashLifeSpan;
			for(TimePackedNumber tmp : operationResults)
			{
				tmp.incLifeSpan();
			}
		}
	}
	
	/**
	 * Funkcja obsługuje sytuację,  gdy długość życia casha nie mieści
	 * się w zakresie long, wystarczy sprawdzać cash, ponieważ każdy inny
	 * obiekt ma życie krótsze niż cash
	 */
	private void handleIncLifeOverflow()
	{
		long longest = operationResults.size();
		cashLifeSpan = longest + 1;
		
		for(TimePackedNumber tmp : operationResults)
		{
			tmp.setLifeSpan(longest);
			--longest;
		}
	}
	
	/**
	 * Dodawanie liczby wraz z wynikiem, poprzedzone potrzbnym
	 * sprawdzeniem przepełnienia pamięci oraz zwiększeniem
	 * licznika czasu trwania dla casha i wszystkich przechowywanych liczb
	 */
	public void add(int i, BigInteger b) {
		// to ma odpalić czyszczenie jak brakuje pamięci albo jest za dużo obiektów w cashu

		
		//Usuwanie nastarszych wartości jeżeli brakuje pamięci
		while(!enoughtMemoryForNumber(b) && !operationResults.isEmpty())
		{
			removeOldest();
		}
		
		//Ustalamy dla wszystkich nową długość życia
		incLife();
		//Właściwe wstawianie obiektu
		operationResults.add(new TimePackedNumber(i,b));
	}

	/**
	 * Pobranie wartości dla zadanej liczby
	 * użytkownik jest zobowiązany wcześniej sprawdzić czy
	 * liczba istnieje
	 */
	public BigInteger get(int i) {
		
		for(int j = 0; j < operationResults.size(); ++j)
		{
			TimePackedNumber tmp = operationResults.peek();
			if(tmp.getNumber() == i)
			{
				//obiekt wyciągany ma odnawiany czas życia
				tmp.setLifeSpan(0);
				return tmp.getResult();
			}
		}
		
		return new BigInteger("0");
	}

	/**
	 * Sprawdzenie obeności zadanej liczby w kolejce
	 */
	public boolean exist(int i) {
		
		for(int j = 0; j < operationResults.size(); ++j)
		{
			if(operationResults.peek().getNumber() == i)
			{
				return true;
			}
		}
		
		return false;
	}
	
}

/**
 * Klasa do przechowywania i porównywania poprzez czas trwania
 * w cashu liczb i wyników operacji
 * @author Szymon Majkut
 *
 */
class TimePackedNumber implements Comparable<TimePackedNumber>
{
	/**
	 * Pole wraz z seterem i geterem odpowiedzialne za
	 * przechowanie liczby do obliczeń
	 */
	private int number = 0;
	//public void setNumber(int i) { number = i; }
	public int getNumber() { return number; }
	
	/*
	 * Pole wraz z seterem i geterem odpowiedzialne za
	 * przechowanie wartości wyniku operacji dla danej liczby
	 */
	private BigInteger result = new BigInteger("0");
	//public void setResult(BigInteger b) { result = b; }
	public BigInteger getResult() { return result; }
	
	/**
	 * Pole określa długość szeroko pojętego życia obiektu
	 */
	private long lifeSpan = 0;
	
	/**
	 * Funkcja inkrementująca długość życia obiektu
	 * problem obsługi overflow pozostawiamy cashowi
	 */
	public void incLifeSpan() { ++lifeSpan; }
	
	/**
	 * Ustawienie wartości długości życia
	 * @param l nowa długość życia obiektu
	 */
	public void setLifeSpan(long l)
	{
		lifeSpan = l;
	}
	
	/**
	 * Funkcja porównuje dwa, czasy życia 
	 * @param l obiekt porównywany z obecnym
	 * @return efekt porównania który element powinien być wyżej w kolejce
	 */
	public int compareTo(TimePackedNumber l) {
		if(lifeSpan > l.lifeSpan)
		{
			return 1;
		}
		else if(lifeSpan == l.lifeSpan)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
	/**
	 * Konstruktor, aby nie dało się już zewnątrz zmienić liczby
	 * i wyniku w obiektcie
	 * @param i
	 * @param r
	 */
	TimePackedNumber(int i, BigInteger r)
	{
		number = i;
		result = r;
	}
}
