package com.fan107.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.common.helper.TimeHelp;

public class OrderState {
	
	/**
	 * 判断下单时间是否在时间段内
	 * @return
	 */
	public static boolean checkTime(String time) {
		String[] orderTimeStr = time.split("\\|");
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String nowTimeStr=format.format(new Date()).split(" ")[1];
		int nowMinute = TimeHelp.formatToMinute(nowTimeStr);
		
		for(int i=0; i<orderTimeStr.length; i++) {
			String[] startTimeStr = orderTimeStr[i].split("-");
			int startMinute = TimeHelp.formatToMinute(startTimeStr[0]);
			int endMinute = TimeHelp.formatToMinute(startTimeStr[1]);
			
			if(nowMinute >= startMinute && nowMinute <= endMinute) {
				return true;
			}
		}
		
//		return false;
		return true;
	}
	
	public static boolean checkLimitPrice(float totalPrice) {
		boolean isLimit = true;
		
//		if()
		
		return isLimit;
	}
}
