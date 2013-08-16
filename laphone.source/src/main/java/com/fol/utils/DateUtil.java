package com.fol.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

	
	static String[] DATE_PATTEN = new String[]{"yyyy/MM/dd HH:mm:ss","yyyy/MM/dd"};   
	
	/**
	 * 将当前日期转为long字符串
	 * @return
	 */
	public long getNowLong() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获取指定日期与当前日期之间的差值.
	 * startDate+returnValue=endDate
	 * @param date
	 * @return
	 */
	public int getDifferenceDaysw(Date startDate, Date endDate) {
		
		int result = 0;
		
		if (startDate.before(endDate)) {
			while (endDate.before(startDate)) {
				startDate = DateUtils.addDays(startDate, 1);
				result ++;
			}
		} 

		if (endDate.before(startDate)){
			
			while(startDate.before(endDate)) {
				endDate = DateUtils.addDays(endDate, 1);
				result ++;
			}
			result = result * -1;
		}
		return result;
	}
	
	
	private String[] getDateDurationString(String fromDate, String endDate) {
		return new String[]{fromDate, endDate};
	}
	
	public String[] getTodayDuration() {
		Date date  = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		String fromDate = format.format(date);
		String endDate = format.format(date);
		return getDateDurationString(fromDate, endDate);
	}
	
	public String[] getYesterdayDuration() {
		Date date  = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		String endDate = format.format(date);
		date = DateUtils.addDays(date, -1);
		String fromDate = format.format(date);
		return getDateDurationString(fromDate, endDate);
	}
	
	public String[] getThisWeekDuration() {
		Date date  = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		String endDate = format.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		date = DateUtils.addDays(date, dayOfWeek * -1 +1);
		String fromDate = format.format(date);
		return getDateDurationString(fromDate, endDate);
	}
	
	public String[] getLastWeekDuration() {
		
		Date date  = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		date = DateUtils.addDays(date, dayOfWeek * -1);
		String endDate = format.format(date);
		
		date = DateUtils.addDays(date, -6);
		String fromDate = format.format(date);
		
		return getDateDurationString(fromDate, endDate);
	}
	
	public String[] getThisMonthDuration() {
		Date date  = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		String endDate = format.format(date);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
		date = cal.getTime();
		String fromDate = format.format(date);
		return getDateDurationString(fromDate, endDate);
	}
	
	public  String[] getLastMonthDuration() {
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);
		String fromDate = format.format(cal.getTime());
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String endDate = format.format(cal.getTime());
		return getDateDurationString(fromDate, endDate);
	}
	
	public String[] getDaysOffsetTodayDuration(int previousDays){
		Date date  = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTEN[1]);
		String endDate = format.format(date);
		date = DateUtils.addDays(date, previousDays);
		String fromDate = format.format(date);
		return getDateDurationString(fromDate, endDate);
	}
	
}
