package pl.edu.agh.kis.rozdz18;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Cw2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		File path = new File("zabawyzplikami/");

		SortedDirList newDirList = new SortedDirList(path);
		
				
		for(String file : newDirList.list())
		{
			System.out.println(file);
		}
		
		for(String file : newDirList.list("6"))
		{
			System.out.println(file);
		}
		

	}

}

class DirFilterCw2 implements FilenameFilter {
	
	private Pattern pattern;
	public DirFilterCw2(String regex) {
		pattern = Pattern.compile(regex);
	}
	public boolean accept(File dir, String name)
	{
		return pattern.matcher(name).matches();
	}
}

class SortedDirList {
	
	private File listDir;
	
	SortedDirList(File f)
	{
		if(f.isDirectory())
		{
			listDir = f;
		}
		else
		{
			System.out.println("Nie podałeś katalogu!");
		}
	}
	public String[] list()
	{
		return listDir.list();
	}
	public String[] list(String regex)
	{
		String[] result;
		result = listDir.list(new DirFilterCw2(regex));


		Arrays.sort(result,String.CASE_INSENSITIVE_ORDER);

		return result;

	}
}