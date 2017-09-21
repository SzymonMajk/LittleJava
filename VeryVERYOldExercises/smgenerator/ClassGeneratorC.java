package pl.edu.agh.kis.smgenerator;

/**: Object/pl/edu/agh/kis/rozdz5/ClassGeneratorC */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Trzecia wersja generatora klas javy, tworzący proste szkielety klas
 * z pakietów znajdujących się w pliku i zapisujący pliki w odpowiednich katalogach
 * dodatkowo w pliku znajdują się określenia czy generować main i javadoc oraz
 * znajdują się pola do dodania, wraz z metodami set i get
 * zdecydowałem się na dziedziczenie po ClassGeneratorB, ponieważ w ten sposób
 * mogę zapobiec duplikowaniu kodu w pakiecie
 * @author Szymon Majkut
 *
 * Obsługuje pliki w których każda klasa znajduje się w jakimś pakiecie
 * po nazwie klasy następuje szereg nazw pól wraz z widocznością i określeniem
 * statyczności, po czym uzupełnia całość słowem end, inaczej klasa nie zostanie
 * wygenerowana
 */
public class ClassGeneratorC extends ClassGeneratorB {
	
	/**
	 * Stwierdza czy będziemy generować domyślną metodę main
	 */
	private boolean generateMain;
	
	/**
	 * Stwierdza czy będziemy generować komentarze
	 */
	private boolean generateJavadoc;
	
	/**
	 * Przechowuje zmienne, które musimy wygenerować
	 */
	private Queue<String> variablesToGenerate = new LinkedList<String>();
	
	/**
	 * Zadaniem funkcji jest wyczyszczenie wszystkich pól generatora
	 */
	private void clearData()
	{
		generateMain = false;
		generateJavadoc = false;
		variablesToGenerate.clear();
		className = "BezNazwy";
		packageName = "";
	}
	
	/**
	 * Funkcja ma za zadanie utworzenie komentarza Javadoc przed generowaną klasą
	 * zabezpieczona na wypadek chęci wyłączenia komentarzy
	 * @param tabNumber ilość wcięć
	 * @param content zawartość komentarza
	 */
	protected void generateClassComment(int tabNumber, String content)
	{
		if (generateJavadoc)
		{
			generateTab(tabNumber, "/** ");
			generateTab(tabNumber, " * " + content);
			generateTab(tabNumber, " * ");
			generateTab(tabNumber, " */ ");
		}
	}
	
	/**
	 * Funkcja ma za zadanie utworzenie komentarza Javadoc przed funkcją
	 * bezargumentową - zabezpieczona na wypadek chęci wyłączenia komentarzy
	 * @param tabNumber ilość wcięć
	 * @param content zawartość komentarza
	 */
	protected void generateMethodComment(int tabNumber, String content)
	{
		if (generateJavadoc)
		{
			generateTab(tabNumber, "/** ");
			generateTab(tabNumber, " * " + content);
			generateTab(tabNumber, " */ ");
		}
	}
	
	/**
	 * Funkcja ma za zadanie utworzenie komentarza Javadoc przed funkcją
	 * z argumentami - zabezpieczona na wypadek chęci wyłączenia komentarzy
	 * @param tabNumber ilość wcięć
	 * @param content zawartość komentarza
	 * @param argsNames tablica nazw argumentów
	 * @param argsComments tablica opisów argumentów dla Javadoc
	 */
	protected void generateMethodComment(int tabNumber, String content, String[] argsNames, String[] argsComments)
	{
		if (generateJavadoc)
		{
			generateTab(tabNumber, "/**");
			generateTab(tabNumber, " * " + content);
			
			if (argsNames.length == argsComments.length)
			{
				for(int i = 0; i < argsNames.length; ++i)
				{
					generateTab(tabNumber, " * @param " + argsNames[i] + " " + argsComments[i]);
				}
			}
			else
			{
				generateTab(tabNumber, " * " + "Ilość argumentów i ich komentrzy nie jest zgodna");
			}
			generateTab(tabNumber, " */");
		}
	}
	
	private String varName(String s)
	{
		int index = 0;
		for(int i = 0; i < s.length(); ++i)
		{
			if(s.charAt(i) == ' ')
			{
				index = i;
				break;
			}
		}
		
		if(s.substring(index+1).length() > 0)
		{
			return s.substring(index+1);
		}
		else
		{
			return "bezNazwy";
		}
	}
	
	private String varType(String s)
	{
		int index = 0;
		for(int i = 0; i < s.length(); ++i)
		{
			if(s.charAt(i) == ' ')
			{
				index = i;
			}
		}
		
		if(s.substring(0,index).length() > 0)
		{
			return s.substring(0,index);
		}
		else
		{
			return "bezTypu";
		}
	}
	
	/**
	 * Funkcja tworzy pole
	 * @param tabNumber ilość wcięć
	 * @param s zawartość z nazwą pola
	 */
	private void generateVariable(int tabNumber, String s)
	{
		generateMethodComment(tabNumber,"Przykładowe pole");
		generateTab(tabNumber, "private "+s+";");
	}
	
	/**
	 * Funkcja tworzy wszystkie zmienne z VariablesToGenerate
	 * wraz z metodami set i get dla każdej z nich
	 */
	private void generateVariables(int tabNumber)
	{
		while(!variablesToGenerate.isEmpty())
		{
			String s = variablesToGenerate.poll();
			generateVariable(tabNumber,s);
			
			String sName = varName(s);
			String sType = varType(s);
			String toGetSetNameS = sName.substring(0,1).toUpperCase() + sName.substring(1);
			
			String[] argsNames = {sType + " " + sName};
			String[] argsComments = {sName + " przykładowa wartość"};
			generateMethod(tabNumber,"Przykładowy set", "public", "set"+toGetSetNameS, 
									"this."+sName+" = " + sName + ";", argsNames, argsComments);
			generateMethod(tabNumber,"Przykładowy get", "public", "get"+toGetSetNameS,
									"", sType, sName);
		}
	}
	
	/**
	 * Funkcja odpowiedzialna za wygenerowanie funkcji ze zwracaną wartością,
	 * bez argumentów
	 * @param tabNumber ilość wcięć
	 * @param commentContent zawartość komentarza dla generowanej metody
	 * @param specifiers zasięg widoczności funkcji
	 * @param name nazwa funkcji
	 * @param bodyContent zawartość ciała funkcji
	 * @param returnName powiadamia co zwraca dana metoda
	 */
	protected void generateMethod(int tabNumber, String commentContent, String specifiers,String name, String bodyContent, String returnName, String returnValue)
	{

		generateMethodComment(tabNumber,commentContent);
		String firstLine = specifiers + " " + returnName + " " + name + "(";
		
		firstLine += ") { \n";
		
		generateTab(tabNumber, firstLine);
		if (!bodyContent.equals(""))
		{
			generateTab(tabNumber+1, bodyContent);
		}
		if (!returnName.equals(""))
		{
			generateTab(tabNumber+1, "return " + returnValue + ";");
		}
		generateTab(tabNumber, "}");
	}
	
	/**
	 * Funkcja odpowiedzialna za wygenerowanie klasy wraz z jej konstruktorem
	 * domyślnym oraz funkcją main
	 * @param tabNumber poziom zagłębienia klasy
	 * @param name nazwa klasy
	 */
	protected void generateClass(int tabNumber, String name)
	{
		//tutaj konkretnie generujemy klasę najbardziej zewnętrzną
		generateClassComment(tabNumber,"Klasa generowana automatycznie " + name);
		
		generateTab(tabNumber,"public class " + name + " {");
		
		generateVariables(tabNumber+1);
		
		generateMethod(tabNumber+1,"Domyślny konstruktor " + name, "public", name, "//metoda wygenerowana - należy uzupełnić implementacje");
		
		if (generateMain)
		{
			String[] args = {"String[] args"};
			String[] argsComments = {"tablica argumentów przekazanych do funkcji"};
			generateMethod(tabNumber+1, "Metoda main automatycznie generowana","static public void","main", "System.out.println(\"Metoda jeszcze nie jest zaimplementowana - tylko wygenerowany wzorzec\");", args, argsComments);
		}
		
		generateTab(tabNumber, "}");
	}
	
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
				FileWriter out;
			
				if (s.equals("end"))
				{
					//Teraz gdy mamy dane, możemy wygenerować klasę
					targetFile.write("package " + packageName +"\n");
					generateClass(0,className);
					
					//Czyszczenie danych dla nowej klasy
					clearData();
					targetFile.close();
				}
				if (s.contains(".")) // mamy do czynienia z nazwą pakietu
				{
					
					//Tworzenie ścieżki do pliku
					String s2 = new String((takeDir(s)).replace('.','/'));

					//File writingFile = new File(takeDir(sourcePath)+s2);
					File writingFile = new File(targetDir+s2);
					writingFile.mkdirs();
					
					//Tworzenie pliku do zapisu i podpięcie do niego klasy
					//out = new FileWriter(takeDir(sourcePath)+s2+takeName(s) + ".java");
					out = new FileWriter(targetDir+s2+takeName(s) + ".java");
					
					// zabezpieczenie przed znakami białymi w nazwach klasy
					className = takeName(s).replace(' ', '#');
					targetFile = out;
					packageName = takeDir(s) + className + ";";
				}
				else if(s.contains("="))
				{
					if(s.contains("main=t"))
					{
						generateMain = true;
					}
					if(s.contains("javadoc=t"))
					{
						generateJavadoc = true;
					}
					if(s.contains("var="))
					{
						variablesToGenerate.add(s.substring(4,s.length()));
					}
				}
			
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
	ClassGeneratorC(String sourcePath, String targetDir)
	{
		this.sourcePath = sourcePath;
		this.targetDir = targetDir;
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
			ClassGeneratorC first = new ClassGeneratorC(args[0],args[1]);
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
