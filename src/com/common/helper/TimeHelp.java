package com.common.helper;

public class TimeHelp {
	
	/**
	 * ½« 09:00 ×ª»»³É 9*60 + 0*60 
	 * @param time
	 * @return
	 */
	public static int formatToMinute(String time) {
		int minute = 0;
		
		String[] timeStr = time.split(":");
		minute += Integer.valueOf(timeStr[0])*60;
		minute += Integer.valueOf(timeStr[1]);
		
		return minute;
	}
	
}
