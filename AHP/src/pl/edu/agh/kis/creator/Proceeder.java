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
	
	private ArrayList<Block> alternatives = new ArrayList<Block>();
	
	private void displayHierarchyCreate()
	{
		System.out.println("\nCurrent Hierarchy:");
		for(int i = 0; i < hierarchy.size(); ++i)
		{
			ArrayList<Block> blocks = hierarchy.get(new Integer(i));
			
				System.out.print("Layer" + i +": ");
				for(Block b : blocks)
					System.out.print(b.getBlockName() + " ");
				System.out.println();
		}
		
		System.out.println("\nCurrent Alternatives: ");
		for(Block b : alternatives)
			System.out.print(b.getBlockName() + " ");
		System.out.println("\n!!!------------------------------------!!!");
	}
	
	private void displayHierarchyConnect()
	{
		System.out.println("\nCurrent Hierarchy:");
		
		ArrayList<Block> blocks = hierarchy.get(new Integer(0));
		System.out.print("Layer" + 0 +": ");
		for(Block b : blocks)
			System.out.println(b.getBlockName());
		for(int i = 1; i < hierarchy.size(); ++i)
		{
			blocks = hierarchy.get(new Integer(i));
			
				System.out.print("Layer" + i +": ");
				for(Block b : blocks)
					System.out.print(b.getBlockName() + "{parent-" + 
				b.getParentName() + "} ");
				
			System.out.println();
		}
		
		System.out.println("\nCurrent Alternatives: ");
		for(Block b : alternatives)
			System.out.print(b.getBlockName() + " ");
		System.out.println("\n!!!------------------------------------!!!");
	}
	
	private void displayHierarchyWithIndexes()
	{
		System.out.println("\nCurrent Hierarchy:");
		for(int i = 0; i < hierarchy.size(); ++i)
		{
			ArrayList<Block> blocks = hierarchy.get(new Integer(i));
			
				System.out.print("Layer" + i +": ");
				for(Block b : blocks)
				{
					System.out.print(b.getBlockName() + " ");
					if(b.getlowerLayerWeights().equals("\r"))
						System.out.print("[setRelatives] ");
				}
				
			System.out.println();
		}
		
		System.out.println("\nCurrent Alternatives: ");
		for(Block b : alternatives)
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
				hierarchy.get(new Integer(hierarchy.size()-1)).isEmpty() &&
				hierarchy.size() > 1;
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
	
	private boolean checkBlocks()
	{
		ArrayList<Block> blocks = null;
		
		if(hierarchy.size() < 2)
		{
			System.out.printf("You need at least one criterion layer.");
			return false;
		}
		
		for(int i = 1; i < hierarchy.size()-1; ++i)
		{
			blocks = hierarchy.get(i);
			if(blocks.size() < 2)
			{
				System.out.printf("Every criterion must have no subcriterion "
						+ "or more than two of them.");
				return false;
			}
		}
		
		blocks = hierarchy.get(0);
		if(blocks.size() != 1)
		{
			System.out.printf("Top layer must have one block.");
			return false;
		}
		
		if(alternatives.size() < 2)
		{
			System.out.printf("You need at least two alternatives.");
			return false;
		}
			
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
		if(aboveIndex*belowIndex >= 0 && aboveLayer*belowLayer >= 0 &&
			belowLayer < hierarchy.size() && aboveLayer < belowLayer && 
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
		ArrayList<Block> blocks = hierarchy.get(new Integer(1));
		Block goal = hierarchy.get(new Integer(0)).get(0);
		
		for(Block b : blocks)
		{
			goal.addLowerLayerBlock(b);
		}
	}
	
	private void addAlternativesConnections()
	{
		ArrayList<Block> upper = hierarchy.get(new Integer(hierarchy.size()-1));
		
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
	
	private void setRelatives(Integer Layer, int index, Scanner userDecisonGetter)
	{
		Block currentBlock = hierarchy.get(Layer).get(index);
		Double userDecision = 0.0;
		ArrayList<Double> column = new ArrayList<Double>();
		
		for(int i = 1; i < currentBlock.getLowerLayerBlocksNumber(); ++i)
		{
			for(int j = 0; j < i; ++j)
			{				
				System.out.print("Add ratio(" + (j+1)  + "," + (i+1) +"): ");
				try
				{
					userDecision = 
							Double.parseDouble(userDecisonGetter.nextLine());
					if(userDecision < 0)
						throw new NumberFormatException("Negative value");
					column.add(userDecision);
				} catch (NumberFormatException e) {
					System.err.printf("Number must be positive real number.");
					--j;
				} 
				
			}
			System.out.print("Check inconcistency Index for so far ratios:");
			if(hierarchy.get(Layer).get(index).setPairRelatives(column))
				column.clear();
			else
			{
				--i;
				column.clear();
			}
				
		}
		
		System.out.println("All subcriterions relations for " + 
				currentBlock.getBlockName() + " have been set.");
	}
	
	private boolean checkRelativesSet()
	{
		for(int i = 0; i < hierarchy.size(); ++i)
		{
			for(Block b : hierarchy.get(i))
				if(b.getlowerLayerWeights().equals("\r"))
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
	public boolean createHierarchy(Scanner userDecision)
	{
		String decisionLine = "";
		addLayer();
		addBlock(0,"Goal");
		
		while(true)
		{
			System.out.println("l - add layer, L - delete layer, A - add "
					+ "block D - delete block, a - add alternative, "
					+ "d - delete alternative, n - next step with check "
					+ "q - quit, displaying after every decision");
	
			decisionLine = userDecision.nextLine();
			if(decisionLine != null && !decisionLine.equals(""))
			switch (decisionLine.charAt(0))
			{
				case 'q' : return false;
				case 'n' : 
				{
					if(checkLayers() && checkBlocks()) 
						return true; 
					break;
				}
				case 'l' : addLayer(); break;
				case 'L' : if(couldDeleteLayer()) deleteLayer(); break;
				case 'D' : 
				{
					System.out.print("Specify layer number: ");
					int layerNumber = -1;
					try {
						layerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("\nSpecify block number: ");
					int blockIndex = -1;
					try {
						blockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					deleteBlock(layerNumber,blockIndex);
					break;
				}
				case 'A' : 
				{
					System.out.print("Specify layer number: ");
					int layerNumber = -1;
					try {
						layerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("\nSpecify block name: ");
					String blockName = userDecision.nextLine();
					addBlock(layerNumber,blockName);
					break;
				}
				case 'a' :
				{
					System.out.print("\nSpecify alternative name: ");
					String blockName = userDecision.nextLine();
					alternatives.add(new Block(0,blockName));
					break;
				}
				case 'd' :
				{
					System.out.print("\nSpecify alternative index: ");
					int blockIndex = -1;
					try {
						blockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					if(blockIndex >= 0 && blockIndex < alternatives.size())
						alternatives.remove(blockIndex);
				}
			}
			displayHierarchyCreate();
		}
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
	public boolean createConnections(Scanner userDecision)
	{
		String decisionLine = "";
		addGoalConnections();
		addAlternativesConnections();
		//TODO nie ³¹czy do Goal Connection i nie chce wogle robiæ connection...
		while(true)
		{
			System.out.println("a - add connection, d - delete connection, "
					+ "n - next step with check q - quit, "
					+ "displaying after every decision, "
					+ "alternatives are connect automatically");
			
			decisionLine = userDecision.nextLine();
			if(decisionLine != null && !decisionLine.equals(""))
			switch (decisionLine.charAt(0))
			{
				case 'q' : return false;
				case 'n' : 
				{
					if(checkConnections()) 
						return true; 
					System.out.println("Every block must have parent block.");
					break;
				}
				case 'd' : 
				{
					System.out.print("Specify upper layer number: ");
					int upperLayerNumber = -1;
					try {
						upperLayerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify upper block index: ");
					int upperBlockIndex = -1;
					try {
						upperBlockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify below layer number: ");
					int belowLayerNumber = -1;
					try {
						belowLayerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify below block index: ");
					int belowBlockIndex = -1;
					try {
						belowBlockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					deleteConnection(upperLayerNumber,upperBlockIndex,
							belowLayerNumber,belowBlockIndex);
					break;
				}
				case 'a' : 
				{
					System.out.print("Specify upper layer number: ");
					int upperLayerNumber = -1;
					try {
						upperLayerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify upper block index: ");
					int upperBlockIndex = -1;
					try {
						upperBlockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify below layer number: ");
					int belowLayerNumber = -1;
					try {
						belowLayerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify below block index: ");
					int belowBlockIndex = -1;
					try {
						belowBlockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					addConnection(upperLayerNumber,upperBlockIndex,
							belowLayerNumber,belowBlockIndex);
				}
			}
			displayHierarchyConnect();
		}
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
	public boolean createRelatives(Scanner userDecision)
	{
		String decisionLine = "";
		
		while(true)
		{
			System.out.println("s - set relatives for block, "
					+ "n - next step with check q - quit, "
					+ "displaying after every decision");

			decisionLine = userDecision.nextLine();
			if(decisionLine != null && !decisionLine.equals(""))
			switch (decisionLine.charAt(0))
			{
				case 'q' : return false;
				case 'n' : 
				{
					if(checkRelativesSet()) 
						return true; 
					System.out.println("Every block must have specified relatives.");
					break;
				}
				case 's' : 
				{
					System.out.print("Specify layer number: ");
					int layerNumber = -1;
					try {
						layerNumber = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					System.out.print("Specify block index: ");
					int blockIndex = -1;
					try {
						blockIndex = 
							Integer.parseInt(userDecision.nextLine());
					} catch (NumberFormatException e) {
						System.err.println(
								"It have to be digit!");
						continue;
					}
					if(couldSetRelatives(layerNumber,blockIndex))
						setRelatives(layerNumber,blockIndex,userDecision);
					break;
				}
			}
			displayHierarchyWithIndexes();
		}
	}
	
	/**
	 * Sample use of Proceeder with terminal communication with user.
	 * 
	 * @param args in current version do not used
	 */
	public static void main(String[] args)
	{
		Proceeder p = new Proceeder();
		Scanner userDecision = new Scanner(System.in);
		
		if(p.createHierarchy(userDecision) && 
			p.createConnections(userDecision) && p.createRelatives(userDecision))
			p.fileCretor.writeData(p.getHierarchy());
	}
}