package pl.edu.agh.kis;

import java.util.ArrayList;

/**
 * Class collects all maths static methods for project classes, which need
 * to calculate pair weight matrix and return priority vector. Class lets
 * to use two methods outside and hidden private methods with actualy
 * force calculations. Functions inform about error on standard error output.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class AhpMaths 
{
	private static Double[][] createPairCompareMatrix(
			String[] lowerLayerCriterionWeightsEntry)
	{
		int n = 2;
		
		while( (n-1)*n/2 < lowerLayerCriterionWeightsEntry.length ) 
		{
			n++;
		}
		
		if((n-1)*n/2 != lowerLayerCriterionWeightsEntry.length)
		{
			System.err.println("Niezgodyn rozmiar wektora porównañ " +
					lowerLayerCriterionWeightsEntry.length);
			return null;
		}
		
		Double[][] pairCompareMatrix = new Double[n][n];
		int vectorNumberCounter = 0;
		
		for(int i = 0; i < n; ++i)
		{
			for(int j = 0; j < n; ++j)
			{
				if(i == j)
					pairCompareMatrix[i][j] = 1.0;
				
				else if(j > i)
					pairCompareMatrix[i][j] = 
						Double.parseDouble(
						lowerLayerCriterionWeightsEntry[vectorNumberCounter++]);
				else
					pairCompareMatrix[i][j] = 1/pairCompareMatrix[j][i];
			}
		}
		
		return pairCompareMatrix;
	}
	
	private static Double[] createPriorityVectorSimple(Double[][] pairWeightMatrix)
	{
		if(pairWeightMatrix == null || pairWeightMatrix.length == 0)
			return null;
		
		Double[] priorityVector = new Double[pairWeightMatrix.length];
		
		for(int i = 0; i < pairWeightMatrix.length; ++i)
			priorityVector[i] = pairWeightMatrix[i][pairWeightMatrix.length-1];
		
		return normalizeVector(priorityVector);
	}
	
	/*private static Double[] createPriorityVectorGeometricMean(
			Double[][] pairWeightMatrix)
	{
		if(pairWeightMatrix == null || pairWeightMatrix.length == 0)
			return null;
		
		Double[] priorityVector = new Double[pairWeightMatrix.length];
		
		for(int i = 0; i < pairWeightMatrix.length; ++i)
			priorityVector[i] = pairWeightMatrix[i][pairWeightMatrix.length-1];
		
		return normalizeVector(priorityVector);
	}*/
	
	private static Double[] normalizeVector(Double[] vector)
	{
		Double[] normalized = new Double[vector.length];
		Double sum = 0.0;
		
		for(Double d : vector)
			sum += d;
		
		for(int i = 0; i < vector.length; ++i)
			normalized[i] = vector[i]/sum;
		
		return normalized;
	}
	
	/**
	 * Function transform numbers from String into pair comparison matrix
	 * and use geometric mean method to gain priority vector which is returned.
	 * If there are any errors, user is informed about it on standard error output
	 * and function return null.
	 * 
	 * @param lowerLayerCriterionWeightsEntry String with double values, separated
	 * 		by space, represents relatives ratios between lower layer choices.
	 * @return normalized vector with double values, represents strong of every
	 * 		lower layer choise or null if there were any error.
	 */
	public static Double[] calculateLowerCriterionsPriorityVector(
			 String lowerLayerCriterionWeightsEntry)
	{	
		Double[][] pairCompareMatrix = createPairCompareMatrix(
				lowerLayerCriterionWeightsEntry.split(" "));
		
		return createPriorityVectorSimple(pairCompareMatrix);
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