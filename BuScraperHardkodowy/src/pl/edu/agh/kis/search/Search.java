package pl.edu.agh.kis.search;

/**
 * Klasa pomocnicza przechowuj¹ca szczegó³y pojedyñczego wyszukania, zawiera
 * i udostêpnia nazwê przystanku pocz¹tkowego oraz koñcowego, rodzaj dnia,
 * godzinê oraz minuty od których naliczmay czas, oraz maksymalny czas ró¿nicy
 * pomiêdzy poszukiwanymi godzinami a godzin¹ startow¹ w godzinach,
 * dla którego mo¿emy wyszukiwaæ. Wartoœci te s¹ nadawane w konstruktorze
 * sparametryzowanym.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class Search {

	/**
	 * 
	 */
	private String firstBuStop;
	
	/**
	 * 
	 */
	private String secondBuStop;
	
	/**
	 * 
	 */
	private int typeOfDay;
	
	/**
	 * 
	 */
	private int hour;
	
	/**
	 * 
	 */
	private int minutes;
	
	/**
	 * 
	 */
	private int maxTime;
	
	/**
	 * Zwraca nazwê przystanku pocz¹tkowego. Jego wartoœæ jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawnoœci pozostawiami dostawc¹ danych do konstruktora.
	 * @return nazwa przystanku pocz¹tkowego, ustalona poprzez argument konstruktora.
	 */
	public String getFirstBuStop()
	{
		return firstBuStop;
	}
	
	/**
	 * Zwraca nazwê przystanku koñcowego. Jego wartoœæ jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawnoœci pozostawiami dostawc¹ danych do konstruktora.
	 * @return nazwa przystanku koñcowego, ustalona poprzez argument konstruktora.
	 */
	public String getSecondBuStop()
	{
		return secondBuStop;
	}
	
	/**
	 * Zwraca rodzaj dnia. Mo¿liwe s¹ dni powszednie, wtedy wartoœæ to 0, dla sobót 
	 * wartoœæ to 1, a dla œwi¹t wartoœæ o 2. Jego wartoœæ jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawnoœci pozostawiami dostawc¹ danych do konstruktora.
	 * @return rodzaj dnia, ustalona poprzez argument konstruktora.. Mo¿liwe s¹ dni 
	 * powszednie, wtedy wartoœæ to 0, dla sobót wartoœæ to 1, a dla œwi¹t wartoœæ o 2.
	 */
	public int getTypeOfDay()
	{
		return typeOfDay;
	}
	
	/**
	 * Zwraca pocz¹tkow¹ godzinê wyszukania. Jego wartoœæ jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawnoœci pozostawiami dostawc¹ danych do konstruktora.
	 * @return pocz¹tkow¹ godzinê wyszukania, ustalona poprzez argument konstruktora.
	 */
	public int getHour()
	{
		return hour;
	}
	
	/**
	 * Zwraca pocz¹tkowe minuty wyszukania. Ich wartoœæ jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawnoœci pozostawiami dostawc¹ danych do konstruktora.
	 * @return pocz¹tkowe minuty wyszukania, ustalone poprzez argument konstruktora.
	 */
	public int getMinutes()
	{
		return minutes;
	}
	
	/**
	 * Zwraca maksymalny czas odjazdu wyszukania w godzinach . Ich wartoœæ jest ustalana
	 * w kontruktorze, a sprawdzanie jego poprawnoœci pozostawiami dostawc¹ danych
	 * do konstruktora.
	 * @return maksymalny czas odjazdu w godzinach, ustalony poprzez argument konstruktora.
	 */
	public int getMaxTime()
	{
		return maxTime;
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalaj¹cy na ustalenie nazwy przystanku
	 * pocz¹tkowego, przystanku koñcowego, rodzaju dnia, godziny oraz minut
	 * startowych wyszukania, a tak¿e maksymlanego czasu ró¿nicy pomiêdzy godzin¹
	 * startow¹ a ostatni¹ bran¹ pod uwagê godzin¹, jednostk¹ s¹ godziny.
	 * @param firstBuStop nazwa przystanku pocz¹tkowego.
	 * @param secondBuStop nazwa przystanku koñcowego.
	 * @param typeOfDay rodzaj dnia. 
	 * @param hour godzina od której naliczamy odjazd.
	 * @param minutes minuty od których naliczamy odjazd.
	 * @param maxTime maksymalny czas odjazdu w godzinach.
	 * @param typeOfDay rodzaj dnia. Mo¿liwe s¹ dni powszednie, wtedy wartoœæ to 0,
	 * 		dla sobót wartoœæ to 1, a dla œwi¹t wartoœæ o 2.
	 */
	public Search(String firstBuStop, String secondBuStop,
			int typeOfDay, int hour, int minutes, int maxTime)
	{
		this.firstBuStop = firstBuStop;
		this.secondBuStop = secondBuStop;
		this.typeOfDay = typeOfDay;
		this.hour = hour;
		this.minutes = minutes;
		this. maxTime = maxTime;
	}
}
