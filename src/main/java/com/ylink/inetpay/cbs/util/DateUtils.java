package com.ylink.inetpay.cbs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类.
 * 
 * @author 潘瑞峥
 * @date 2012-10-31
 */
public class DateUtils {
	
	/**
	 * 获取当前时间的字符串，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentPrettyDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	/**
	 * 将Date转换为datetime型
	 */
	public static String dateToDateTime(Date date){
		
		if(date ==null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 将Date转换为yyyyMMdd型
	 */
	public static String dateToyyMMdd(Date date){
		
		if(date ==null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	/**
	 * 获取下一天凌晨时间
	 */
	public static Date findNestDateBegin(Date date){
		if(date == null)
			return null;
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_YEAR,1);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);  
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0);  
	    
	    return calendar.getTime();
	}
	
	/**
	 * 获取今日凌晨时间
	 */
	public static Date findTodayDateBegin(Date date){
		if(date == null)
			return null;
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		
		return calendar.getTime();
	}
	
	/**
	 * 获取时间 yyyyMMdd格式
	 */
	public static String dateTOYYYYMMDD(Date date){
		if(date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		return sdf.format(date);
	}
	
	/**
	 * 获取时间 根据自己指定的类型获取时间显示类型
	 */
	public static String getDateFormat(String dateFormat,Date date){
		if(dateFormat == null || date ==null)
			return null;
		return new SimpleDateFormat(dateFormat).format(date);
	}
	
	/**
	 * 获取指定时间 返回String
	 */
	public static String getAppointDateFormat(String dateFormat,Date date,Integer i){
		if(dateFormat==null||date==null)
			return null;
		if(i == null )
			i=0;
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_YEAR,i);
		return new SimpleDateFormat(dateFormat).format(calendar.getTime());
	}
	
	/**判断是否超过24小时
	   *   
	   * @param date1
	   * @param date2
	   * @return boolean
	   * @throws Exception
	   */
	    public static boolean judgmentDate(String date1, String date2)  { 
	        try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Date start = sdf.parse(date1); 
				Date end = sdf.parse(date2); 
				long cha = end.getTime() - start.getTime(); 
				if(cha<0){
				  return false; 
				}
				double result = cha * 1.0 / (1000 * 60 * 60);
				if(result<=24){ 
				     return true; 
				}else{ 
				     return false; 
				}
			} catch (ParseException e) {
				return false; 
			} 
	    }
	/**
	 * 获取指定时间 返回Date
	 */
	public static Date getAppointDateFormatDate(Date date,Integer i){
		if(date==null)
			return null;
		if(i == null)
			i=0;
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR,i);
		return calendar.getTime();
	}
	/**
	 * 获取当前账务日期的年月：yyyyMM
	 */
	public static String getCurrYearMonth(int month,String accountDate){
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyyMMdd").parse(accountDate));
		} catch (ParseException e) {
			return null;
		}
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
		c.add(Calendar.YEAR,0);
		c.add(Calendar.MONTH,month);
		return format2.format(c.getTime());
	}
	/**
	 * 获取当前账务日期的年：yyyy 
	 */
	public static String getCurrYear(int year,String accountDate){
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyyMMdd").parse(accountDate));
		} catch (ParseException e) {
			return null;
		}
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy");
		c.add(Calendar.YEAR,year);
		return format2.format(c.getTime());
	}
}