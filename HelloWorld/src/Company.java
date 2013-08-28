
// Evan Greif, egreif1@gmail.com
// 8/22/2013
// Class for a company's PDF folder

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
				
				
				// add to list of files if it's not already there
				if (files.indexOf(child.getPath().replace(".pdf",".txt")) == -1)
				{
					this.files.add(child.getPath().replace(".pdf", ".txt"));
				}
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
	public void findLines(String[] terms, String f)
	{
		// declare the file we are on
		this.lines.add("Matches for file: " + this.getNameFromPath(f) + "\n\n");
		
		FileReader file = null;
		try
		{
			file = new FileReader(f);
		
			BufferedReader br = new BufferedReader(file);
			String line = null;
			
			line = br.readLine();
			
			while(line != null)
			{	
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

				line = br.readLine();
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	public void findParas(String[] terms, String f)
	{
		// declare what file we are on
		this.matches.add("Matches for file: " + this.getNameFromPath(f) + "\n\n");
		
		// now buffer though paragraphs and return the matching ones
		FileReader file = null;
		try
		{
			
			file = new FileReader(f);
			
			BufferedReader br = new BufferedReader(file);
			String line = null;
			boolean on_block = false;
			ArrayList<String> current = new ArrayList<String>();
			
			line = br.readLine();
			
			while(line != null)
			{

				if (on_block)
				{
					this.matches.add(this.replaceTerms(terms,line));
					if (this.endsPara(line))
					{
						on_block = false;
						current.clear();
						this.matches.add("\n\n");
						line = br.readLine();
						continue;
					}
					
				}
				
				current.add(line);

				String test = line.toLowerCase();

				for (String t : terms)
				{
					
					if (test != null && test.contains(t.toLowerCase()))
					{
						for (String p : current)
						{
							this.matches.add(this.replaceTerms(terms,p));
						}
						on_block = true;
						current.clear();
						break;
					}
			}
			
			if (this.endsPara(line))
			{
				current.clear();
			}

			line = br.readLine();
			
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
	}
	
	public boolean endsPara(String line)
	{
		if (line == "\n")
			return true;
		else if (line.length()<2)
			return true;
		else if (line.length()<60 && line.charAt(line.length()-2)=='.')
			return true;
		else if (line.length()<40)
			return true;
		
		return false;
	}
	
	public static void writeParaReport(String file, ArrayList<Company> comps,String[] terms)
	{
		try{
			PrintWriter bw = new PrintWriter(file,"UTF-8");
			

			// Write out terms header
			bw.println("PARAGRAPHS MATCHING ");
			for (String t : terms)
			{
				bw.print(t+" ");
			}
			
			bw.print("\n\n");
			
			for (Company c : comps)
			{
				bw.print(c.name);
				bw.print("\n\n");
				ArrayList<String> matches = c.matches;
				for (String m : matches)
				{
					bw.print(m);
				}
			}

			bw.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
	}
	
	public void findAll(String[] terms)
	{
		for (String f : this.files)
		{
			this.findParas(terms,f);
		}
	}

	public String replaceTerms(String[] terms,String line)
	{
		for (String t : terms)
		{
			line = line.replace(t,"***"+t+"***");
		}

		return line;
	}

}
