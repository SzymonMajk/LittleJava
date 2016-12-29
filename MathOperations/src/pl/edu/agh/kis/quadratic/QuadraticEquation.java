package pl.edu.agh.kis.quadratic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Klasa do masowego obliczania rzeczywistych pierwiastków kwadratowych
 * działająca na plikach, do działania potrzeba nazwę przygotowanego pliku
 * z zestawami parametrów równania, oraz nazwa pliku wyjściowego
 * @author Szymon Majkut
 */
public class QuadraticEquation {
	
	private static double calculateDiscriminant(double a, double b, double c)
	{
		return b*b - (4 * a * c);
	}
	
	private static double calculateSolution(double a, double b, double disc)
	{
		//otrzymane disc jest już odpowiednie, zawiera minus jeżeli trzeba
		return (-b + disc)/(2*a);
	}
	
	/**
	 * Odpowiada za sprawdzenie poprawności współczynników równania
	 * @param coef współczynniki równania
	 * @return sprawdzenie czy są odpowiednie
	 */
	private static boolean isGoodCoef(String coef)
	{				
		// Wzorzec na trzy liczby niekoniecznie z kropkami, oddzialane białym znakiem
		Pattern pat = Pattern.compile("(-?\\d+\\.?(\\d)*\\s+){2}(-?\\d+\\.?(\\d)*)");
			System.out.println(coef);
		if(!pat.matcher(coef).find())
		{
			return false;
		}
		
		return true;	
	}
	
	/**
	 * Najprostrza forma zwrócenia wyniku w zależności od podanych parametrów
	 * @param coef tablica stringów z parametrami równania
	 * @return informacja w postaci stringu - wynik obliczeń
	 */
	public static String calculate(String coefficients)
	{
		if(!isGoodCoef(coefficients))
		{
			return "Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!";
		}
		
		String[] coef = coefficients.split("\\s+");
		
		//mamy pewność że String coefficients zawiera poprawne dane

		double firstCoef = Double.parseDouble(coef[0]);
		double secondCoef = Double.parseDouble(coef[1]);
		double thirdCoef = Double.parseDouble(coef[2]);
		
		if(Double.parseDouble(coef[0]) == 0.0)
		{
			return "a = " + firstCoef + " b = " + secondCoef +
					" c = " + thirdCoef + " => Pierwszy współczynnik = 0!";
		}
		

		double discriminant = calculateDiscriminant(firstCoef,secondCoef,thirdCoef);
		
		if(discriminant < 0)
		{
			return "a = " + firstCoef + " b = " + secondCoef +
					" c = " + thirdCoef + " => Brak rozwiązań rzeczywistych";
		}
		else if(discriminant > 0)
		{
			double x1 = calculateSolution(firstCoef,
					secondCoef,Math.sqrt(discriminant));
			double x2 = calculateSolution(firstCoef,
					secondCoef,-Math.sqrt(discriminant));
					
			return "a = " + firstCoef + " b = " + secondCoef +
					" c = " + thirdCoef + " => x1 = " + x1 + " x2 =  " + x2;
		}
		else 
		{
			return "a = " + firstCoef + " b = " + secondCoef +
					" c = " + thirdCoef + "=> x1 = x2 = " +secondCoef / (-2 * firstCoef );
		}
	}
	
	/**
	 * Funkcja odpowiada za przeprowadzenie obliczeń dla wszystkich zestawów parametrów
	 * z pierwszego parametru, oraz zapisanie wyników do pliku o nazwie z drugiego
	 * parametru, funkcja radzi sobie z wyznaczaniem wadliwych linii oraz tworzy plik 
	 * wyjściowy jeżeli takowy nie istnieje
	 * @param input nazwa pliku z parametrami równań do obliczenia
	 * @param result nazwa pliku do którego mają zostać zapisane wyniki
	 */
	public static void fileCalculations(File input, File result)
	{
		
		String line = "";
		
		try {
		
			BufferedReader inRead = new BufferedReader(new FileReader(input));
				
			if(!result.exists())
			{
				//jeżeli trzeba tworzymy katalogi oraz plik
				//new File(result.getParent()).mkdirs();
				result.createNewFile();
			}
			
			FileWriter out = new FileWriter(result);
			
			//przesył danych
			
			while((line = inRead.readLine()) != null)
			{
				out.write(calculate(line)+"\n");
			}
			
			inRead.close();
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Przykład wykorzystania programu, jeżeli nie napotkamy problemów, wyniki obliczeń
	 * dla danych z pliku z parametrami, trafia do pliku z wynikami i program kończy działanie
	 * @param args argumenty programu, pierwszy to nazwa pliku wejściowego, drugi to nazwa pliku wyjściowego
	 */
	public static void main(String[] args)
	{		
		if(args.length < 2)
		{
			System.err.println("Podano złe parametry! Powinno być: plikwejscie plikwyjscie");
			return;
		}
		
		File input = new File(args[0]);
		File result = new File(args[1]);
		
		if(!input.exists())
		{
			System.err.println("Podany plik nie istnieje!");
			return;
		}
		
		fileCalculations(input,result);
	
	}
	
}