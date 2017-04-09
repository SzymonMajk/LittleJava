package pl.edu.agh.kis.math;

/**
 * Delivers simple methods operating with vectors and matrixes, used in more
 * specialized classes, which should extend it.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class BasicAhpMath {

	private static int calculateMatrixSizeFromWeightsEntrySize(int vectorSize)
	{
		int matrixSize = 2;
		
		while((matrixSize-1)*matrixSize/2 < vectorSize) 
		{
			++matrixSize;
		}
		
		return matrixSize;
	}
	
	/**
	 * Create and return pair relatives matrix using weights delivered in
	 * parameter. Check if the parameter has correct length and inform about
	 * incorrect data with null return. 
	 * 
	 * @param lowerLayerCriterionWeights numbers stored in tables, length of
	 * 		table is checked before matrix creation start.
	 * @return pair relatives matrix stored in double table of table or null
	 * 		in case, the length of the parameter is incorrect.
	 */
	protected static Double[][] createPairCompareMatrix(
			Double[] lowerLayerCriterionWeights)
	{
		int matrixSize = calculateMatrixSizeFromWeightsEntrySize(
				lowerLayerCriterionWeights.length);
		
		if((matrixSize-1)*matrixSize/2 != 
				lowerLayerCriterionWeights.length)
		{
			System.err.println("Incorrect size of the weights vector " +
					lowerLayerCriterionWeights.length);
			return null;
		}
		
		Double[][] pairCompareMatrix = new Double[matrixSize][matrixSize];
		int vectorNumberCounter = 0;
		
		for(int i = 0; i < matrixSize; ++i)
		{
			for(int j = 0; j < matrixSize; ++j)
			{
				if(i == j)
					pairCompareMatrix[i][j] = 1.0;
				
				else if(j > i)
					pairCompareMatrix[i][j] = 
						lowerLayerCriterionWeights[vectorNumberCounter++];
				else
					pairCompareMatrix[i][j] = 1/pairCompareMatrix[j][i];
			}
		}
		
		return pairCompareMatrix;
	}
	
	/**
	 * Method normalize delivered vector by dividing every element by sum
	 * of all elements. Returns null in case of null parameter or zero lenght
	 * vector.
	 * 
	 * @param vector table of doubles to normalize.
	 * @return normalized new created vector or null in case of zero length
	 * 		parameter or null parameter.
	 */
	protected static Double[] normalizeVector(Double[] vector)
	{
		if(vector == null || vector.length == 0)
			return null;
		
		Double[] normalized = new Double[vector.length];
		Double sum = 0.0;
		
		for(Double d : vector)
			sum += d;
		
		for(int i = 0; i < vector.length; ++i)
			normalized[i] = vector[i]/sum;
		
		return normalized;
	}
	
	/**
	 * Easiest way to deriver priority vector, return null in case parameter
	 * is null or have length equal to zero, otherwise return last normalized
	 * column of matrix gained in parameter.
	 * 
	 * @param pairWeightMatrix quadratic matrix of double values.
	 * @return normalized last column from delivered matrix or null in case
	 * 		of null or empty parameter.
	 */
	protected static Double[] createPriorityVectorSimple(
			Double[][] pairWeightMatrix)
	{
		if(pairWeightMatrix == null || pairWeightMatrix.length == 0)
			return null;
		
		Double[] priorityVector = new Double[pairWeightMatrix.length];
		
		for(int i = 0; i < pairWeightMatrix.length; ++i)
			priorityVector[i] = pairWeightMatrix[i][pairWeightMatrix.length-1];
		
		return normalizeVector(priorityVector);
	}
	
	/**
	 * Uses geometric mean method to deliver normalized priority vector from
	 * matrix gained in parameter. Return null in case parameter is null or
	 * its length is equal to zero, otherwise return priority vector calculated
	 * with geometric mean method, using function Math.pow().
	 * 
	 * @param pairWeightMatrix quadratic matrix of double values.
	 * @return normalized last column from delivered matrix or null in case
	 * 		of null or empty parameter.
	 */
	protected static Double[] createPriorityVectorGeometricMean(
			Double[][] pairWeightMatrix)
	{
		if(pairWeightMatrix == null || pairWeightMatrix.length == 0)
			return null;
		
		Double[] priorityVector = new Double[pairWeightMatrix.length];
		Double result = new Double("1");
		
		for(int i = 0; i < pairWeightMatrix.length; ++i)
		{
			result = new Double("1");
			
			for(int j = 0; j < pairWeightMatrix.length; ++j)
			{
				result = result * pairWeightMatrix[i][j];
			}
			priorityVector[i] = 
					Math.pow(result, 1/(double)pairWeightMatrix.length);
		}
		
		return normalizeVector(priorityVector);
	}
}