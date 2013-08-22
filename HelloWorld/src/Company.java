
// Evan Greif, egreif1@gmail.com
// 8/22/2013
// Class for a company's PDF folder

import java.util.ArrayList;
import java.io.File;

public class Company {
	
	// text files that correspond to the company
	public ArrayList<String> files;
	
	// matching paragraphs for the specific terms
	public ArrayList<String> matches;
	
	// matching lines for the specific terms
	public ArrayList<String> lines;
	
	// name of the company
	public String name;
	
	// directory for the company
	public String directory;
	
	public Company(String directory)
	{
		// initialize directory
		this.directory = directory;
		
		// create empty array lists for other parameters
		files = new ArrayList<String>();
		matches = new ArrayList<String>();
		lines = new ArrayList<String>();
		
		// get the name of the company
		String[] parts = directory.split("/");
		int end = parts.length;
		name = parts[end-1];
		System.out.println(name);
	
		this.recConvert(directory);
		
		
	}
	
	// recursively iterates through the directory and gets all the files
	public void recConvert(String directory)
	{	
		File folder = new File(directory);
		for (File child : folder.listFiles())
		{
			// skip hidden files 
			if (child.getName().charAt(0) == '.')
				continue;
			
			if (child.isDirectory())
			{
				recConvert(child.getPath());
			}
			else
			{
				// convert from pdf to text here!
				
				// add to list of files
				this.files.add(child.getPath());
			}
		}
	}
	
	public static void main(String[] args)
	{
		Company comp = new Company("/home/egreif1/Documents/Coding/PDF/test/");
	}

}
