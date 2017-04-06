package pl.edu.agh.kis.math;

import java.util.ArrayList;

/**
 * Delivers static functions for calculating lower layer priority vectors
 * in AHP method for criterion and function with sums the priority vectors
 * of lower layers with weights calculated from pair relatives about this
 * layer for current criterion.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class SolverCalculator extends BasicAhpMath
{
	/**
	 * Transforms numbers from parameter into pair comparison matrix and
	 * uses geometric mean method to gain priority vector and then return it.
	 * In case of errors return null.
	 * 
	 * @param lowerLayerCrterionWeightsEntry table with double values,
	 * 		represents relatives ratios between lower layer choices.
	 * @return normalized vector with double values, represents strong of every
	 * 		lower layer choise or null if there were any error.
	 */
	public static Double[] calculateLowerCriterionsPriorityVector(
			Double[] lowerLayerCrterionWeights)
	{
		Double[][] pairCompareMatrix = createPairCompareMatrix(
				lowerLayerCrterionWeights);
		
		return createPriorityVectorGeometricMean(pairCompareMatrix);
	}
	
	/**
	 * Function sum the vectors from first argument, multiplied by weights from second
	 * argument.
	 * 
	 * @param vectorsToSum list of vectors to sum, result vector must be the same size.
	 * @param priorityVector vector with weights of lower layer it helps to well estimate
	 * 		the strong of every vector from first argument.
	 * @return normalized result vector for criterion which called method. If method
	 * 		get null parameters it returns null.
	 */
	public static Double[] sumVectorsWithWeights(ArrayList<Double[]> vectorsToSum,
			Double[] priorityVector)
	{
		if(vectorsToSum == null || priorityVector == null)
			return null;
		
		Double[] resultVector = new Double[vectorsToSum.get(0).length];
		for(int i = 0; i < resultVector.length; ++i)
			resultVector[i] = 0.0;
		
		for(int i = 0; i < resultVector.length; ++i)
		{
			for(int j = 0; j < vectorsToSum.size(); ++j)
			{
				resultVector[i] += vectorsToSum.get(j)[i]*priorityVector[j];
			}
		}
		
		return normalizeVector(resultVector);
	}
}