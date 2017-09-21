package pl.edu.agh.kis.crawler;

/**
 * Klasa implementująca interfejs Buffer, która dane przychowuje w stałej tablicy,
 * dzięki niej możemy w dogodny sposób zaobserwować i testować wielowątkowość Robota,
 * @author Szymon Majkut
 *
 */
public class TableBuffer implements Buffer {

	final int SIZE = 10;
	private int current = 0;
	private String[] buf = new String[SIZE];
	
	public boolean isFull() {
		return current==SIZE;
	}

	public void put(String s) {

		while(isFull())
		{
			try {
				System.out.println("Czekam bo pełna...");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buf[current++] = s;
		System.out.println("Dodałem: " + s);
		notifyAll();
	}

	public boolean isEmpty() {
		return current==0;
	}

	public String take() {
		while(isEmpty())
		{
			try {
				System.out.println("Czekam bo pusta...");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		String s = buf[current--];
		System.out.println("Zwracam: " + s);
		notifyAll();
		return s;
	}
	
	public int size()
	{
		return current;
	}

}
