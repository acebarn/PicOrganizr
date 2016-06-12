package de.acebarn.photoOrganizr.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class IOController {

	private static final Logger logger = LoggerFactory.getLogger(IOController.class);

	private String[] fileTypes;
	private String sourcePath;
	private String supportedFileTypes;
	private String targetPath;
	private String prefix;

	public IOController(String sourcePath, String supportedFileTypes, String targetPath, String prefix) {
		this.sourcePath = sourcePath;
		this.supportedFileTypes = supportedFileTypes;
		this.targetPath = targetPath;
		this.prefix = prefix;
		fileTypes = supportedFileTypes.split(" ");
	}

	public List<File> readDirectory() throws FileNotFoundException {
		File inputDirectory = new File(sourcePath);

		if (!inputDirectory.exists()) {
			throw new FileNotFoundException();
		}

		List<File> fileList = (LinkedList<File>) FileUtils.listFiles(inputDirectory, fileTypes, true);

		return fileList;
	}

	public void organizePictureByDay(File picFile) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(picFile);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		String month = DateUtils.getMonthAsDecimalValue(cal.get(Calendar.MONTH));
		int day = cal.get(Calendar.DAY_OF_MONTH);

		File targetDirectory = new File(targetPath);
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
		
		File dayFolder = new File(monthFolder.getAbsolutePath()+File.separator+day);
		if (!dayFolder.exists())
		{
			dayFolder.mkdirs();
		}
		
		String newFileName = prefix + "_" + year+month+day+picFile.getName();
		File newFile = new File(dayFolder.getAbsolutePath()+File.separator+newFileName);
		
		logger.info("Kopiere Bild {} nach {}",picFile.getName(),newFile.getAbsolutePath());
		FileUtils.copyFileToDirectory(picFile, dayFolder, true);

	}

}
