import java.io.File;
import java.util.ArrayList;


public class Runner {
	
	public Runner(String directory,String fileout, String[] terms)
	{
		ArrayList<Company> comps = new ArrayList<Company>();
		
		String fout = fileout;
		
		// make company object for each directory
		File parent = new File(directory);
		for (File child : parent.listFiles())
		{
			if (child.isDirectory())
			{
				comps.add(new Company(child.getPath()));
			}
		}
		
		for (Company c : comps)
		{
			c.findAll(terms);
		}
		
		Company.writeParaReport(fout,comps,terms);
	}

}
