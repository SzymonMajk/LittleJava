package pl.edu.agh.kis;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class MathTests
{
	@Test
	public void testCalculateLowerCriterionsPriorityVector() 
	{

		Double[] test = AhpMaths.calculateLowerCriterionsPriorityVector("2 5 3");
		
		assertEquals(test[0], new Double(0.4163114848181448));
		assertEquals(test[1], new Double(0.3436575589372035));
		assertEquals(test[2], new Double(0.24003095624465184));
		
		test = AhpMaths.calculateLowerCriterionsPriorityVector("2 5 3 4 2 5");
		
		assertEquals(test[0], new Double(0.2907433986585326));
		assertEquals(test[1], new Double(0.26419646805558833));
		assertEquals(test[2], new Double(0.2544202178821889));
		assertEquals(test[3], new Double(0.19063991540369019
));
	}

	@Test
	public void testSumVectorsWithWeights()
	{

		ArrayList<Double[]> testArray = new ArrayList<Double[]>();
		Double[] test1 = AhpMaths.calculateLowerCriterionsPriorityVector("2 5 3");
		Double[] test2 = AhpMaths.calculateLowerCriterionsPriorityVector("4 3 1");
		Double[] test3 = AhpMaths.calculateLowerCriterionsPriorityVector("1 6 2");
		Double[] weights = AhpMaths.calculateLowerCriterionsPriorityVector("3 2 1");
		
		testArray.add(test1);
		testArray.add(test2);
		testArray.add(test3);
		
		Double[] toCheck = AhpMaths.sumVectorsWithWeights(testArray,weights);
		
		assertEquals(toCheck[0], new Double(0.4215571566044293));
		assertEquals(toCheck[1], new Double(0.32232456709218077));
		assertEquals(toCheck[2], new Double(0.2561182763033901));	
	}
}
