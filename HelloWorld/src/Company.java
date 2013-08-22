
// Evan Greif, egreif1@gmail.com
// 8/22/2013
// Class for a company's PDF folder

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	
	public String getNameFromPath(String file)
	{
		String[] parts = file.split("/");
		int end = parts.length;
		return parts[end-1];
	}
	
	//find all lines in files containing one or more of the terms
	public void findLines(ArrayList<String> terms, String f)
	{
		// declare the file we are on
		this.lines.add("Matches for file: " + this.getNameFromPath(f) + "\n\n");
		
		FileReader file = null;
		try
		{
			file = new FileReader(f);
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		BufferedReader br = new BufferedReader(file);
		String line = null;
		
		try
		{
			line = br.readLine();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		while(line != null)
		{
			try {
				line = br.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			// now search the line
			for (String t : terms)
			{
				String rx = t;
				if (line != null && line.indexOf(rx)!=-1)
				{
					this.lines.add(line);
					break;
				}
			}
		}
	}
	
	public void findParas(ArrayList<String> terms, String f)
	{
		// declare what file we are on
		this.matches.add("Matches for file: " + this.getNameFromPath(f) + "\n\n");
		
		// now buffer though paragraphs and return the matching ones
		FileReader file = null;
		try{
			file = new FileReader(f);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		BufferedReader br = new BufferedReader(file);
		
		
		
		
	}
	 
	public static void main(String[] args)
	{
		Company comp = new Company("/home/egreif1/Documents/Coding/PDF/test/");
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("China");
		comp.findLines(terms);
		
		for (String l : comp.lines)
		{
			System.out.println(l);
		}
	}

}
