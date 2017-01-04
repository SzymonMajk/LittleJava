package pl.edu.agh.kis;

public interface PageCash extends DownloadQueue, VisitedPages {
	
	/**
	 * Zadaniem funkcji jest wylistowanie zawartości linków już odwiedzonych
	 * @return String z linkami już odwiedzonymi
	 */
	public String listVisited();
	
	/**
	 * Zadaniem funkcji jest wylistowanie zawartości linków do odwiedzenia
	 * @return String z linkami do odwiedzenia
	 */
	public String listQueue();
}
