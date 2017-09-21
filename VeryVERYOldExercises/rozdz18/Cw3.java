package pl.edu.agh.kis.rozdz18;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Cw3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		File path = new File("zabawyzplikami/");
		String[] fileList;
		
		if(args.length == 0)
		{
			fileList = path.list();
		}
		else
		{
			fileList = path.list(new DirFilterCw3(args[0]));
			
			Arrays.sort(fileList,String.CASE_INSENSITIVE_ORDER);
		}
			long size = 0;
			for(String file : fileList)
			{
				size += new File("zabawyzplikami/" + file).length();
			}
		System.out.println("Sumaryczny rozmiar plików = " + size);
		

	}

}

class DirFilterCw3 implements FilenameFilter {
	
	private Pattern pattern;
	public DirFilterCw3(String regex) {
		pattern = Pattern.compile(regex);
	}
	public boolean accept(File dir, String name)
	{
		return pattern.matcher(name).matches();
	}
}