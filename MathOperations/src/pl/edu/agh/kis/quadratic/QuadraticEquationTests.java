package pl.edu.agh.kis.quadratic;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class QuadraticEquationTests {

	@Test
	public void testCalculate() {
		//Kilka testów dla przypadku dwóch pierwiastków rzeczywistych
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1 -3 -10"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1.0 -3 -10"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1 -3.0 -10"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1 -3 -10"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1 -3 -10.0"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1.0 -3.0 -10.0"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1 -3 -10 anotherText"));
		assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
				QuadraticEquation.calculate("1 -3 -10 anotherTestAndWhiteSpace "));
		//Kilka testów dla przypadku jednego pierwiastka rzeczywistego
		assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
				QuadraticEquation.calculate("1 -6 9"));
		assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
				QuadraticEquation.calculate("1.0 -6 9"));
		assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
				QuadraticEquation.calculate("1 -6.0 9"));
		assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
				QuadraticEquation.calculate("1 -6 9.0"));
		assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
				QuadraticEquation.calculate("1.0 -6.0 9.0"));
		assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
				QuadraticEquation.calculate("1 -6 9  textAndAnotherWhiteSpace "));
		//Kilka testów braku rozwiązań rzeczywistych
		assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
				QuadraticEquation.calculate("1 2 5"));
		assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
				QuadraticEquation.calculate("1.0 2 5"));
		assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
				QuadraticEquation.calculate("1 2.0 5"));
		assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
				QuadraticEquation.calculate("1 2 5.0"));
		assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
				QuadraticEquation.calculate("1.0 2.0 5.0"));
		assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
				QuadraticEquation.calculate("1 2 5 textAndAnotherWhiteSpace "));
		//Kilka przypadków nietypowych
		assertEquals("a = -2.6965 b = 6.457 c = -2.46 => " +
				"x1 = 0.47533966963216817 x2 =  1.9192459042599141",
				QuadraticEquation.calculate("-2.6965 6.457 -2.46"));
		assertEquals("a = -256856.69 b = 645848.46 c = 2568568.46 => " +
				"x1 = -2.1458120616319722 x2 =  4.660243202203004",
				QuadraticEquation.calculate("-256856.69 645848.46 2568568.46"));
		assertEquals("a = 99999.0 b = 99999.0 c = -99999.0 => " +
				"x1 = 0.6180339887498949 x2 =  -1.6180339887498947",
				QuadraticEquation.calculate("99999 99999.0 -99999"));
		assertEquals("a = 12.0 b = 0.0 c = -1.0 => " +
				"x1 = 0.28867513459481287 x2 =  -0.28867513459481287",
				QuadraticEquation.calculate("12.0 0 -1"));
		//Kilka testów dla przypadków z zerowym pierwszym współczynnikiem
		assertEquals("a = 0.0 b = 0.0 c = 0.0 => Pierwszy współczynnik = 0!",
				QuadraticEquation.calculate("0 0 0"));
		assertEquals("a = 0.0 b = 2.0 c = 0.0 => Pierwszy współczynnik = 0!",
				QuadraticEquation.calculate("0 2.0 0"));
		assertEquals("a = 0.0 b = 0.0 c = 0.0 => Pierwszy współczynnik = 0!",
				QuadraticEquation.calculate("0 0 0.0"));
		assertEquals("a = 0.0 b = 6.0 c = 0.0 => Pierwszy współczynnik = 0!",
				QuadraticEquation.calculate("0 6.0 0"));
		assertEquals("a = 0.0 b = 0.0 c = -5.0 => Pierwszy współczynnik = 0!",
				QuadraticEquation.calculate("0 0 -5.0"));
		//Kilka przypadków, które oznaczają błędy w danych wejściowych
		assertEquals("Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!",
				QuadraticEquation.calculate("5g 5 5"));
		assertEquals("Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!",
				QuadraticEquation.calculate("5g 5.05h5 5"));
		assertEquals("Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!",
				QuadraticEquation.calculate("yu5g 5 5.65"));
		assertEquals("Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!",
				QuadraticEquation.calculate("/5\\g 5.65u5 [5"));
	}

	@Test
	public void testFileCalculations() throws IOException {
		
		//tutaj wykorzystamy takie same przypadki jak w testach powyżej, tylko uprzednio
		//trzeba je zeskładować w pliku...
		
		//utworzenie pliku i wpisanie testowych coefów
		File testIn = new File("testFileCalculationsJUnitIN");
		File testOut = new File("testFileCalculationsJUnitOUT");
		
		FileWriter writerIn = new FileWriter(testIn);
		
		writerIn.write("1 -3 -10\n");
		writerIn.write("1 -6 9\n");
		writerIn.write("1 2 5\n");
		writerIn.write("-2.6965 6.457 -2.46\n");
		writerIn.write("0 0 0\n");
		writerIn.write("5g 5 5\n");
		writerIn.write("53\n");

		writerIn.close();
		
		//używamy funkcji i porównujemy to co powinno się pojawić, z tym co się
		//pojawi w pliku wyjścia
		QuadraticEquation.fileCalculations(testIn,testOut);
		
		//porównanie danych w pliku wyjścia z oczekiwanymi
		BufferedReader inRead = new BufferedReader(new FileReader(testOut));
		String line = "";
		
		if((line = inRead.readLine()) != null)
		{
			assertEquals("a = 1.0 b = -3.0 c = -10.0 => x1 = 5.0 x2 =  -2.0",
					line);
		}
		else
		{
			fail("Results to short");
		}
		
		if((line = inRead.readLine()) != null)
		{
			assertEquals("a = 1.0 b = -6.0 c = 9.0=> x1 = x2 = 3.0",
					line);
		}
		else
		{
			fail("Results to short");
		}
		
		if((line = inRead.readLine()) != null)
		{
			assertEquals("a = 1.0 b = 2.0 c = 5.0 => Brak rozwiązań rzeczywistych",
					line);
		}
		else
		{
			fail("Results to short");
		}
		
		if((line = inRead.readLine()) != null)
		{
			assertEquals("a = -2.6965 b = 6.457 c = -2.46 => " +
				"x1 = 0.47533966963216817 x2 =  1.9192459042599141",
					line);
		}
		else
		{
			fail("Results to short");
		}
		
		if((line = inRead.readLine()) != null)
		{
			assertEquals("a = 0.0 b = 0.0 c = 0.0 => Pierwszy współczynnik = 0!",
					line);
		}
		else
		{
			fail("Results to short");
		}
		
		if((line = inRead.readLine()) != null)
		{
			assertEquals("Błędy w linii wejściowej, spradź czy podałeś właściwe współczynniki!",
					line);
		}
		else
		{
			fail("Results to short");
		}
		
		inRead.close();
		
		//usunięcie utworzonych plików
		
		if(testIn.exists())
		{
			if(!(testIn.delete() && testOut.delete()))
			{
				fail("Test files not deleted");
			}
			
		}
		
	}

}
