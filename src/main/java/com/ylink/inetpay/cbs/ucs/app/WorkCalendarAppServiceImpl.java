package com.ylink.inetpay.cbs.ucs.app;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.ucs.sec.service.WorkCalendarService;
import com.ylink.inetpay.common.core.constant.WorkDayTypeEnum;
import com.ylink.inetpay.common.project.cbs.app.WorkCalendarAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.WorkCalendarDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("workCalendarAppService")
public class WorkCalendarAppServiceImpl implements WorkCalendarAppService {
	@Autowired
	private WorkCalendarService workCalendarService;

	@Override
	public WorkCalendarDto get(String dayId) throws CbsCheckedException {
		return workCalendarService.get(dayId);
	}

	@Override
	public void init(String year) throws CbsCheckedException {
		workCalendarService.init(year);
		
	}

	@Override
	public void save(WorkCalendarDto workCalendar) throws CbsCheckedException {
		workCalendarService.save(workCalendar);
		
	}

	@Override
	public void update(WorkCalendarDto workCalendar) throws CbsCheckedException {
		workCalendarService.update(workCalendar);
		
	}

	@Override
	public void update(String dayId, WorkDayTypeEnum workDayType) throws CbsCheckedException {
		workCalendarService.update(dayId, workDayType);
		
	}

	@Override
	public boolean isWorkDay(Date date) throws CbsCheckedException {
		
		return workCalendarService.isWorkDay(date);
	}

	@Override
	public String findNextWorkDay(Date time) throws CbsCheckedException {
		
		return workCalendarService.findNextWorkDay(time);
	}

	@Override
	public String findCurrrentOrNextWorkDay(Date time) throws CbsCheckedException {
		
		return workCalendarService.findCurrrentOrNextWorkDay(time);
	}

	@Override
	public PageData<WorkCalendarDto> list(PageData<WorkCalendarDto> pageData, String dayId, WorkDayTypeEnum workDayType)
			throws CbsCheckedException {
		
		return workCalendarService.list(pageData, dayId, workDayType);
	}

	@Override
	public String getMonthFirstWorkDay(int month) throws CbsCheckedException {
		
		return workCalendarService.getMonthFirstWorkDay(month);
	}

	@Override
	public String getMonthSecondWorkDay(int month) throws CbsCheckedException {
		
		return workCalendarService.getMonthSecondWorkDay(month);
	}

	@Override
	public String getMonthLastWorkDay(int month) throws CbsCheckedException {
		
		return workCalendarService.getMonthLastWorkDay(month);
	}

	@Override
	public String getMonthSecondLastWorkDay(int month) throws CbsCheckedException {
		
		return workCalendarService.getMonthSecondLastWorkDay(month);
	}

	@Override
	public String getYearFirstWorkDay(int year) throws CbsCheckedException {
		
		return workCalendarService.getYearFirstWorkDay(year);
	}

	@Override
	public String getYearSecondWorkDay(int year) throws CbsCheckedException {
		
		return workCalendarService.getYearSecondWorkDay(year);
	}

	@Override
	public String getYearLastWorkDay(int year) throws CbsCheckedException {
		
		return workCalendarService.getYearLastWorkDay(year);
	}

	@Override
	public String getYearSecondLastWorkDay(int year) throws CbsCheckedException {
		
		return workCalendarService.getYearSecondLastWorkDay(year);
	}

}
