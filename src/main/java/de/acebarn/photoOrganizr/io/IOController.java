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
	private File sourcePath;
	private String supportedFileTypes;
	private File targetPath;
	private String prefix;

	public IOController(File sourcePath, String supportedFileTypes, File targetPath, String prefix) {
		this.sourcePath = sourcePath;
		this.supportedFileTypes = supportedFileTypes;
		this.targetPath = targetPath;
		this.prefix = prefix;
		fileTypes = supportedFileTypes.split(" ");
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
		int day = cal.get(Calendar.DAY_OF_MONTH);

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

		sanitizePrefix();
		
		String newFileName = prefix + year + month + day + "_" + picFile.getName();
		File newFile = new File(dayFolder.getAbsolutePath() + File.separator + newFileName);

		logger.info("Kopiere Bild {} nach {}", picFile.getName(), newFile.getAbsolutePath());
		FileUtils.copyFile(picFile, newFile, true);

	}

	private void sanitizePrefix() {
		if (!prefix.equals(""))
		{
			prefix = prefix + "_";
		}
		
	}

}
