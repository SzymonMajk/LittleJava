package pl.edu.agh.kis.solver;

import java.util.ArrayList;
import pl.edu.agh.kis.math.AhpMaths;

/**
 * Criterion storage info about single cryterion. Have method to add references to 
 * all cryterion from lower layer, which should be attached to this cryterion, with 
 * weights of this lower cryterions in this cryterion, so after calculate the pair
 * comparison matrix and gets all the lower result vectors, this criterion
 * could sum all lower vectors regarding theirs weight. After this calculation,
 * criterion could return its own result vector to upper layer or if there is 
 * no upper layer, this vector is answer.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 * 
 */
public class Criterion 
{
	private String criterionName = "";
	
	private String lowerCriterionNames = "";
	
	private String lowerLayerCriterionWeightsEntry = "";
	
	private Integer layer;
	
	private Double[] resultVector;
	
	private ArrayList<Criterion> lowerLayerCriterions 
		= new ArrayList<Criterion>();
	
	private void calculateResultAlternativeVector(String alternativePriorityVector)
	{
		String[] splited = alternativePriorityVector.split(" ");
		resultVector = new Double[splited.length];
		
		for(int i = 0; i < splited.length; ++i)
		{
			resultVector[i] = Double.parseDouble(splited[i]);
		}
	}
	
	/**
	 * Return String object with criterion name.
	 * 
	 * @return String object with criterion name.
	 */
	public String getCriterionName()
	{
		return criterionName;
	}
	
	/**
	 * Return Integer object with layer value.
	 * 
	 * @return Integer object with layer value.
	 */
	public Integer getLayer()
	{
		return layer;
	}
	
	/**
	 * Return result vector. If it was not yet calculated,
	 * returns null.
	 * 
	 * @return double array with result or null if method called before
	 * 		calculations.
	 */
	public Double[] getResultVector()
	{
		return resultVector;
	}
	
	/**
	 * Connect all criterion from lower criterion list, which has names
	 * from this criterion sub criterion names String.
	 * 
	 * @param lowerCriterions list witch criterion from one level lower
	 * 		criterion list.
	 * @return true only if all criterions from sub criterion String where
	 * 		where found in lower Criterion List, false any other case.
	 */
	public boolean correctAssociateLowerCriterions(CriterionList lowerCriterions)
	{
		String[] splitedLowerCriterionNames = lowerCriterionNames.split(" ");

		for(String criterionName : splitedLowerCriterionNames)
		{
			//System.out.println("Szukam podkrytium o nazwie "+criterionName);
			if(lowerCriterions.hasCriterionByName(criterionName))
			{
				//System.out.println("    zosta³o znalezione. ");
				lowerLayerCriterions.
				add(lowerCriterions.getCriterionByName(criterionName));
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Function proceed math functions to correctly calculate result vector
	 * for this criterion.
	 * 
	 * @return true only if there where no error during calculating especially,
	 * if any of lower Layer result vector were null or size of lower layer criternion
	 * weights and lower layer criterion number is different, false in any other case.
	 */
	public boolean calculateResultCriterionVector()
	{
		Double[] lowerLayerCriterionWeightsValuesVector = 
				AhpMaths.calculateLowerCriterionsPriorityVector(
						lowerLayerCriterionWeightsEntry);
		
		if(lowerLayerCriterionWeightsValuesVector == null)
			return false;
		
		if(lowerLayerCriterionWeightsValuesVector.length != 
				lowerLayerCriterions.size())
			return false;
		
		ArrayList<Double[]> lowerLayerResultVectors = new ArrayList<Double[]>();
		
		for(Criterion c : lowerLayerCriterions)
		{
			lowerLayerResultVectors.add(c.getResultVector());
		}
		
		resultVector = AhpMaths.sumVectorsWithWeights(lowerLayerResultVectors,
				lowerLayerCriterionWeightsValuesVector);
				
		return true;
	}
	
	/**
	 * Simple method to easy show all current details of Criterion, such as
	 * name, names of sub criterions, layer, lower layer weights and priority
	 * vector, if any of information is not yet initialized by user it is not shown.
	 */
	@Override
	public String toString()
	{
		StringBuilder criterionPrintBuilder = new StringBuilder();
		
		criterionPrintBuilder.append("Name == ");
		criterionPrintBuilder.append(criterionName);
		criterionPrintBuilder.append("\n");
		if(!lowerCriterionNames.equals(""))
		{
			criterionPrintBuilder.append("Sub criterions from file == ");
			criterionPrintBuilder.append(lowerCriterionNames);
			criterionPrintBuilder.append("\n");
		}
		if(lowerLayerCriterions.size() != 0)
		{
			criterionPrintBuilder.append("Sub criterions from list == ");
			
			for(int i = 0; i < lowerLayerCriterions.size()-1; ++i)
			{
				criterionPrintBuilder.
				append(lowerLayerCriterions.get(i).getCriterionName());
				criterionPrintBuilder.append(" ");
			}	
			criterionPrintBuilder.
			append(lowerLayerCriterions.
					get(lowerLayerCriterions.size()-1).getCriterionName());	
			criterionPrintBuilder.append("\n");
		}
		criterionPrintBuilder.append("Layer == ");
		criterionPrintBuilder.append(layer);
		criterionPrintBuilder.append("\n");
		if(!lowerLayerCriterionWeightsEntry.equals(""))
		{
			criterionPrintBuilder.append("Lower Layer Weights == ");
			criterionPrintBuilder.append(lowerLayerCriterionWeightsEntry);
			criterionPrintBuilder.append("\n");
		}
		if(resultVector != null)
		{
			criterionPrintBuilder.append("Priority Vector == ");
			for(int i = 0; i < resultVector.length-1; ++i)
			{
				criterionPrintBuilder.append(resultVector[i]);
				criterionPrintBuilder.append(" ");
			}
			criterionPrintBuilder.append(resultVector[resultVector.length-1]);
			criterionPrintBuilder.append("\n");
		}
		return criterionPrintBuilder.toString();
	}
	
	/**
	 * trr
	 * 
	 * @param criterionName
	 * @param parentCriterionName
	 * @param layer
	 * @param currentLayerWeights
	 * @param lowerLayerCriterionWeightsEntry
	 */
	Criterion(String criterionName,  Integer layer, String lowerCriterionNames,
			String lowerLayerCriterionWeightsEntry)
	{
		this.criterionName = criterionName;
		this.layer = layer;
		this.lowerCriterionNames = lowerCriterionNames;
		this.lowerLayerCriterionWeightsEntry = lowerLayerCriterionWeightsEntry;
	}
	
	/**
	 * trr
	 * 
	 * @param criterionName
	 * @param layer
	 * @param currentLayerWeights
	 */
	Criterion(String criterionName, Integer layer,
			String alternativePriorityVector)
	{
		this.criterionName = criterionName;
		this.layer = layer;
		calculateResultAlternativeVector(alternativePriorityVector);
	}
}