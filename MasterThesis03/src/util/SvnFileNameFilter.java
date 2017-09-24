package util;

import java.io.File;
import java.io.FilenameFilter;

public class SvnFileNameFilter implements FilenameFilter{

	@Override
	public boolean accept(File dir, String name) {
		return !(name.endsWith(".svn"));
		
	}

}
