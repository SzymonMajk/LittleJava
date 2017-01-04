package pl.edu.agh.kis;

public class DownloaderException extends Exception {
	
	private String link;
	
	/**
	 * Prosty getter informacji zapisanej w wyjątku
	 * @return
	 */
	public String getLink()
	{
		return link;
	}
	
	/**
	 * Prosty konstruktor sparametryzowany czymś co powinno być URL'em
	 * @param l
	 */
	DownloaderException(String l)
	{
		link = l;
	}
}