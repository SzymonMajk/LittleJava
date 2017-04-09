package pl.edu.agh.kis.creator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Uses XmlFormatFileCreator to write data collected in Block's with lower
 * layer pair weights consistency estimated using geometric mean index and
 * provide methods to put user through completing informations.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class Proceeder
{
	private HashMap<Integer,ArrayList<Block>> hierarchy 
				= new HashMap<Integer,ArrayList<Block>>();
	
	private void displayHierarchyCreate()
	{
		System.out.println("\nCurrent Hierarchy:");
		for(int i = hierarchy.size()-1; i >= 0 ; --i)
		{
			ArrayList<Block> blocks = hierarchy.get(new Integer(i));
			
				System.out.print("Layer" + i +": ");
				for(Block b : blocks)
					System.out.print(b.getBlockName() + " ");
				
			System.out.println();
		}
		System.out.println("!!!------------------------------------!!!");
	}
	
	private void displayHierarchyConnect()
	{
		System.out.println("\nCurrent Hierarchy:");
		
		ArrayList<Block> blocks = hierarchy.get(
				new Integer(hierarchy.size()-1));
		System.out.print("Layer" + (hierarchy.size()-1) +": ");
		for(Block b : blocks)
			System.out.println(b.getBlockName());
		for(int i = hierarchy.size()-2; i >= 1 ; --i)
		{
			blocks = hierarchy.get(new Integer(i));
			
				System.out.print("Layer" + i +": ");
				for(Block b : blocks)
					System.out.print(b.getBlockName() + "{parent-" + 
				b.getParentName() + "} ");
				
			System.out.println();
		}
		blocks = hierarchy.get(
				new Integer(0));
		System.out.print("Layer" + 0 + ": ");
		for(Block b : blocks)
			System.out.print(b.getBlockName() + " ");
		System.out.println("\n!!!------------------------------------!!!");
	}
	
	private void displayHierarchyWithIndexes()
	{
		System.out.println("\nCurrent Hierarchy:");
		for(int i = hierarchy.size()-1; i >= 1 ; --i)
		{
			ArrayList<Block> blocks = hierarchy.get(new Integer(i));
			
				System.out.print("Layer" + i +": ");
				for(Block b : blocks)
				{
					System.out.print(b.getBlockName() );
					if(b.getlowerLayerWeights().equals(""))
						System.out.print("[setRelatives] ");
				}
				
			System.out.println();
		}
		ArrayList<Block> blocks = hierarchy.get(new Integer(0));
		
		System.out.print("Layer" + 0 +": ");
		for(Block b : blocks)
			System.out.print(b.getBlockName() + " ");
		System.out.println("\n!!!------------------------------------!!!");
	}
	
	private void addLayer()
	{
		hierarchy.put(new Integer(hierarchy.size()), new ArrayList<Block>());
	}
	
	private boolean couldDeleteLayer()
	{
		return hierarchy != null && 
				hierarchy.get(new Integer(hierarchy.size()-1)) != null &&
				hierarchy.get(new Integer(hierarchy.size()-1)).isEmpty();
	}
	
	private void deleteLayer()
	{
		hierarchy.remove(hierarchy.size()-1);
	}
	
	private boolean checkLayers()
	{
		for(ArrayList<Block> blocks : hierarchy.values())
			if(blocks == null || blocks.isEmpty())
				return false;
		return true;
	}
	
	private void addBlock(Integer layer, String blockName)
	{
		if(layer >= 0 && layer < hierarchy.size())
			hierarchy.get(layer).add(new Block(layer,blockName));
	}
	
	private void deleteBlock(Integer layer, int index)
	{
		if(layer >= 0 && layer < hierarchy.size())
			if(index >= 0 && index < hierarchy.get(layer).size())
				hierarchy.get(layer).remove(index);
	}
	
	private void addConnection(Integer aboveLayer, Integer aboveIndex,
			Integer belowLayer, Integer belowIndex)
	{
		if(aboveIndex*belowIndex >= 0 && 
			aboveLayer*belowLayer > 0 &&
			aboveLayer < hierarchy.size() && 
			aboveLayer.equals(belowLayer + 1) && 
			hierarchy.get(aboveLayer).size() > aboveIndex &&
			hierarchy.get(belowLayer).size() > belowIndex)
		{
			Block upper = hierarchy.get(aboveLayer).get(aboveIndex);
			Block below = hierarchy.get(belowLayer).get(belowIndex);
			
			upper.addLowerLayerBlock(below);
		}
	}
	
	private void deleteConnection(Integer aboveLayer, Integer aboveIndex,
			Integer belowLayer, Integer belowIndex)
	{
		if(aboveIndex*belowIndex >= 0 && 
			aboveLayer*belowLayer > 0 &&
			aboveLayer.equals(belowLayer + 1) && 
			hierarchy.get(aboveLayer).size() > aboveIndex &&
			hierarchy.get(belowLayer).size() > belowIndex)
		{
			Block upper = hierarchy.get(aboveLayer).get(aboveIndex);
			Block below = hierarchy.get(belowLayer).get(belowIndex);
			
			upper.deleteLowerLayerBlock(below);
		}
	}
	
	private void addGoalConnections()
	{
		ArrayList<Block> blocks = hierarchy.get(new Integer(hierarchy.size()-2));
		Block goal = hierarchy.get(new Integer(hierarchy.size()-1)).get(0);
		
		for(Block b : blocks)
		{
			goal.addLowerLayerBlock(b);
		}
	}
	
	private void addAlternativesConnections()
	{
		ArrayList<Block> alternatives = hierarchy.get(new Integer(0));
		ArrayList<Block> upper = hierarchy.get(new Integer(1));
		
		for(Block b : upper)
		{
			for(Block alt : alternatives)
			{
				b.addLowerLayerBlock(alt);
			}
		}
	}
	
	private boolean checkConnections()
	{
		for(int i = 1; i < hierarchy.size()-1; ++i)
		{
			for(Block b : hierarchy.get(i))
				if(b.getParentName().equals(""))
					return false;
		}
		return true;
	}

	private boolean couldSetRelatives(Integer Layer, int index)
	{
		return Layer > 0 && Layer < hierarchy.size() && index >= 0 &&
			index < hierarchy.get(Layer).size();
	}
	
	private void setRelatives(Integer Layer, int index)
	{
		Block currentBlock = hierarchy.get(Layer).get(index);
		Double userDecision = 0.0;
		ArrayList<Double> column = new ArrayList<Double>();
		
		for(int i = 1; i < currentBlock.getLowerLayerBlocksNumber(); ++i)
		{
			for(int j = 0; j < i; ++j)
			{
				System.out.print("Add ratio(" + (j+1)  + "," + (i+1) +"): ");
				userDecision = 
						Double.parseDouble(new Scanner(System.in).nextLine());
				column.add(userDecision);
			}
			System.out.print("Check inconcistency Index for so far ratios:");
			for(Double d : column)
				System.out.print(d + " ");
			System.out.println();
			
			hierarchy.get(Layer).get(index).setPairRelatives(column);
			column.clear();
		}
		
		System.out.println("All subcriterions relations for " + 
				currentBlock.getBlockName() + " have been set.");
	}
	
	private boolean checkRelativesSet()
	{
		for(ArrayList<Block> blocks : hierarchy.values())
		{
			for(Block b : blocks)
				if(b.getlowerLayerWeights().equals(""))
					return false;
		}
		return true;
	}
	
	/**
	 * trr
	 */
	public XmlFormatFileCreator fileCretor = 
			new XmlFormatFileCreator("DefaultHierarchy2.xml");
	
	public HashMap<Integer,ArrayList<Block>> getHierarchy()
	{
		return hierarchy;
	}
	
	/**
	 * First step of the program, lets user to add or reduce number of layers,
	 * allows to delete only layers from highest level. Looks for empty layers
	 * and inform about it. Allow to add or delete block.
	 * 
	 * @return true if created hierarchy have no empty layers and top layer
	 * 		has only one block.
	 */
	public boolean createHierarchy()
	{
		displayHierarchyCreate();
		
		addLayer();
		addLayer();
		addLayer();
		addLayer();
		addLayer();
		
		displayHierarchyCreate();
		
		if(couldDeleteLayer())
			deleteLayer();
		
		addBlock(0,"Budka");
		addBlock(0,"Sto³ówka");
		addBlock(1,"Cena");
		addBlock(1,"Iloœæ");
		addBlock(1,"Wystrój");
		addBlock(1,"Obs³uga");
		addBlock(1,"Odleg³oœæ");
		addBlock(2,"Jedzenie");
		addBlock(2,"Miejsce");
		addBlock(3,"Goal");
		addBlock(3,"ToDelete");
		deleteBlock(3,1);	
		
		displayHierarchyCreate();
		
		if(checkLayers())
			return true;
		return false;
	}
	
	/**
	 * Second step of the program, lets user to specify connections between
	 * block in center layers. Every block from layer zero is automatically
	 * added to layers from layer one and block from highest layer is also
	 * automatically connect with layers from layer highest minus one. This
	 * connection means adding to the list of lower layer connections.
	 * 
	 * @return true if at the end every block without blocks from layer zero
	 * 		and layer one have connection to block from higher layer, by its
	 * 		parent name.
	 */
	public boolean createConnections()
	{
		displayHierarchyConnect();
		
		addGoalConnections();
		addAlternativesConnections();
		
		displayHierarchyConnect();
		
		addConnection(2,0,1,0);
		addConnection(2,0,1,1);
		addConnection(2,0,1,2);
		deleteConnection(2,0,1,2);
		addConnection(2,1,1,2);
		addConnection(2,1,1,3);
		addConnection(2,1,1,4);	
		
		displayHierarchyConnect();
		if(checkConnections())
			return true;
		return false;
	}
	
	/**
	 * Third step of the program, lets user to estimate relatives for every
	 * block, where relatives means the ratio between weights of compare every
	 * lower layer block each other. Before adding next block, calculate
	 * inconsistency index and require to repeat estimation if index is to
	 * high.
	 * 
	 * @return true if all blocks from layer higher than zero have consistent
	 * 		pair weight ratio.
	 */
	public boolean createRelatives()
	{
		displayHierarchyWithIndexes();
		
		/*if(couldSetRelatives(3,0))
			setRelatives(3,0);
		if(couldSetRelatives(2,0))
			setRelatives(2,0);*/
		if(couldSetRelatives(2,1))
			setRelatives(2,1);
		/*if(couldSetRelatives(1,0))
			setRelatives(1,0);
		if(couldSetRelatives(1,1))
			setRelatives(1,1);
		if(couldSetRelatives(1,2))
			setRelatives(1,2);
		if(couldSetRelatives(1,3))
			setRelatives(1,3);*/
		
		displayHierarchyWithIndexes();
		
		if(checkRelativesSet())
			return true;
		
		return false;
	}
	
	/**
	 * Sample use of Proceeder with terminal communication with user.
	 * 
	 * @param args in current version do not used
	 */
	public static void main(String[] args)
	{
		Proceeder p = new Proceeder();
		
		if(p.createHierarchy() && p.createConnections() &&
				p.createRelatives())
			p.fileCretor.writeData(p.getHierarchy());
	}
}