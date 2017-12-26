package com.ylink.inetpay.cbs.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
/**
 * @类名称： DateUtil
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-30 下午7:18:46
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 下午7:18:46
 * @操作原因： 
 * 
 */
public class DateUtil {
	protected static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	/**
	 * 時間加一天減一秒
	 * @param d
	 * @return
	 */
	public static Date endOfDay(Date d) {
			Calendar calender = Calendar.getInstance();
			 calender.setTime(d);
			 calender.add(Calendar.DATE,1);
		     calender.add(Calendar.SECOND, -1);
		     return calender.getTime();
	}
	/**
	 * 獲取兩時間月份差
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
 
        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);
        int day=endCalendar.get(Calendar.DATE)
                - startCalendar.get(Calendar.DATE);
        if(day>0){
        	return year*12+month+1;
        }else{
        	return year*12+month;
        }
    }
	/**
	 * 獲取兩時間年份差
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getYear(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
 
        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);
        int day=endCalendar.get(Calendar.DATE)
                - startCalendar.get(Calendar.DATE);
        if(month>0 || day>0){
        	return year+1;
        }else{
        	return year;
        }
    }
	/**
	 * 獲取兩時間天數差
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	/* public static int getDay(Date smdate,Date bdate) throws Exception
	 {    
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        smdate=sdf.parse(sdf.format(smdate));  
	        bdate=sdf.parse(sdf.format(bdate));  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(smdate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(bdate);    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));           
	    }    */
	/**
	 * 獲取兩時間天數差
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDay(Date start, Date end) {
       /* if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        return endCalendar.get(Calendar.DATE)
                - startCalendar.get(Calendar.DATE);*/
		if(start!=null && end!=null){
			return (int) ((end.getTime()-start.getTime())/1000/60/60/24);
		}
		return 0;
    }
	
	
	/**
	 * @方法描述:根据日期格式yyyymm得到上个月信息
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午7:18:48
	 * @param calDate
	 * @return 
	 * @返回类型： String
	*/
	public static  String getPreMonth(String calDate){
		String year=null,month=null;
		if(calDate==null || calDate.length() != 6){
			return calDate;
		}
		month=calDate.subSequence(4, 6).toString();
		year=calDate.subSequence(0, 2).toString();
		Integer m=Integer.parseInt(month);
		m=m==1?12:m-1;
		month=m>9?m.toString():"0"+m;
		return year+month;
	}
	/**
	 * 将yyyy-MM-dd转换为yyyyMMdd
	 */
	public static String dateStringToString(String str){
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
	     SimpleDateFormat sf2 =new SimpleDateFormat("yyyyMMdd");
	     String sfstr = "";
	     try {
	      sfstr = sf2.format(sf1.parse(str));
	  } catch (ParseException e) {
		  logger.error("系统异常，异常原因：{}",ExceptionProcUtil.getExceptionDesc(e));
	  }
	  return sfstr;
	}
	/**
	 * 将yyyyMMdd转换为yyyy-MM-dd
	 */
	public static String dateStringToymd(String str){
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
	     SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
	     String sfstr = "";
	     try {
	      sfstr = sf2.format(sf1.parse(str));
	  } catch (ParseException e) {
		  logger.error("系统异常，异常原因：{}",ExceptionProcUtil.getExceptionDesc(e));
	  }
	  return sfstr;
	}
}
