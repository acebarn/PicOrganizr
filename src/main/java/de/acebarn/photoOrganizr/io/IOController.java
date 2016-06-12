package de.acebarn.photoOrganizr.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class IOController {

	private static final String path = "/home/alessio/Pictures/2015/01";
	private static final String supportedFileTypes = "jpg JPG nef NEF";
	
	private String[] fileTypes;
	
	public IOController()
	{
		fileTypes = supportedFileTypes.split(" ");
	}
	
	public List<File> readDirectory() throws FileNotFoundException
	{
		File inputDirectory = new File(path);
		
		if (!inputDirectory.exists())
		{
			throw new FileNotFoundException("Das angegebene Verzeichnis konnte nicht gefunden werden");
		}
		
		List<File> fileList = (LinkedList<File>) FileUtils.listFiles(inputDirectory, fileTypes, true);
		
		return fileList;
	}
	
}
