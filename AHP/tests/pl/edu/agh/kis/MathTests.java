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
		
		assertEquals(test[0], new Double(0.5555555555555556));
		assertEquals(test[1], new Double(0.3333333333333333));
		assertEquals(test[2], new Double(0.1111111111111111));
		
		test = AhpMaths.calculateLowerCriterionsPriorityVector("2 5 3 4 2 5");
		
		assertEquals(test[0], new Double(0.2727272727272727));
		assertEquals(test[1], new Double(0.18181818181818182));
		assertEquals(test[2], new Double(0.45454545454545453));
		assertEquals(test[3], new Double(0.09090909090909091));
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
		
		assertEquals(toCheck[0], new Double(0.5944444444444444));
		assertEquals(toCheck[1], new Double(0.27222222222222225));
		assertEquals(toCheck[2], new Double(0.13333333333333333));	
	}
}
