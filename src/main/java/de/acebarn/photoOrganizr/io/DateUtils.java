package de.acebarn.photoOrganizr.io;

public class DateUtils {

	public static String getMonthAsDecimalValue(int calendarMonthCount) {
		StringBuilder month = new StringBuilder();
		if (calendarMonthCount + 1 < 10) {
			month.append("0");
		}
		month.append(calendarMonthCount + 1);
		return month.toString();

	}

}
