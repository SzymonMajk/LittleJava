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
		Double[] test1 = SolverCalculator.calculateLowerCriterionsPriorityVector(data1);
		Double[] data2 = {2.0,5.0,3.0,4.0,2.0,5.0};
		Double[] test2 = SolverCalculator.calculateLowerCriterionsPriorityVector(data2);
		Double[] data3 = {0.5,0.25,3.0,0.5,2.0,2.0};
		Double[] test3 = SolverCalculator.calculateLowerCriterionsPriorityVector(data3);
		
		assertEquals(test1[0], new Double(0.581552066851616));
		assertEquals(test1[1], new Double(0.3089956436328642));
		assertEquals(test1[2], new Double(0.10945228951551983));
	
		assertEquals(test2[0], new Double(0.47870096526524175));
		assertEquals(test2[1], new Double(0.2892670638286677));
		assertEquals(test2[2], new Double(0.14463353191433384));
		assertEquals(test2[3], new Double(0.08739843899175663));
		
		assertEquals(test3[0], new Double(0.17354986138009285));
		assertEquals(test3[1], new Double(0.26373875583296336));
		assertEquals(test3[2], new Double(0.4435539486868272));
		assertEquals(test3[3], new Double(0.11915743410011669));
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
		
		assertEquals(toCheck[0], new Double(0.5770226243428465));
		assertEquals(toCheck[1], new Double(0.292651628862838));
		assertEquals(toCheck[2], new Double(0.1303257467943155));	
	}
}