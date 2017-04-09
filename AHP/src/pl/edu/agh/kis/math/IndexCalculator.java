package pl.edu.agh.kis.math;

/**
 * Class delivers method calculating consistency index using geometric mean
 * method. Every consistency index have it own range and it own meaning.
 * Methods do not clarify meaning of provided pair weight numbers, but
 * trying to calculate user coherence and return number. It is crucial
 * to send numbers in order, just like in pair weight comparison matrix,
 * the top triangle, row by row, from the left top element.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class IndexCalculator extends BasicAhpMath
{	
	private static Double calculateGlobalConsistencyIndex(
			Double[][] localQuantificatorsMatrix)
	{
		
		Double quadraticSum = new Double(0.0);
		int n = localQuantificatorsMatrix.length;
		
		for(int i = 0; i < n-1; ++i)
		{
			for(int j = i+1; j < n; ++j)
			{
				quadraticSum += Math.pow(Math.
						log(localQuantificatorsMatrix[i][j]),2);
			}
		}
		
		return ((2 *quadraticSum) / ((n-1)*(n-2)));
	}
	
	private static Double[][] createLocalQuantificatorsMatrix(
			Double[][] pairCompareMatrix,Double[] priorityVector)
	{		
		for(int i = 0; i < pairCompareMatrix.length; ++i)
		{
			for(int j = 0; j < pairCompareMatrix.length; ++j)
			{
				pairCompareMatrix[i][j] = pairCompareMatrix[i][j] * 
						priorityVector[j] /priorityVector[i];
			}
		}
		
		return pairCompareMatrix;
	}
	
	/**
	 * Help to assume that pair weight matrix created using numbers in order
	 * provided in parameter is consistent or not using number from zero to 
	 * one, the highest number, the more inconsistent matrix is. Method do not
	 * check the order of elements, only the number of elements to create
	 * quadratic matrix, so used relatives have to be in order to lower layer
	 * criterions, or we can see it as numbers in top triangle of matrix, 
	 * row by row.
	 * 
	 * @param lowerLayerRelatives numbers comparing every lower layer criterion,
	 * 		method will check size of vector, but does not check theirs meaning.
	 * @return high precision number from zero to one inform about consistency
	 * 		of matrix created from providing vector, the lower result, the
	 * 		higher consistent matrix is.
	 */
	public static Double checkMeanConsistencyIndex(Double[] lowerLayerWeights)
	{
		Double[][] pairCompareMatrix = createPairCompareMatrix(
				lowerLayerWeights);
		
		Double[][] localQuantificatorsMatrix = createLocalQuantificatorsMatrix(
				pairCompareMatrix,SolverCalculator.
				calculateLowerCriterionsPriorityVector(lowerLayerWeights));
		
		return calculateGlobalConsistencyIndex(localQuantificatorsMatrix);
	}
}