package pl.edu.agh.kis.zadania1;
//cw 11
public class WszystkieKoloryTeczy {

	int liczbaCalkowitaKolorow;
	public void zmienOdcienKoloru(int nowyOdcien)
	{
		liczbaCalkowitaKolorow = nowyOdcien;
	}
	public int get()
	{
		return liczbaCalkowitaKolorow;
	}
	
	static public void main(String[] args)
	{
		WszystkieKoloryTeczy wkt = new WszystkieKoloryTeczy();
		
		wkt.zmienOdcienKoloru(5);
		System.out.println(wkt.get());
	}
}
