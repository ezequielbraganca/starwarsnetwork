package br.com.letscode.starwarsnetwork.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 	
	
	public static String dateToString(Date date) {
		return format.format(date);
	}
}
