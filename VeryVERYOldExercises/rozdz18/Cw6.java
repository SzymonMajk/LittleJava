package pl.edu.agh.kis.rozdz18;

//ma wypisać wszystkie pliki po określonej dacie

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Cw6 {
	static long size = 0;
	public static void main(String[] args)
	{	
		final SimpleDateFormat date = new SimpleDateFormat("26/05/2015");
		
		new ProcessFilesCw6(new ProcessFilesCw6.Strategy() {
			public void process(File file) {
				Date modDate = new Date(file.lastModified());
				try {
					if(modDate.after(date.parse("26/05/2015")))
						System.out.println(file);
				} catch(ParseException e) {
					System.out.println(e.getMessage());
				}
			}
		}, "java").start(args);
		
	}
}

class DirectoryCw6 {

	public static File[] local(File dir, final String regex)
	{
		return dir.listFiles(new FilenameFilter() {
			private Pattern pattern = Pattern.compile(regex);
			public boolean accept(File dir, String name)
			{
				return pattern.matcher(new File(name).getName()).matches();
			}
		});
	}
	
	public static File[] local(String path, final String regex)
	{
		return local(new File(path),regex);
	}
	
	public static class TreeInfo implements Iterable<File> {
		public List<File> files = new ArrayList<File>();
		public List<File> dirs = new ArrayList<File>();
		
		public Iterator<File> iterator()
		{
			return files.iterator();
		}
		
		void addAll(TreeInfo other)
		{
			files.addAll(other.files);
			dirs.addAll(other.dirs);
		}
		public String toString()
		{
			return "katalogi: " + PPrintCw6.pformat(dirs) +
					"\n\npliki: " + PPrintCw6.pformat(files);
		}
	}
	
	public static TreeInfo walk(String start, String regex)
	{
		return recurseDirs(new File(start),regex);
	}
	
	public static TreeInfo walk(File start, String regex)
	{
		return recurseDirs(start,regex);
	}
	
	public static TreeInfo walk(String start)
	{
		return recurseDirs(new File(start),".*");
	}
	
	static TreeInfo recurseDirs(File startDir, String regex)
	{
		TreeInfo result = new TreeInfo();
		for(File item : startDir.listFiles())
		{
			if(item.isDirectory())
			{
				result.dirs.add(item);
				result.addAll(recurseDirs(item,regex));
			}
			else
			{
				if(item.getName().matches(regex))
				{
					result.files.add(item);
				}
			}
		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] skad = {"/home"};
		for(String arg : skad)
		{
			System.out.println(walk(arg));
		}
	}

}

class PPrintCw6 {
	public static String pformat(Collection<?> c)
	{
		if(c.size()==0 ) return "[]";
		StringBuilder result = new StringBuilder("[");
		for(Object elem : c)
		{
			if(c.size() != 1) result.append("\n ");
			result.append(elem);
		}
		if(c.size() != 1)
			result.append("\n");
		result.append("]");
		return result.toString();
	}
	public static void pprint(Collection<?> c){
		System.out.println(pformat(c));
	}
	public static void pprint(Object[] c){
		System.out.println(Arrays.asList(c));
	}
}

class ProcessFilesCw6 {
	public interface Strategy {
		void process(File file);
	}
	
	private Strategy strategy;
	
	private String ext;
	
	public ProcessFilesCw6(Strategy strategy, String ext)
	{
		this.strategy = strategy;
		this.ext = ext;
	}
	
	public void start(String[] args)
	{
		try{
			
			if(args.length == 0)
			{
				processDirectoryTree(new File("."));
			}
			else
			{
				for(String arg : args)
				{
					File fileArg = new File(arg);
					if(fileArg.isDirectory())
					{
						processDirectoryTree(fileArg);
					}
					else
					{
						if(arg.endsWith(".") + ext != null)
						{
							arg += "." + ext;
						}
						strategy.process(new File(arg).getCanonicalFile());
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void processDirectoryTree(File root) throws IOException
	{
		for(File file : DirectoryCw6.walk(root.getAbsoluteFile(), ".*\\." + ext) )
		{
			strategy.process(file.getCanonicalFile());
		}
	}
	
}
