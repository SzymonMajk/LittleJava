package pl.edu.agh.kis.creator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
		
	private boolean prepareFile()
	{
		try {
			return endFile.createNewFile() && endFile.canWrite() && 
				endFile.canRead() && endFile.isFile();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	private void writeBlock()
	{
		
	}
	
	private void writeLayer()
	{
		
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