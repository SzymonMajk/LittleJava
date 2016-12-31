package pl.edu.agh.kis;

import java.util.Date;

/**
 * Klasa przez którą odbywa się samo wykorzystywanie biblioteki logów,
 * umożliwia utworzenie logów na trzech poziomach priorytetu oraz
 * filtrowanie logów
 * @author Szymon Majkut
 * @version 1.1
 *
 */
public class Logger {
	
	/*
	 * priorityLevel label:
	 * 0 - INFO
	 * 1 - WARNING
	 * 2 - ERROR
	 */
	private int priorityLevel = 0;
	
	/**
	 * Setter dla pola priorityLevel z prostym sprawdzaniem danych
	 * @param newLevel nowy poziom dokładności logów
	 */
	public void setPriorityLevel(int newLevel)
	{
		if(newLevel >= 0 && newLevel <= 2)
		{
			priorityLevel = newLevel;
		}
		else
		{
			System.out.println("Podano błędny priority Level!");
		}
	}
	
	/**
	 * Domyślne implementacje cashy dla logów na różnych poziomach
	 */
	private LogsCashing infoCash = new SimpleLogCash();
	private LogsCashing warningCash = new SimpleLogCash();
	private LogsCashing errorCash = new SimpleLogCash();
	
	/**
	 * Analogicznie do klas interfejsu LogCashing, możemy wybrać sposób wysłania logów
	 */
	private Appends append;
	
	/**
	 * Funkcja służy do zmiany obecnego Appendera
	 * @param newAppender nowy sposób wysyłania logów
	 */
	public void changeAppender(Appends newAppender)
	{
		//np. można zmienić na plikowy, a mamy pewność że funkcje
		// implementujące Appends będą posiadać potrzebne do działania metody
		append = newAppender;
		append.clear();
	}
	
	/**
	 * Funkcja dodaje datę, dodaję wszystkie argumenty loga, oraz znak nowej linii
	 * @param s argumenty loga
	 * @return gotowy log z datą i argumentami w postaci Stringa
	 */
	private String prepareLog(String[] args)
	{
		String result = "" + new Date() + ": ";
		for(int i = 0; i < args.length-1; ++i)
			result += args[i] + ", ";
		result += args[args.length-1];
		return result + "\n";
	}
	
	/**
	 * Dodawanie loga typu INFO
	 * @param s czysty log
	 */
	public void info(String...args)
	{
		if(priorityLevel <= 0)
		{
			infoCash.addLog(prepareLog(args));
		}
	}
	
	/**
	 * Dodawanie loga typu WARNING
	 * @param s czysty log
	 */
	public void warning(String...args)
	{
		if(priorityLevel <= 1)
		{
			warningCash.addLog(prepareLog(args));
		}		
	}
	
	/**
	 * Dodawanie loga typu ERROR
	 * @param s czysty log
	 */
	public void error(String...args)
	{
		if(priorityLevel <= 2)
		{
			errorCash.addLog(prepareLog(args));
		}
	}
	
	/**
	 * Funkcja uruchamia procedurę wysyłania zeskładowanych logów, jeżeli
	 * logger znajduje się na odpowiednim dla nich poziomie priorytetu
	 */
	public void execute()
	{
		if(priorityLevel <= 2)
		{
			while(!errorCash.isEmpty())
			{
				append.sendNext(errorCash.pollLog());
			}
		}
		
		if(priorityLevel <= 1)
		{
			while(!warningCash.isEmpty())
			{
				append.sendNext(warningCash.pollLog());
			}
		}	
		
		if(priorityLevel <= 0)
		{
			while(!infoCash.isEmpty())
			{
				append.sendNext(infoCash.pollLog());
			}
		}
		
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalający natychmiast określić rodzaj
	 * wysyłania logów
	 * @param startAppender
	 */
	public Logger(Appends startAppender)
	{
		append = startAppender;
		append.clear();
	}
	
	/**
	 * Domyślny konstruktor wykorzystujący swój sparametryzowany odpowiednik
	 */
	public Logger()
	{
		this(new ConsoleAppender());
	}
	
	/**
	 * Kilka testów
	 * @param args w tych testach nie wykorzystywany
	 */
	public static void main(String[] args)
	{
		Logger l = new Logger();
		
		l.info("Typowe info");
		l.warning("Typowy warning", "I drugi argument");
		l.error("Typowy error");
		
		l.setPriorityLevel(0);
		
		l.execute();
		
		l.info("Typowe info");
		l.warning("Typowy warning");
		l.error("Typowy error");
		
		l.setPriorityLevel(1);
		
		l.execute();
		
		l.changeAppender(new FileAppender());
		
		
		l.info("Typowe info");
		l.warning("Typowy warning");
		l.error("Typowy error");
		
		l.setPriorityLevel(1);
		
		l.execute();
		
	}
}