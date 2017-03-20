package pl.edu.agh.kis;

import java.util.ArrayList;

/**
 * CriterionList objects storage Criterion objects with specific information
 * like layer number informs about type of Criterions, starts by 0 means alternatives
 * criterion and higher number means criterions closer to top. Have several method to
 * cooperate with criterions from cash.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class CriterionList implements Comparable<CriterionList>
{
	private ArrayList<Criterion> criterionCash = new ArrayList<Criterion>();
	
	private Integer layerNumber;
	
	/**
	 * Check if list holds criterion with name from parameter.
	 * 
	 * @param name String value with name of checking criterion.
	 * @return true if name was found and false in any other case.
	 */
	public boolean hasCriterionByName(String name)
	{
		for(Criterion c : criterionCash)
		{
			if(c.getCriterionName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Return criterion by name, this method should be called after calling
	 * hasCriterionByName() in other case, it could return null if criterion
	 * will not be found.
	 * 
	 * @param name String value with name of trying to find criterion.
	 * @return founded criterion or null if it not exist in cash.
	 */
	public Criterion getCriterionByName(String name)
	{
		for(Criterion c : criterionCash)
		{
			if(c.getCriterionName().equals(name))
				return c;
		}
		return null;
	}
	
	/**
	 * Check if index from parameter is in size of cash.
	 * 
	 * @param index int number we are trying to check.
	 * @return true if index is in size and false in any other case.
	 */
	public boolean hasCriterionByIndex(int index)
	{
		return index < criterionCash.size() && index >= 0;
	}
	
	/**
	 * Return criterion on index from parameter, this method should be 
	 * called after calling hasCriterionByIndex() in other case, it 
	 * could cause exception if index will be out of bound. 
	 *
	 * @param index int number we are trying to find.
	 * @return founded criterion.
	 */
	public Criterion getCriterionByIndex(int index)
	{
		return criterionCash.get(index);
	}
	
	/**
	 * Return the Integer with layerNumber.
	 * 
	 * @return Integer represents layerNumber.
	 */
	public Integer getLayerNumber()
	{
		return layerNumber;
	}
	
	/**
	 * Function uses result vector to find index of the best alternative for
	 * this criterion list. This method should be called only on the highest
	 * layer, to get a got result.
	 * 
	 * @return index of the best alternative for this criterion.
	 */
	public int getFinalAlternativeIndex()
	{
		int maxIndex = 0;
		Double[] resultVector = criterionCash.get(0).getResultVector();
		
		for(int i = 0; i < resultVector.length; ++i)
		{
			if(resultVector[i] > resultVector[maxIndex])
				maxIndex = i;
		}
		
		return maxIndex;
	}
	
	/**
	 * Adding criterion to cash. Problems could be caused only by container
	 * represents cash.
	 * 
	 * @param toAdd criterion which should be added.
	 */
	public void addCriterion(Criterion toAdd)
	{
		criterionCash.add(toAdd);
	}
	
	/**
	 * Let all criterions from list, to connect their sub criterions.
	 * 
	 * @param lowerCriterions list with criterions from one level lower layer.
	 * @return true if all connections where succesfuly, false in any other case.
	 */
	public boolean associateAllCriterions(CriterionList lowerCriterions)
	{
		for(Criterion c : criterionCash)
		{
			if(!c.correctAssociateLowerCriterions(lowerCriterions))
				return false;
		}
		return true;
	}
	
	/**
	 * Lets every criterion from criterionCash to proceed calculation
	 * and get result vector.
	 * 
	 * @return true if every calculation were succesful, false in every other
	 * 		case.
	 */
	public boolean proceedLayerCalculations()
	{
		for(Criterion c : criterionCash)
		{
			if(!c.calculateResultCriterionVector())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Function return String to print it from criterion from index.
	 * 
	 * @param index index of criterion which should be printed.
	 * @return String value which informations to print.
	 */
	public String printCriterionNameByIndex(int index)
	{
		return criterionCash.get(index).getCriterionName();
	}
	
	/**
	 * Function let every criterion from cash to print.
	 */
	public void printCriterions()
	{
	    for(Criterion c : criterionCash)
	    {
	    	System.out.println(c.toString());
	    }
	}
	
	/**
	 * Function to allow sorting Criterion List in List of Criterion Lists.
	 * 
	 * @param other Criterion List to compare with.
	 * @return result depends on layer number of current and parameter Criterion
	 * 		List, due to compareTo() methodology.
	 */
	@Override
	public int compareTo(CriterionList other) {
		 
        if(layerNumber.equals(other.getLayerNumber()))
        {
            return 0;
        }
        else if (layerNumber > other.getLayerNumber())
        {
            return 1;
        }
        else
        {
        	return -1;
        }
	}
	
	/**
	 * Help to create criterion list with adding layer number from parameter.
	 * Layer number will by used to compare criterion lists.
	 * 
	 * @param layerNumber number of layer for creating criterion list.
	 */
	CriterionList(int layerNumber)
	{
		this.layerNumber = layerNumber;
	}
}
