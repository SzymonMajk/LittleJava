package pl.edu.agh.kis.rozdz18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

public class Cw1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		File path = new File("zabawyzplikami/");
		String[] fileList;

		fileList = path.list(new DirFilterCw1(args));
			
		Arrays.sort(fileList,String.CASE_INSENSITIVE_ORDER);
		for(String file : fileList)
		{
			System.out.println(file);
		}	
	}
}

class DirFilterCw1 implements FilenameFilter {
	
	String[] words;
	public DirFilterCw1(String[] words) {
		this.words = words;
		
	}
	public boolean accept(File dir, String name)
	{
		String fileContent = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(dir.getName() + "/" + name));
			String line;
			while((line = in.readLine()) != null)
			{
				fileContent += line+"\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < words.length; ++i)
		{
			if(fileContent.contains(words[i]))
			{
				return true;
			}
		}
		
		return false;
	}
}