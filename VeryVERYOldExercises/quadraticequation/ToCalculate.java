package pl.edu.agh.kis.quadraticequation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToCalculate {

	double firstCoef;
	double secondCoef;
	double thirdCoef;
	
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
	private static boolean isGoodCoef(String[] coef)
	{
		if(coef.length < 3)
		{
			return false;
		}
		
		//wykorzystujemy wyrażenie regularne do znalezienia odpowiedniej ilości liczb
		Pattern pat = Pattern.compile("-?[0-9]+\\.[0-9]+");
		Matcher mat;
			
		for(int i = 0; i < 3; ++i)
		{
			mat = pat.matcher(coef[i]);
			if(!mat.find())
			{
				return false;
			}
		}
		
		//jeśli przejdzie wszystkie sprawdzenia, to dane są podane poprawnie
		return true;
		
	}
	
	/**
	 * Najprostrza forma zwrócenia wyniku w zależności od podanych parametrów
	 * @param coef tablica stringów z parametrami równania
	 * @return informacja w postaci stringu - wynik obliczeñ
	 */
	private static String calculate(String coefficients)
	{
		String[] coef = coefficients.split(" ");
		
		if(!isGoodCoef(coef))
		{
			return "Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!";
		}
		
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
	
	public static void main(String[] args)
	{


		
		//i teraz tylko zmienić, żeby zamiast tak, to zapisywało do pliku
		
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
		
		String line = "";
		//String[] coefficients = line.split(" ");
		
		try {
		
			BufferedReader inRead = new BufferedReader(new FileReader(input));
				
			if(!result.exists())
			{
				new File(result.getParent()).mkdirs();
				result.createNewFile();
			}
			
			FileWriter out = new FileWriter(result);
	
			
			//przesył danych
			
			while((line = inRead.readLine()) != null)
			{
				//coefficients = line.split(" ");
				out.write(calculate(line)+"\n");
			}
			
			inRead.close();
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
}