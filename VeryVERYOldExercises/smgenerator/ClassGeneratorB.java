package pl.edu.agh.kis.smgenerator;

/**: Object/pl/edu/agh/kis/rozdz5/ClassGeneratorB */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * Druga wersja generatora klas javy, tworzący proste szkielety klas
 * z pakietów znajdujących się w pliku i zapisujący pliki w odpowiednich katalogach
 * zdecydowałem się na dziedziczenie po ClassGeneratorA, ponieważ w ten sposób
 * mogę zapobiec duplikowaniu kodu w pakiecie
 * @author Szymon Majkut
 *
 */
public class ClassGeneratorB extends ClassGeneratorA {

	/**
	 * Funkcja pomocnicza, wyciąŋająca nazwę klasy z całego pakietu
	 * @param s pakiet z którego chcemy wyciągnąć nazwę
	 * @return nazwa funkcji z pakietu
	 */
	protected String takeName(String s)
	{
		int index = 0;
		for(int i = 0; i < s.length(); ++i)
		{
			if(s.charAt(i) == '.' || s.charAt(i) == '/')
			{
				index = i;
			}
		}
		
		if(s.substring(index+1).length() > 0)
		{
			return s.substring(index+1);
		}
		else
		{
			return "bez nazwy";
		}
		
	}
	
	/**
	 * Funkcja pomocnicza, która ma za zadanie wyciągnąć lokalizację,
	 * w której ma zostać utworzona klasa
	 * @param s pakiet z którego chcemy wyciągnąć lokalizację
	 * @return lokalizacja klasy
	 */
	protected String takeDir(String s)
	{
		int index = 0;
		for(int i = 0; i < s.length(); ++i)
		{
			if(s.charAt(i) == '.' || s.charAt(i) == '/')
			{
				index = i;
			}
		}
		if(s.substring(index+1).length() > 0)
		{
			return s.substring(0,index+1);
		}
		else
		{
			return "/home";
		}
		
	}
	/**
	 * Zawiera nazwę pakietu aktualnie generowanej klasy
	 */
	protected String packageName;
	
	/**
	 * Funkcja rozpoczynająca proces generowania klasy
	 *//*
	public void generate()
	{
		
		try {
			file.write("package " + packageName + "\n");
			generateClass(0,className);
		} catch (IOException e) {
			System.out.println("Wystąpił problem z wpisem do pliku");
			e.printStackTrace();
		}

	}*/
	
	/**
	 * Funkcja rozpoczynająca proces generowania klasy, wykorzystuje nazwę
	 * otrzymanego pliku źródłowego, czyta z niego poszczególne linie tekstu
	 * i wykorzystuje je w celu utworzenia nowego pliku o nazwie takiej samej
	 * jak nazwa klasy
	 */
	public void generate()
	{	
		FileReader in;
		try {
			String s = "";
			in = new FileReader(sourcePath);
			BufferedReader bufIn = new BufferedReader(in);
			
			while((s = bufIn.readLine()) != null)
			{
				//Tworzenie ścieżki do pliku
				String s2 = new String((takeDir(s)).replace('.','/'));
				
				File writingFile = new File(targetDir+s2);
				writingFile.mkdirs();
				
				//Tworzenie pliku do zapisu i podpięcie do niego klasy
				FileWriter out = new FileWriter(targetDir+s2+takeName(s) + ".java");
				
				// zabezpieczenie przed znakami białymi w nazwach klasy
				className = takeName(s).replace(' ', '#');
				targetFile = out;
				packageName = takeDir(s) + className + ";";
				
				//Sam fakt tworzenia klasy
				targetFile.write("package " + packageName +"\n");
				generateClass(0,className);
					
				targetFile.close();
			}
			
			bufIn.close();
			
		}	
		catch (FileNotFoundException e) {
			System.out.println("Nie znaleziono pliku");
			e.printStackTrace();
		}	
		catch (IOException e) {
			System.out.println("Problemy z plikami");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Kostruktor przypisujący nazwę pakietu, wyciąga z niego nazwę klasy
	 * i umieszcza te dane w odpowiednich atrybutach
	 * oraz przekazuje obiekt do komunikacji z plikiem
	 * @param name przekazuje nazwę tworzonej klasy
	 * @param f przekazuje obiekt łączący z tworzonym plikiem
	 */
	ClassGeneratorB(String sourcePath, String targetDir)
	{
		this.sourcePath = sourcePath;
		this.targetDir = targetDir;
	}
	
	/**
	 * Przygotowany do dziedziczenia
	 */
	ClassGeneratorB()
	{
		//nothing
	}
	
	/**
	 * Przykładowy sposób wykorzystania klasy
	 * @param args argumenty przekazane do programu
	 * @throws IOException mogą być generowane przez obiekty związane z komunikajcą
	 * z plikami
	 */
	public static void main(String[] args) 
	{
		if(args[0].charAt(args[0].length()-1) != '/' 
				&& args[1].charAt(args[1].length()-1) == '/')
		{
			ClassGeneratorB first = new ClassGeneratorB(args[0],args[1]);
			first.generate();
		}
		else
		{
			System.out.println("Błędne argumenty, powienien być plik i katalog!");
		}
	}
	/* Output (100%match)
	 *///:~
}
