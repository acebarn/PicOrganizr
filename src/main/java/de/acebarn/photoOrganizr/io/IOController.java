package de.acebarn.photoOrganizr.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;

public class IOController {

	private static final Logger logger = LoggerFactory.getLogger(IOController.class);

	private String[] fileTypes;
	private File sourcePath;
	private String supportedFileTypes;
	private File targetPath;
	private String prefix;

	public IOController(File sourcePath, String supportedFileTypes, File targetPath, String prefix) {
		this.sourcePath = sourcePath;
		this.supportedFileTypes = supportedFileTypes;
		this.targetPath = targetPath;
		this.prefix = prefix;
		sanitizePrefix();
		fileTypes = supportedFileTypes.split(" ");
	}
	
	public IOController()
	{
		
	}

	public List<File> readDirectory() throws FileNotFoundException {
		File inputDirectory = sourcePath;

		if (!inputDirectory.exists()) {
			throw new FileNotFoundException();
		}

		List<File> fileList = (LinkedList<File>) FileUtils.listFiles(inputDirectory, fileTypes, true);

		return fileList;
	}

	public void organizePictureByDay(File picFile) throws ImageProcessingException, IOException {
		Calendar cal = DateUtils.getCaptureDate(picFile);

		int year = cal.get(Calendar.YEAR);
		String month = DateUtils.getMonthAsDecimalValue(cal.get(Calendar.MONTH));
		String day = DateUtils.getDayAsDecimalValue(cal.get(Calendar.DAY_OF_MONTH));

		File targetDirectory = targetPath;
		if (!targetDirectory.exists()) {
			throw new FileNotFoundException();
		}

		File yearFolder = new File(targetDirectory.getAbsolutePath() + File.separator + year);
		if (!yearFolder.exists()) {
			yearFolder.mkdirs();
		}

		File monthFolder = new File(yearFolder.getAbsolutePath() + File.separator + month);
		if (!monthFolder.exists()) {
			monthFolder.mkdirs();
		}

		File dayFolder = new File(monthFolder.getAbsolutePath() + File.separator + day);
		if (!dayFolder.exists()) {
			dayFolder.mkdirs();
		}

		
		String newFileName = prefix + year + month + day + "_" + picFile.getName();
		File newFile = new File(dayFolder.getAbsolutePath() + File.separator + newFileName);

		logger.info("Kopiere Bild {} nach {}", picFile.getName(), newFile.getAbsolutePath());
		FileUtils.copyFile(picFile, newFile, true);

	}

	public void sanitizePrefix() {
		if (!prefix.equals(""))
		{
			prefix = prefix + "_";
		}
		
	}
	
	public File getSourcePath() {
		return sourcePath;
	}
	
	public void setSourcePath(File sourcePath) {
		this.sourcePath = sourcePath;
	}
	
	public File getTargetPath() {
		return targetPath;
	}
	
	public void setTargetPath(File targetPath) {
		this.targetPath = targetPath;
	}
	
	public void readPreviousConfiguration()
	{
		Map<String, Object> storeMap;
		try {
			FileInputStream fio = new FileInputStream("previous.conf");
			ObjectInputStream ois = new ObjectInputStream(fio);
			storeMap = (Map<String, Object>) ois.readObject();
			
			sourcePath = (File) storeMap.get("source");
			targetPath = (File) storeMap.get("target");
			prefix = (String) storeMap.get("prefix");
			
		} catch (FileNotFoundException e) {
			logger.info("No previous configuration has been read");
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
		
		
	}
	
	public void writePreviousSourcePath()
	{
		Map<String, Object> storeMap = new HashMap<String, Object>();
		if (sourcePath != null && targetPath != null)
		{
			storeMap.put("source", sourcePath);
			storeMap.put("target", targetPath);
			storeMap.put("prefix", prefix);
			logger.info("Writing to config...\n Source: {}\nTarget: {}\nPrefix: {}",sourcePath.getAbsolutePath(),targetPath.getAbsolutePath(),prefix);
			try {
				FileOutputStream fout = new FileOutputStream(new File("previous.conf"));
				ObjectOutputStream oout = new ObjectOutputStream(fout);
				oout.writeObject(storeMap);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void setSupportedFileTypes(String supportedFileTypes) {
		this.supportedFileTypes = supportedFileTypes;
	}
	
	public String getSupportedFileTypes() {
		return supportedFileTypes;
	}

	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
}
