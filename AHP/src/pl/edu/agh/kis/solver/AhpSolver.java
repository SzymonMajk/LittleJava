package pl.edu.agh.kis.solver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Starting class, holds list of CriterionLists, handle
 * import of data, creating and adding criterions to appropriate CriterionList.
 * After set up data, starts to calculating weights from the lowest layer to the goal.
 * After all, shows result weight vector and return name of won alternative.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 *
 */
public class AhpSolver 
{
	private File dataFile;
	
	private ArrayList<CriterionList> listOfCriterionList = new ArrayList<>();
	
	private boolean checkFileAvailability()
	{
		if(!dataFile.exists() || 
			dataFile.isDirectory() ||
			!dataFile.canRead())
			return false;
		
		return true;
	}
	
	private Criterion createCriterion(NodeList criterionDetails)
	{
		return new Criterion(
				criterionDetails.item(0).getTextContent(),
				Integer.parseInt(criterionDetails.item(1).getTextContent()),
				criterionDetails.item(2).getTextContent(),
				criterionDetails.item(3).getTextContent());
	}
	
	private Criterion createAlternative(NodeList alternativeDetails)
	{
		return new Criterion(
				alternativeDetails.item(0).getTextContent(),
				Integer.
				parseInt(alternativeDetails.item(1).getTextContent()),
				alternativeDetails.item(2).getTextContent());
	}
	
	private boolean checkExistListByLayer(Integer currentLayer)
	{
		Iterator<CriterionList> listIterator = listOfCriterionList.iterator();
		
		while (listIterator.hasNext()) 
		{
			if(listIterator.next().getLayerNumber().equals(currentLayer))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void addCriterionList(Integer addedListLayer)
	{
		listOfCriterionList.add(new CriterionList(addedListLayer));
		Collections.sort(listOfCriterionList);
	}
	
	private void insertCriterionToList(Criterion toInsert)
	{
		Integer currentLayer = toInsert.getLayer();

		if(!checkExistListByLayer(currentLayer))
		{
			addCriterionList(currentLayer);
		}
		
		Iterator<CriterionList> listIterator = listOfCriterionList.iterator();
		
		while (listIterator.hasNext()) 
		{
			CriterionList nextCriterionList = listIterator.next();
			if(nextCriterionList.getLayerNumber().equals(currentLayer))
			{
				nextCriterionList.addCriterion(toInsert);
			}
		}
	}
	
	private boolean correctParseData()
	{
		if(!checkFileAvailability())
			return false;
		
		try 
		{
            Document doc = DocumentBuilderFactory.newInstance()
            									 .newDocumentBuilder()
            									 .parse(dataFile);
            
            NodeList criterios = doc.getElementsByTagName("criterion");
            NodeList alternatives = doc.getElementsByTagName("alternative");

            if(alternatives.getLength() < 0 || criterios.getLength() < 0)
            	return false;
            
            for(int i = 0; i < alternatives.getLength(); ++i)
            {
            	NodeList alternativesDetails = alternatives.item(i).getChildNodes();
            	//System.out.println(createAlternative(alternativesDetails).toString());
            	insertCriterionToList(createAlternative(alternativesDetails));
            }
            
            for(int i = 0; i < criterios.getLength(); ++i)
            {
            	NodeList criteriosDetails = criterios.item(i).getChildNodes();
            	//System.out.println(createCriterion(criteriosDetails).toString());
            	insertCriterionToList(createCriterion(criteriosDetails));
            }
        } 
		catch (Exception e)
		{
            e.printStackTrace();
            return false;
        }
		
		return true;
	}
	
	private boolean correctAssociateCriterions()
	{		
		for(int i = 1; i < listOfCriterionList.size(); ++i)
		{
			if(!listOfCriterionList.
				get(i).associateAllCriterions(listOfCriterionList.get(i-1)))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Function call private functions which parse data from data file, create all
	 * criterions and alternatives, to end by associate all connections between 
	 * criterions due to informations data.
	 * 
	 * @return true if parsing and connecting were succesfuly in other case 
	 * 		return false.
	 */
	public boolean correctPrepareData()
	{
		return correctParseData() && correctAssociateCriterions();
	}
	
	/**
	 * Function lets all criterions from the lowest criterion lawer to the
	 * highest to calculate result vector. If any error cause it is write
	 * to standard error output.
	 */
	public void proceedAllCalculation()
	{
		for(int i = 1; i < listOfCriterionList.size(); ++i)
        {
        	if(!listOfCriterionList.get(i).proceedLayerCalculations())
        		System.err.println("Wyst¹pi³ b³¹d przy obliczeniach");
        }
	}
	
	/**
	 * Function print information about final result vector from
	 * criterion from the highest layer. If user chose can also print
	 * all information about this criterion.
	 * 
	 * @param showPriorityVector if logic value is true, then function
	 * 		will print all information about highest layer criterio.
	 */
	public void printFinalResult(boolean showPriorityVector)
	{
		if(showPriorityVector)
			listOfCriterionList.
			get(listOfCriterionList.size()-1).printCriterions();
		
		int bestAlternativeIndex = listOfCriterionList.
				get(listOfCriterionList.size()-1).getFinalAlternativeIndex();
		
		if(listOfCriterionList.get(0).hasCriterionByIndex(bestAlternativeIndex))
			System.out.println("Najlepsz¹ alternatyw¹ jest: "
					+ listOfCriterionList.get(0).
					printCriterionNameByIndex(bestAlternativeIndex));
	}
	
	/**
	 * Function print all criterions from the lowest layer to the highest.
	 */
	public void printAllCriterion()
	{
		for(CriterionList listC : listOfCriterionList)
        {
        	listC.printCriterions();
        }
	}
	
	/**
	 * Allows to set data file by user. To correct parse, file must have correct
	 * xml format.
	 * 
	 * @param dataFile File object, represents xml file in reachable directory.
	 */
	public AhpSolver(File dataFile)
	{
		this.dataFile = dataFile;
	}
	
	/**
	 * Set data from default file, which should exist after installation.
	 */
	public AhpSolver()
	{
		this.dataFile = new File("DefaultHierarchy.xml");
	}
	
	/**
	 * Create AhpSolver object and if user set file name in arguments uses it
	 * to parse data else uses default file. Then it tries to correctly parse
	 * and create objects from data, informs about errors and close the app. If
	 * everything passed well, function starts calculating result vectors using
	 * proceedAllCalculations() and after that shows final result with function
	 * printFinalResult().
	 * 
	 * @param args holds path to the xml file from user
	 */
	public static void main(String[] args) 
	{
		AhpSolver solver;
		
		if(args.length > 0)
			solver = new AhpSolver(new File(args[0]));
		else
			solver = new AhpSolver();
		
		if (!solver.correctPrepareData())
		{
			System.out.println("Problem with preparing data file.");
			return;
		}
		
		System.out.println(
				"\nProgram got data from xml file, now it start calculating:\n");
		
		solver.proceedAllCalculation();
		if(args.length >= 2 && args[1].equals("-p"))
			solver.printAllCriterion();
		solver.printFinalResult(true);
	}
}