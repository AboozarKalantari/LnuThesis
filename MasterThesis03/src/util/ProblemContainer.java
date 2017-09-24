package util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileNameExtensionFilter;

public class ProblemContainer {
	File directory;
	String[] testFiles;
	public ProblemContainer(String path) {
		directory = new File(path);
		if(directory.exists()&& directory.isDirectory())
		{
			// get all folders of test files
			FilenameFilter filter = new SvnFileNameFilter();
			File [] list = directory.listFiles(filter);
			testFiles = new String[list.length];
			for (int i=0; i<list.length;i++)
			{
				testFiles[i] = list[i].getName();
			}
			
		}
	}
	
	
	public String[] getProblemPaths()
	{
		return testFiles;
	}
	
}
