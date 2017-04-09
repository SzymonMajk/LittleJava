package pl.edu.agh.kis.creator;

import java.util.ArrayList;

import pl.edu.agh.kis.math.IndexCalculator;

/**
 * Represents and collects informations about single criterion or alternative,
 * allows to add or edit before final writing to file. Contains criterions or
 * alternative name, lower layer blocks layer number, vector with pair 
 * relatives of lower layer blocks. Could create lower layer blocks names 
 * and lower layer blocks weights in string separated by white space.
 * Alternatives are collect in layer 0, and the higher layer, the more top
 * criterion is.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
class Block 
{
	private Integer layerNumber;
	
	private String blockName;
	
	private ArrayList<Block> lowerLayerBlocks = new ArrayList<Block>();

	private ArrayList<Double> lowerLayerWeights = new ArrayList<Double>();
	
	private String parentName = "";
		
	private String prepareLowerLayerNames()
	{
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int i = 0; i < lowerLayerBlocks.size()-1; ++i)
		{
			nameBuilder.append(lowerLayerBlocks.get(i));
			nameBuilder.append(" ");
		}
		
		nameBuilder.append(lowerLayerBlocks.
				get(lowerLayerBlocks.size()-1));
		
		return nameBuilder.toString();
	}
	
	private String prepareLowerLayerWeightsEntry()
	{
		if(lowerLayerWeights.isEmpty())
			return "";
		
		StringBuilder weightBuilder = new StringBuilder();
		
		for(int i = 0; i < lowerLayerWeights.size()-1; ++i)
		{
			weightBuilder.append(lowerLayerWeights.get(i));
			weightBuilder.append(" ");
		}
		
		weightBuilder.append(lowerLayerWeights.
				get(lowerLayerWeights.size()-1));
		
		return weightBuilder.toString();
	}
	
	private Double[] prepareLowerLayerWeights()
	{
		if(lowerLayerWeights.isEmpty())
			return null;
		
		Double[] result = new Double[lowerLayerWeights.size()];
		
		for(int i = 0; i < lowerLayerWeights.size(); ++i)
		{
			result[i] = lowerLayerWeights.get(i);
		}
		
		return result;
	}
	
	private void addColumnToLowerLayerWeights(ArrayList<Double> column)
	{
		int rowWidth = column.size()-1;
		int currentPosition = rowWidth;

		for(int i = 0; i <= rowWidth; ++i)
		{
			lowerLayerWeights.add(currentPosition, column.get(i));
			currentPosition += rowWidth-i;
		}	
	}
	
	private boolean checkConsistency(Double[] lowerLayerWeights)
	{
		System.out.println("Start to check consistency.");
		
		Double index = IndexCalculator.
				checkMeanConsistencyIndex(lowerLayerWeights);
		
		System.out.println("Consistency has been checked = " + index);
		
		return true;
	}
	
	/**
	 * Lets return information about layer in witch asked block is located.
	 * Layer number is formalized during creation of the block.
	 * 
	 * @return index of layer in witch current block is located.
	 */
	public Integer getLayerNumber()
	{
		return layerNumber;
	}
	
	/**
	 * trrr
	 * 
	 * @return
	 */
	public Integer getLowerLayerBlocksNumber()
	{
		return lowerLayerBlocks.size();
	}
	
	/**
	 * Lets return name of current block. Block name must be unique.
	 * 
	 * @return name of the current block or null if proposed name was null.
	 */
	public String getBlockName()
	{
		return blockName;
	}
	
	/**
	 * Return name of current parent block. In case block is not connected
	 * to the parent block, return empty String.
	 * 
	 * @return name of parent block or empty String if block is not connected.
	 */
	public String getParentName()
	{
		return parentName;
	}
	
	/**
	 * Create a single String with lower layer blocks names in order existed
	 * in lowerLayerBlocks list and return it. Should be used during writing to 
	 * file or displaying informations about block.
	 * 
	 * @return String with names of lower layer blocks, separated with white
	 * 		space.
	 */
	public String getLowerLayerBlockNames()
	{
		return prepareLowerLayerNames();
	}
	
	/**
	 * Create a single String with lower layer blocks relatives in order existed
	 * in lowerLayerBlocks list and return it. Should be used during writing to file or
	 * displaying informations about block. Return empty String if weights array
	 * is empty, which mean that consistency has not been calculated yet.
	 * 
	 * @return String with relatives of lower layer blocks, separated with white
	 * 		space, or empty String if weight array is empty.
	 */
	public String getlowerLayerWeights()
	{
		return prepareLowerLayerWeightsEntry();
	}
	
	/**
	 * Specify name of block from upper layer which is connected to the current
	 * block. Should be used to check if all blocks from center layers have
	 * parent block.
	 * 
	 * @param parentName String specify name of parent block for current block.
	 */
	public void setParentName(String parentName)
	{
		this.parentName = parentName;
	}
	
	/**
	 * Create connection beetwen current block and lower layer block, where
	 * current block become parent block for lower layer block from parameter.
	 * The difference beetwen layers must equal one. Also method check if
	 * proposed block is alternative, do not specify parent name for it,
	 * otherwise set as parent name name of current block.
	 * 
	 * @param lowerLayerBlock block that should be connected to current block
	 * 		as lower layer block.
	 * @return true only if difference beetwen layer of blocks is equal one,
	 * 		otherwise return false.
	 */
	public boolean addLowerLayerBlock(Block lowerLayerBlock)
	{
		if(layerNumber - 1 != lowerLayerBlock.getLayerNumber())
			return false;
		
		if(lowerLayerBlock.getLayerNumber() != 0)
			lowerLayerBlock.setParentName(blockName);
		
		lowerLayerBlocks.add(lowerLayerBlock);
		return true;
	}
	
	/**
	 * Clears connection beetwen lower layer block specified in parameter
	 * if this block is in lower layer blocks list of current block. Delete
	 * parent name in specified block and tries to remove specified block from
	 * list. Pair weight relatives are not set in this step yet, so this method
	 * does not clear or change lowerLayerWeights.
	 * 
	 * @return true only if block from parameter was in list and has been 
	 * 		removed with seting null at its parent name component.
	 */
	public boolean deleteLowerLayerBlock(Block lowerLayerBlock)
	{
		if(lowerLayerBlocks.contains(lowerLayerBlock))
		{
			lowerLayerBlock.setParentName("");
			return lowerLayerBlocks.remove(lowerLayerBlock);
		}
		
		return false;
	}
	
	/**
	 * Check if current block have parent block in layer. Blocks from highest
	 * layer and layer zero should not call this method, return 
	 * information will be inappropriate.
	 * 
	 * @return true if block have connection to the parent block.
	 */
	public boolean checkConnectionWithParent()
	{
		return parentName != null;
	}
	
	/**
	 * Allow user to set relatives between all lower layer block in order
	 * of lower layer block list, checking consistency of created matrix
	 * after adding every new row and column if necessary informs user
	 * about inconsistency and ask him again about previous row and column.
	 */
	public void setPairRelatives(ArrayList<Double> column)
	{
		addColumnToLowerLayerWeights(column);
		if(column.size() > 1)
			checkConsistency(prepareLowerLayerWeights());
	}
	
	/**
	 * Block names must be unique. Layer number equal to zero means, that block
	 * is an alternative, every higher positive number means the level above
	 * alternatives. There should be only one block on top.
	 * 
	 * @param layerNumber layer of hierarchy in witch created block should be
	 * 		located.
	 * @param blockName unique short name without white spaces, which identify
	 * 		created block for other elements of hierarchy.
	 */
	public Block(Integer layerNumber, String blockName)
	{
		this.layerNumber = layerNumber;
		this.blockName = blockName;
	}
}