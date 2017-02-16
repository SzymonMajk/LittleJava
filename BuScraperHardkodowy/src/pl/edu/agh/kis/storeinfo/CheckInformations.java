package pl.edu.agh.kis.storeinfo;

/**
 * Interfejs zapewniaj�cy mo�liwo�� skorzystania z wzorca projektowego strategii dla 
 * sprawdzania poprawno�ci danych zdobytych przez obiekt wy�uskuj�cy przed zesk�adowaniem 
 * ich w pami�ci komputera. Udost�pnia metod� pozwalaj�c� na sprawdzenie poprawno�ci 
 * otrzymanych danych.
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public interface CheckInformations {
	
	/**
	 * Funkcja ma za zadanie sprawdzi� poprawno�� danych otrzymanych w argumencie
	 * oraz zwr�ci� warto�� prawdy w przypadku ich poprawno�ci lub fa�szu w przypadku
	 * braku poprawno�ci. Poprawno�� jest zale�na od danej implementacji interfejsu.
	 * @param informations String zawieraj�cy dane wyodr�bnione przez obiekt wy�uskuj�cy
	 * 		na rzecz kt�rych musimy przeprowadzi� sprawdzenie.
	 * @return warto�� prawdy w przypadku poprawno�ci danych lub warto�� fa�szu, je�eli
	 * nie przejd� walidacji.
	 */
	public boolean checkInformation(String informations);
}