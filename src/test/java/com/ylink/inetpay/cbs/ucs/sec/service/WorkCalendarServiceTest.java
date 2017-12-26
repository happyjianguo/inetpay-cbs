package com.ylink.inetpay.cbs.ucs.sec.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;

public class WorkCalendarServiceTest extends UCBaseTest {
	
	@Autowired
	private WorkCalendarService workCalendarService;

	@Test
	public void testwork() {

	    
		 System.out.println("获取指定月第一个工作日"+workCalendarService.getMonthFirstWorkDay(0)); 
		 System.out.println("获取指定月第二个工作日"+workCalendarService.getMonthSecondWorkDay(0)); 
		 System.out.println("获取指定月最后一个工作日"+workCalendarService.getMonthLastWorkDay(0)); 
		 System.out.println("获取指定月倒数第二个工作日"+workCalendarService.getMonthSecondLastWorkDay(0)); 
		 
		 System.out.println("获取指定年第一个工作日"+workCalendarService.getYearFirstWorkDay(0)); 
		 System.out.println("获取指定年第二个工作日"+workCalendarService.getYearSecondWorkDay(0)); 
		 System.out.println("获取指定年最后一个工作日"+workCalendarService.getYearLastWorkDay(0)); 
		 System.out.println("获取指定年倒数第二个工作日"+workCalendarService.getYearSecondLastWorkDay(0)); 
	}
}
