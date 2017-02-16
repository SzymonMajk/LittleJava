package pl.edu.agh.kis.search;

/**
 * Klasa pomocnicza przechowuj�ca szczeg�y pojedy�czego wyszukania, zawiera
 * i udost�pnia nazw� przystanku pocz�tkowego oraz ko�cowego, rodzaj dnia,
 * godzin� oraz minuty od kt�rych naliczmay czas, oraz maksymalny czas r�nicy
 * pomi�dzy poszukiwanymi godzinami a godzin� startow� w godzinach,
 * dla kt�rego mo�emy wyszukiwa�. Warto�ci te s� nadawane w konstruktorze
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
	 * Zwraca nazw� przystanku pocz�tkowego. Jego warto�� jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawno�ci pozostawiami dostawc� danych do konstruktora.
	 * @return nazwa przystanku pocz�tkowego, ustalona poprzez argument konstruktora.
	 */
	public String getFirstBuStop()
	{
		return firstBuStop;
	}
	
	/**
	 * Zwraca nazw� przystanku ko�cowego. Jego warto�� jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawno�ci pozostawiami dostawc� danych do konstruktora.
	 * @return nazwa przystanku ko�cowego, ustalona poprzez argument konstruktora.
	 */
	public String getSecondBuStop()
	{
		return secondBuStop;
	}
	
	/**
	 * Zwraca rodzaj dnia. Mo�liwe s� dni powszednie, wtedy warto�� to 0, dla sob�t 
	 * warto�� to 1, a dla �wi�t warto�� o 2. Jego warto�� jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawno�ci pozostawiami dostawc� danych do konstruktora.
	 * @return rodzaj dnia, ustalona poprzez argument konstruktora.. Mo�liwe s� dni 
	 * powszednie, wtedy warto�� to 0, dla sob�t warto�� to 1, a dla �wi�t warto�� o 2.
	 */
	public int getTypeOfDay()
	{
		return typeOfDay;
	}
	
	/**
	 * Zwraca pocz�tkow� godzin� wyszukania. Jego warto�� jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawno�ci pozostawiami dostawc� danych do konstruktora.
	 * @return pocz�tkow� godzin� wyszukania, ustalona poprzez argument konstruktora.
	 */
	public int getHour()
	{
		return hour;
	}
	
	/**
	 * Zwraca pocz�tkowe minuty wyszukania. Ich warto�� jest ustalana w kontruktorze,
	 * a sprawdzanie jego poprawno�ci pozostawiami dostawc� danych do konstruktora.
	 * @return pocz�tkowe minuty wyszukania, ustalone poprzez argument konstruktora.
	 */
	public int getMinutes()
	{
		return minutes;
	}
	
	/**
	 * Zwraca maksymalny czas odjazdu wyszukania w godzinach . Ich warto�� jest ustalana
	 * w kontruktorze, a sprawdzanie jego poprawno�ci pozostawiami dostawc� danych
	 * do konstruktora.
	 * @return maksymalny czas odjazdu w godzinach, ustalony poprzez argument konstruktora.
	 */
	public int getMaxTime()
	{
		return maxTime;
	}
	
	/**
	 * Konstruktor sparametryzowany pozwalaj�cy na ustalenie nazwy przystanku
	 * pocz�tkowego, przystanku ko�cowego, rodzaju dnia, godziny oraz minut
	 * startowych wyszukania, a tak�e maksymlanego czasu r�nicy pomi�dzy godzin�
	 * startow� a ostatni� bran� pod uwag� godzin�, jednostk� s� godziny.
	 * @param firstBuStop nazwa przystanku pocz�tkowego.
	 * @param secondBuStop nazwa przystanku ko�cowego.
	 * @param typeOfDay rodzaj dnia. 
	 * @param hour godzina od kt�rej naliczamy odjazd.
	 * @param minutes minuty od kt�rych naliczamy odjazd.
	 * @param maxTime maksymalny czas odjazdu w godzinach.
	 * @param typeOfDay rodzaj dnia. Mo�liwe s� dni powszednie, wtedy warto�� to 0,
	 * 		dla sob�t warto�� to 1, a dla �wi�t warto�� o 2.
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
