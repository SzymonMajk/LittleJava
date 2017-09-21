package pl.edu.agh.kis.logger;

import java.io.BufferedWriter;
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

	File result = new File("logs/log");
	
	/**
	 * Funkcja wysyła loga na do pliku
	 */
	public void sendNext(String s) {
		
		//sprawdzanie czy ścieżka i plik istnieją, jeśli nie tworzenie
		
		try {
		
		if(!result.exists())
		{
			new File(result.getParent()).mkdirs();
			result.createNewFile();
		}
		
		//musimy zabezpieczyć się przed nadpisywaniem
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
			FileWriter out;
			try {
				out = new FileWriter(result,true);
				out.write("");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}