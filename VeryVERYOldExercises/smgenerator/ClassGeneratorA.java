package pl.edu.agh.kis.smgenerator;

/**: Object/pl/edu/agh/kis/rozdz5/ClassGeneratorA */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Pierwsza wersja generatora klas javy, tworzący proste szkielety klas
 * z nazw znajdujących się w pliku
 * @author Szymon Majkut
 *
 */
public class ClassGeneratorA {

	/**
	 * Przechowuje nazwę pliku z którego powinniśmy czytać
	 */
	protected String sourcePath;
	/**
	 * Przechowuje ściężkę do katalogu w którym będziemy zapisywać
	 */
	protected String targetDir;
	/**
	 * Zawiera nazwę aktualnie generowanej klasy
	 */
	protected String className;
	/**
	 * Obiekt przez który klasa może zapisywać wyniki w pliku docelowym
	 */
	protected FileWriter targetFile;
	
	/**
	 * Funkcja odpowiedzialna za utworzenie pojedynczej linii znaków
	 * i jeżeli trzeba wykonania wcięcia, swój wynik przekazuje do pliku
	 * za pośrednictwem targetFile
	 * @param tabNumber ilość wcięć
	 * @param content zawartość linii
	 */
	protected void generateTab(int tabNumber, String content)
	{
		try {
		for(int i = 0; i < tabNumber; ++i)
		{
			//System.out.print("    ");
			targetFile.write("    ");
		}
		
			//System.out.print(content+"\n");
			targetFile.write(content+"\n");
		} catch (IOException e) {
			System.out.println("Wystąpił problem z wpisem do pliku");
			e.printStackTrace();
		}
	}
	
	/**
	 * Funkcja ma za zadanie utworzenie komentarza Javadoc przed generowaną klasą
	 * @param tabNumber ilość wcięć
	 * @param content zawartość komentarza
	 */
	protected void generateClassComment(int tabNumber, String content)
	{
		generateTab(tabNumber, "/** ");
		generateTab(tabNumber, " * " + content);
		generateTab(tabNumber, " * ");
		generateTab(tabNumber, " */ ");
		
	}
	
	/**
	 * Funkcja ma za zadanie utworzenie komentarza Javadoc przed funkcją
	 * bezargumentową
	 * @param tabNumber ilość wcięć
	 * @param content zawartość komentarza
	 */
	protected void generateMethodComment(int tabNumber, String content)
	{
		generateTab(tabNumber, "/** ");
		generateTab(tabNumber, " * " + content);
		generateTab(tabNumber, " */ ");
		
	}
	
	/**
	 * Funkcja ma za zadanie utworzenie komentarza Javadoc przed funkcją
	 * z argumentami
	 * @param tabNumber ilość wcięć
	 * @param content zawartość komentarza
	 * @param argsNames tablica nazw argumentów
	 * @param argsComments tablica opisów argumentów dla Javadoc
	 */
	protected void generateMethodComment(int tabNumber, String content, String[] argsNames, String[] argsComments)
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
	
	/**
	 * Funkcja odpowiedzialna za wygenerowanie kostruktora bezargumentowego
	 * @param tabNumber ilość wcięć
	 * @param commentContent zawartość komentarza dla generowanej metody
	 * @param specifiers zasięg widoczności funkcji
	 * @param name nazwa funkcji
	 * @param bodyContent zawartość ciała funkcji
	 */
	protected void generateMethod(int tabNumber, String commentContent, String specifiers,String name, String bodyContent)
	{
		generateMethodComment(tabNumber,commentContent);
		
		generateTab(tabNumber, specifiers + " " + name + "() {");
		generateTab(tabNumber+1, bodyContent);
		generateTab(tabNumber, "}");
	}
	
	/**
	 * Funkcja odpowiedzialna za wygenerowanie funkcji z argumentami
	 * bez zwracanej wartości
	 * @param tabNumber ilość wcięć
	 * @param commentContent zawartość komentarza dla generowanej metody
	 * @param specifiers zasięg widoczności funkcji
	 * @param name nazwa funkcji
	 * @param bodyContent zawartość ciała funkcji
	 * @param argsNames tablica nazw argumentów
	 * @param argsComments tablica opisów argumentów dla Javadoc
	 */
	protected void generateMethod(int tabNumber, String commentContent, String specifiers,String name, String bodyContent, String[] argsNames, String[] argsComments)
	{
		generateMethodComment(tabNumber,commentContent,argsNames,argsComments);
		
		String firstLine = specifiers + " void " + name + "(";
		
		for(int i = 0; i < argsNames.length-1; ++i)
		{
			firstLine += argsNames[i] + ", ";
		}
		firstLine += argsNames[argsNames.length-1];
		firstLine += ") { \n";
		
		generateTab(tabNumber, firstLine);
		generateTab(tabNumber+1, bodyContent);
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
		generateMethod(tabNumber+1,"Domyślny konstruktor " + name, "public", name, "//metoda wygenerowana - należy uzupełnić implementacje");
		String[] args = {"String[] args"};
		String[] argsComments = {"tablica argumentów przekazanych do funkcji"};
		generateMethod(tabNumber+1, "Metoda main automatycznie generowana","static public","main", "System.out.println(\"Metoda jeszcze nie jest zaimplementowana - tylko wygenerowany wzorzec\");", args, argsComments);
		
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
			
			// czyta z pliku wszystkie linie
			while((s = bufIn.readLine()) != null)
			{
				//FileWriter out = new FileWriter("/home/szymek/Generator/"+s+".java");
				FileWriter out = new FileWriter(targetDir+s+".java");
					
				// zabezpieczenie przed znakami białymi w nazwach klasy
				className = s.replace(' ', '#');
				targetFile = out;
					
				//Sam fakt tworzenia klasy
				targetFile.write("package pl.edu.agh.kis;\n");
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
	 * Konstruktor przyjmujący jako argument ściężkę do pliku z którego ma czytać
	 * @param sourceName - nazwa pliku, z którego musimy czytać
	 */
	ClassGeneratorA(String sourcePath, String targetDir)
	{
		this.sourcePath = sourcePath;
		this.targetDir = targetDir;
	}
	
	/**
	 * Przeznaczony wyłącznie do dziedziczenia
	 */
	ClassGeneratorA() {}
	
	/**
	 * Przykładowy sposób wykorzystania klasy
	 * @param args argumenty przekazane do programu
	 */
	public static void main(String[] args)
	{			
		if(args[0].charAt(args[0].length()-1) != '/' 
				&& args[1].charAt(args[1].length()-1) == '/')
		{
			ClassGeneratorA first = new ClassGeneratorA(args[0],args[1]);
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
