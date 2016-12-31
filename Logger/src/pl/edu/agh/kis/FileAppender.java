package pl.edu.agh.kis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementacja klasy odpowiedzialnej za wysyłanie logów do pliku
 * plik jest tworzony w katalogu logs i wstępnie tworzony jest jeden plik
 * o nazwie log
 * @author Szymon Majkut
 *
 */
public class FileAppender implements Appends {

	/**
	 * Obiekt zawierający informacje o pliku, w którym składowane będą logi
	 */
	private File result;
	
	/**
	 * Funkcja wysyła pojedynczego loga do pliku logów
	 */
	public void sendNext(String s) {
		
		//sprawdzanie czy ścieżka i plik istnieją, jeśli nie tworzenie
		
		try {
		
		if(!result.exists())
		{
			new File(result.getParent()).mkdirs();
			result.createNewFile();
		}
		
		//musimy zabezpieczyć się przed wyczyszczeniem
		FileWriter out = new FileWriter(result,true);
		
		//przesył danych
				
			out.write(s+"\n");
		
		out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Czyścimy poprzednią zawartość pliku
	 */
	public void clear()
	{
		if(result.exists())
		{
			try {
				//usuwamy i tworzymy plik od nowa
				result.delete();
				result.createNewFile();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sparametryzowany konstruktor pozwalający na określenie nazwy pliku z logami
	 * @param name ciąg znaków w postaci Stringa określający nazwę pliku z logami
	 */
	FileAppender(String name)
	{
		result = new File("logs/"+name);
		
		if(!result.exists())
		{
			new File(result.getParent()).mkdirs();
			try {
				result.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Domyślny konstrukor, wykorzystujący sparametryzowaną wersję, z domyślną
	 * nazwą pliku
	 */
	FileAppender()
	{
		this("log");
	}
}