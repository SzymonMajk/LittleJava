package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import pl.edu.agh.kis.math.SolverCalculator;

public class SolverCalculatorTests
{
	@Test
	public void testCalculateLowerCriterionsPriorityVector() 
	{
		Double[] data1 = {2.0,5.0,3.0};
		Double[] test = SolverCalculator.calculateLowerCriterionsPriorityVector(data1);
		
		assertEquals(test[0], new Double(0.4163114848181448));
		assertEquals(test[1], new Double(0.3436575589372035));
		assertEquals(test[2], new Double(0.24003095624465184));
		
		Double[] data2 = {2.0,5.0,3.0,4.0,2.0,5.0};
		test = SolverCalculator.calculateLowerCriterionsPriorityVector(data2);
		
		assertEquals(test[0], new Double(0.2907433986585326));
		assertEquals(test[1], new Double(0.26419646805558833));
		assertEquals(test[2], new Double(0.2544202178821889));
		assertEquals(test[3], new Double(0.19063991540369019
));
	}

	@Test
	public void testSumVectorsWithWeights()
	{
		Double[] data1 = {2.0,5.0,3.0};
		Double[] data2 = {4.0,3.0,1.0};
		Double[] data3 = {1.0,6.0,2.0};
		Double[] dataWeights = {3.0,2.0,1.0};
		
		ArrayList<Double[]> testArray = new ArrayList<Double[]>();
		Double[] test1 = SolverCalculator.calculateLowerCriterionsPriorityVector(data1);
		Double[] test2 = SolverCalculator.calculateLowerCriterionsPriorityVector(data2);
		Double[] test3 = SolverCalculator.calculateLowerCriterionsPriorityVector(data3);
		Double[] weights = SolverCalculator.calculateLowerCriterionsPriorityVector(dataWeights);
		
		testArray.add(test1);
		testArray.add(test2);
		testArray.add(test3);
		
		Double[] toCheck = SolverCalculator.sumVectorsWithWeights(testArray,weights);
		
		assertEquals(toCheck[0], new Double(0.4215571566044293));
		assertEquals(toCheck[1], new Double(0.32232456709218077));
		assertEquals(toCheck[2], new Double(0.2561182763033901));	
	}
}
