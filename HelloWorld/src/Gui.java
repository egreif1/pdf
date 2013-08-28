

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

public class Gui implements ActionListener {

	/*
	 * Variables
	 */
	
	JTextField directory;
	JTextField keys;
	JRadioButton comp;
	JFrame frame;
	JTextField fout;
	
	/**
	 * This is the Swing gui for the program
	 */
	
	public Gui()
	{
		this.createAndShowGUI();
	}
	
	public void addComponentsToPane(Container pane)
	{
		
		pane.setLayout(null);
		
		Insets insets = pane.getInsets();
		
		JLabel title = new JLabel("Select Parent Directory:");
		pane.add(title);
		
		Dimension textSize = title.getPreferredSize();
		title.setBounds(5+insets.left, 5+insets.top,textSize.width,textSize.height);
		
		directory = new JTextField(50);
		directory.setEditable(false);
		pane.add(directory);
		Dimension fieldSize = directory.getPreferredSize();
		directory.setBounds(5+insets.left,25+insets.top,200,fieldSize.height);
		
		JButton browse = new JButton("In Browse");
		pane.add(browse);
		Dimension buttonSize = browse.getPreferredSize();
		browse.setBounds(210+insets.left,25+insets.top,175, fieldSize.height);
		browse.addActionListener(this);
		
		JLabel outfile = new JLabel("Select file to write out to");
		pane.add(outfile);
		outfile.setBounds(5+insets.left,50+insets.top,200,fieldSize.height);
		
		fout = new JTextField(50);
		fout.setEditable(false);
		pane.add(fout);
		fout.setBounds(5+insets.left,75+insets.top,200,fieldSize.height);
		
		JButton findFile = new JButton("Out Browse");
		pane.add(findFile);
		findFile.setBounds(210+insets.left,75+insets.top,175,fieldSize.height);
		findFile.addActionListener(this);
		
		comp = new JRadioButton("By Company?");
		pane.add(comp);
		Dimension radSize = comp.getPreferredSize();
		comp.setBounds(5+insets.left,125+insets.top,radSize.width,radSize.height);
		
		JLabel key = new JLabel("Comma seperated list of keywords:");
		pane.add(key);
		key.setBounds(5+insets.left,150+insets.top,350,textSize.height);
		
		keys = new JTextField(50);
		pane.add(keys);
		keys.setBounds(5+insets.left,175+insets.top,350,fieldSize.height);
		
		JButton done = new JButton("Done");
		pane.add(done);
		done.setBounds(150+insets.left,200+insets.top,buttonSize.width,buttonSize.height);
		done.addActionListener(this);
	}
	
	public void createAndShowGUI()
	{
		frame = new JFrame("Trascript Reader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addComponentsToPane(frame.getContentPane());
		
		frame.pack();
		frame.setSize(400,300);
		frame.setVisible(true);
		
		
	}	
	
	public void actionPerformed(ActionEvent e)
	{
		String check = ((JButton) e.getSource()).getText();

		if (check == "In Browse")
		{
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
	        int returnVal = fc.showOpenDialog(frame);

	        if (returnVal == JFileChooser.APPROVE_OPTION)
	        {
	        	File file = fc.getSelectedFile();
	        
	        	directory.setText(file.toString());
	        }
		}
		else if (check == "Out Browse")
		{
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
	        int returnVal = fc.showOpenDialog(frame);

	        if (returnVal == JFileChooser.APPROVE_OPTION)
	        {
	        	File file = fc.getSelectedFile();
	        
	        	fout.setText(file.toString());
	        }
		}
		else
		{
			String [] terms;
			if (keys.getText().indexOf(",") != -1)
			{
				terms = keys.getText().split(",");
			}
			else if (keys.getText() != "")
			{
				terms = new String[1];
				terms[0] = keys.getText();
			}
			else
			{
				return;
			}

			boolean company;
			if (comp.isSelected())
			{
				company = true;
			}
			else
				company = false;
			
			// run the pdf ripping program
			if (directory.getText() != "" && fout.getText() != "" && terms.length > 1)
			{
				Runner run = new Runner(directory.getText(),fout.getText()+"/out.txt",terms);
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		
		}
	}


}
