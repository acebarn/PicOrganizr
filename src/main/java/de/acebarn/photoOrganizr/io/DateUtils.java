package de.acebarn.photoOrganizr.io;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class DateUtils {

	public static String getMonthAsDecimalValue(int calendarMonthCount) {
		StringBuilder month = new StringBuilder();
		if (calendarMonthCount + 1 < 10) {
			month.append("0");
		}
		month.append(calendarMonthCount + 1);
		return month.toString();

	}

	/**
	 * Returns a Calendar representing the Date the image has been captured.<br>
	 * <ul><li>JPG: Date read from EXIF</li>
	 * <li>Other: Date read from file creation Timestamp</li></ul>
	 * 
	 * @param imageFile
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	public static Calendar getCaptureDate(File imageFile) throws ImageProcessingException, IOException {
		Date date = new Date(imageFile.lastModified());	
		
		if (imageFile.getName().toLowerCase().endsWith("jpg")) {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		}

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal;
	}

}
