package org.capoz.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class TOPTcompute {

	public void compute() {
		
		int epochTime = getParsedEpochTime();
		String encodedEpochTime = Integer.toString(epochTime);
		
	}
	
	public static int getParsedEpochTime() {
		
		long lresTime = 0;
		int iresTime = 0;
		
		
		Date today = Calendar.getInstance().getTime();
		 
		// Constructs a SimpleDateFormat using the given pattern
		SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
 
		// format() formats a Date into a date/time string.
		String currentTime = crunchifyFormat.format(today);
 
		try {
 
			// parse() parses text from the beginning of the given string to produce a date.
			Date date = crunchifyFormat.parse(currentTime);
 
			// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
			lresTime = date.getTime();
			
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		iresTime = (int) lresTime / 30;
		
		return iresTime;
		
	}
	
	
	
	
	
}
