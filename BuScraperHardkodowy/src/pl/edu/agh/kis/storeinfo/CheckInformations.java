package pl.edu.agh.kis.storeinfo;

/**
 * Interfejs zapewniaj¹cy mo¿liwoœæ skorzystania z wzorca projektowego strategii dla 
 * sprawdzania poprawnoœci danych zdobytych przez obiekt wy³uskuj¹cy przed zesk³adowaniem 
 * ich w pamiêci komputera. Udostêpnia metodê pozwalaj¹c¹ na sprawdzenie poprawnoœci 
 * otrzymanych danych.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface CheckInformations {
	
	/**
	 * Funkcja ma za zadanie sprawdziæ poprawnoœæ danych otrzymanych w argumencie
	 * oraz zwróciæ wartoœæ prawdy w przypadku ich poprawnoœci lub fa³szu w przypadku
	 * braku poprawnoœci. Poprawnoœæ jest zale¿na od danej implementacji interfejsu.
	 * @param informations String zawieraj¹cy dane wyodrêbnione przez obiekt wy³uskuj¹cy
	 * 		na rzecz których musimy przeprowadziæ sprawdzenie.
	 * @return wartoœæ prawdy w przypadku poprawnoœci danych lub wartoœæ fa³szu, je¿eli
	 * nie przejd¹ walidacji.
	 */
	public boolean checkInformation(String informations);
}