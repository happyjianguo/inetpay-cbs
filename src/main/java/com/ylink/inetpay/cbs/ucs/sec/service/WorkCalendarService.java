package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.Date;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.WorkDayTypeEnum;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.WorkCalendarDto;

/**
 * @author yuqingjun
 * @date 2017-04-18
 */
public interface WorkCalendarService {

	/**
	 * 获取工作日历
	 * 
	 * @description
	 * @param dayId
	 * @return
	 * @author yuqingjun
	 * @date 2017-04-18
	 */
	WorkCalendarDto get(String dayId);

	/**
	 * 初始化工作日历.
	 * 
	 * @param year
	 */
	void init(String year);

	/**
	 * 新增
	 * 
	 * @description
	 * @param workCalendar
	 * @author yuqingjun
	 * @date 2013-10-17
	 */
	void save(WorkCalendarDto workCalendar);

	/**
	 * 更新
	 * 
	 * @description
	 * @param workCalendar
	 * @author yuqingjun
	 * @date 2013-10-17
	 */
	void update(WorkCalendarDto workCalendar);

	/**
	 * 更新
	 * 
	 * @description
	 * @param dayId
	 * @param workDayType
	 * @author yuqingjun
	 * @date 2013-10-17
	 */
	void update(String dayId, WorkDayTypeEnum workDayType);

	/**
	 * 判断当前日期是否为工作日，如果是，则返回true，否则则返回false
	 * 
	 * @description
	 * @param date
	 * @return
	 * @author yuqingjun
	 * @date 2013-10-17
	 */
	boolean isWorkDay(Date date);

	/**
	 * 根据传入工作日返回下一工作日.
	 * 
	 * @param time
	 * @return
	 */
	String findNextWorkDay(Date time);

	/**
	 * 如果传入的日期是工作日，则返回传入的日期，否则返回传入的日期之后的第一个工作日，返回格式为：yyyyMMdd
	 * 
	 * @description
	 * @param time
	 * @return
	 * @throws BusiAppCheckedException
	 * @author yuqingjun
	 * @date 2013-10-18
	 */
	String findCurrrentOrNextWorkDay(Date time);

	/**
	 * 分页
	 * 
	 * @description
	 * @param pageData
	 * @param dayId
	 * @param workDayType
	 * @return
	 * @author yuqingjun
	 * @date 2013-10-17
	 */
	PageData<WorkCalendarDto> list(PageData<WorkCalendarDto> pageData, String dayId, WorkDayTypeEnum workDayType);

	/**
	 * 获取指定月第一个工作日
	 * 
	 * @return
	 */
	String getMonthFirstWorkDay(int month);

	/**
	 * 获取指定月第二个工作日
	 * 
	 * @return
	 */
	String getMonthSecondWorkDay(int month);

	/**
	 * 获取指定月最后一个工作日
	 * 
	 * @return
	 */
	String getMonthLastWorkDay(int month);

	/**
	 * 获取指定月倒数第二个工作日
	 * 
	 * @return
	 */
	String getMonthSecondLastWorkDay(int month);

	/**
	 * 获取指定年第一个工作日
	 * 
	 * @return
	 */
	String getYearFirstWorkDay(int year);

	/**
	 * 获取指定年第二个工作日
	 * 
	 * @return
	 */
	String getYearSecondWorkDay(int year);

	/**
	 * 获取指定年最后一个工作日
	 * 
	 * @return
	 */
	String getYearLastWorkDay(int year);

	/**
	 * 获取指定年倒数第二个工作日
	 * 
	 * @return
	 */
	String getYearSecondLastWorkDay(int year);
}
