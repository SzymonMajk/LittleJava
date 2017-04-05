package pl.edu.agh.kis.creator;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public XmlFormatFileCreator fileCretor = 
			new XmlFormatFileCreator("DefaultHierarchy.xml");
	
	private void displayHierarchy()
	{
		System.out.println("Current Hierarchy:");
	}
	
	private void addLayer()
	{
		
	}
	
	private boolean couldDeleteLayer()
	{
		return false;
	}
	
	private void deleteLayer()
	{
		
	}
	
	private boolean checkLayers()
	{
		return false;
	}
	
	private void addBlock(Integer layer)
	{
		
	}
	
	private void deleteBlock(Integer layer, Integer index)
	{
		
	}
	
	private void addConnection(Integer aboveLayer, Integer aboveIndex,
			Integer belowLayer, Integer belowIndex)
	{
		
	}
	
	private void addGoalConnections()
	{
		
	}
	
	private void addAlternativesConnections()
	{
		
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
		
		p.createHierarchy();
		p.createConnections();
		p.createRelatives();
		
		new Block(0,"Jas").setPairRelatives();
		
		p.fileCretor.writeData();
	}
}