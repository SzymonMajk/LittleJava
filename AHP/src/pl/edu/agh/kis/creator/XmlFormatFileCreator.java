package pl.edu.agh.kis.creator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Creates and fills in xml file with consistent ahp hierarchy created and
 * set by user. Methods assume, that all elements of hierarchy have good data
 * and do not proceed any format check. Uses java xml parsers to create well
 * formated file. In case of problems with creating or writing to file, during
 * proceeding, informs user about problems and possible reason and tries to
 * delete already created file.
 * 
 * @author Szymon Majkut
 * @version %I%, %G% 
 */
class XmlFormatFileCreator 
{
	private File endFile;
		
	private String alternativeEntry(int alternativesNumber, int index)
	{
		StringBuilder weightBuilder = new StringBuilder();
		
		for(int i = 0; i < alternativesNumber; ++i)
		{
			if(i == index)
				weightBuilder.append(1);
			else
				weightBuilder.append(0);
			if(i != alternativesNumber-1)
			weightBuilder.append(" ");
		}
		
		return weightBuilder.toString();
	}
	
	private boolean prepareFile()
	{
		try {
			return endFile.createNewFile() || (endFile.canWrite() && 
				endFile.canRead() && endFile.isFile());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	private void clearIfPossible()
	{
		endFile.delete();
	}
	
	/**
	 * Should be called after creation of hierarchy and correctly assignment
	 * of all criterion and alternatives names, layers , lower layer names and
	 * pair weigh comparison vectors. In case of problems with communication
	 * with file inform about meet problems and try to delete already created
	 * file.
	 * 
	 * @return logic value inform about success of create and store all data
	 * 		specified by user in previous program steps.
	 */
	public boolean writeData(HashMap<Integer,ArrayList<Block>> hierarchy)
	{
		if(!prepareFile())
		{
			System.out.println("Could not create file");
			return false;
		}
			
		System.out.println("Start writing down in " + endFile.getName());
		
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("hierarchy");
			doc.appendChild(rootElement);

			ArrayList<Element> criterions = new ArrayList<Element>();
			ArrayList<Element> alternatives = new ArrayList<Element>();
			
			for(int i = 1; i < hierarchy.size(); ++i)
			{
				ArrayList<Block> blocks = hierarchy.get(i);
				
				for(int j = 0; j < blocks.size(); ++j)
				{
					Element current = doc.createElement("criterion");
					criterions.add(current);
					rootElement.appendChild(current);
					
					Block currentBlock = blocks.get(j);
					
					Element currentChild = doc.createElement("name");
					currentChild.appendChild(
							doc.createTextNode(currentBlock.getBlockName()));
					current.appendChild(currentChild);
					currentChild = doc.createElement("layer");
					currentChild.appendChild(
							doc.createTextNode(currentBlock.getLayerNumber().toString()));
					current.appendChild(currentChild);
					currentChild = doc.createElement("lowerCriterionNames");
					currentChild.appendChild(
							doc.createTextNode(currentBlock.getLowerLayerBlockNames()));
					current.appendChild(currentChild);
					currentChild = doc.createElement("lowerLayerWeights");
					currentChild.appendChild(
							doc.createTextNode(currentBlock.getlowerLayerWeights()));
					current.appendChild(currentChild);
				}
			}
			
			ArrayList<Block> blocks = hierarchy.get(0);
			
			for(int j = 0; j < blocks.size(); ++j)
			{
				Element current = doc.createElement("alternative");
				alternatives.add(current);
				rootElement.appendChild(current);
				
				Block currentBlock = blocks.get(j);
				
				Element currentChild = doc.createElement("name");
				currentChild.appendChild(
						doc.createTextNode(currentBlock.getBlockName()));
				current.appendChild(currentChild);
				currentChild = doc.createElement("layer");
				currentChild.appendChild(
						doc.createTextNode(currentBlock.getLayerNumber().toString()));
				current.appendChild(currentChild);
				currentChild = doc.createElement("alternativePriorityVector");
				currentChild.appendChild(
						doc.createTextNode(alternativeEntry(blocks.size(),j)));
				current.appendChild(currentChild);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(endFile);

			transformer.transform(source, result);

		  } catch (ParserConfigurationException pce) {
			System.err.println("Error during writing to file: "
					+pce.getMessage());
			clearIfPossible();
			return false;
		  } catch (TransformerException tfe) {
			  System.err.println("Error during writing to file: "
						+tfe.getMessage());
				clearIfPossible();
				return false;
		  }
		
		System.out.println("File saved.");
		return true;
	}
	
	/**
	 * Create new File object, using file name from method argument and store 
	 * as private component. Do not try to create file itself.
	 * 
	 * @param fileName name of xml file, that is going to be used while 
	 * 		creating and writing to result file.
	 */
	public XmlFormatFileCreator(String fileName)
	{
		endFile = new File(fileName);
	}
}