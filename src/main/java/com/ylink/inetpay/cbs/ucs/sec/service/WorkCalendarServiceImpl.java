package com.ylink.inetpay.cbs.ucs.sec.service;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.dao.WorkCalendarDtoMapper;
import com.ylink.inetpay.cbs.util.DateUtils;
import com.ylink.inetpay.common.core.constant.WorkDayTypeEnum;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.WorkCalendarDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

/**
 * @author LS
 * @date 2013-10-17
 * @description：
 */
@Transactional(value=CbsConstants.TX_MANAGER_UCS)
@Service("workCalendarService")
public class WorkCalendarServiceImpl implements WorkCalendarService {

	@Autowired
	private WorkCalendarDtoMapper workCalendarMapper;
	@Autowired
	private  ActaccountDateService actaccountDateService;
	private final String DATE_FORMAT_STR = "yyyyMMdd";

	@Override
	public void init(String year) {

		matchesPattern(year, "\\d{4}", "年份不正确.");

		WorkCalendarDto entity = null;
		// 星期几.
		int dayOfWeek;
		// 是否为工作日.
		WorkDayTypeEnum workDayType = null;

		int unit = 1;
		// 根据传入的年份设置并把月和日设置为1.
		LocalDate date = new LocalDate(Integer.valueOf(year), unit, unit);
		// 最大天数.
		int maxDay = date.dayOfYear().getMaximumValue();
		// 最小天数.
		int minDay = date.dayOfYear().getMinimumValue();
		// 先减少一天，再从1月1日开始.
		date = date.minusDays(unit);
		for (int i = minDay; i <= maxDay; i++) {
			// 增加一天.
			date = date.plusDays(unit);

			dayOfWeek = date.dayOfWeek().get();
			workDayType = checkWorkDayType(dayOfWeek);

			String id = date.toString(DATE_FORMAT_STR);
			entity = get(id);

			// 新增
			if (null == entity) {
				entity = new WorkCalendarDto(id, workDayType);
				save(entity);
			}
			// 修改
			else {
				entity.setWorkDayType(workDayType);
				update(entity);
			}
		}
	}

	@Override
	public void save(WorkCalendarDto workCalendar) {
		workCalendarMapper.insert(workCalendar);
	}

	@Override
	public void update(WorkCalendarDto workCalendar) {
		workCalendarMapper.updateByPrimaryKeySelective(workCalendar);
	}

	@Override
	public void update(String dayId, WorkDayTypeEnum workDayType) {
		WorkCalendarDto entity = get(dayId);
		entity.setWorkDayType(workDayType);
		update(entity);
	}

	@Override
	public boolean isWorkDay(Date date) {
		String dayId = formateDate(date);
		WorkCalendarDto entity = get(dayId);
		if (entity == null) {
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(), "日期" + dayId + "没有初始化");
		}
		return entity.getWorkDayType() == WorkDayTypeEnum.YES;
	}

	@Override
	public String findNextWorkDay(Date time) {
		if (time == null) {
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"传入的日期不能为空.");
		}
		return workCalendarMapper.findNextWorkDay(formateDate(time));
	}

	@Override
	public PageData<WorkCalendarDto> list(PageData<WorkCalendarDto> pageData, String dayId, WorkDayTypeEnum workDayType) {
		if(pageData == null){
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"传入的pageData不能为空.");
		}
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<WorkCalendarDto> list = workCalendarMapper.list(dayId, workDayType);
		
		Page<WorkCalendarDto> page = (Page<WorkCalendarDto>)list;
		
		pageData.setRows(list);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	private String formateDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STR);
		return sdf.format(date);
	}

	private void matchesPattern(CharSequence input, String pattern, String message) {
		if (!Pattern.matches(pattern, input))
			throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),message);
	}

	/**
	 * 校验dayOfWeek是不工作日.
	 * 
	 * @param dayOfWeek
	 * @return
	 */
	private WorkDayTypeEnum checkWorkDayType(int dayOfWeek) {
		switch (dayOfWeek) {
		/* 非工作日. */
		case 6:
		case 7:
			return WorkDayTypeEnum.NO;
			/* 工作日. */
		default:
			return WorkDayTypeEnum.YES;
		}
	}

	@Override
	public WorkCalendarDto get(String dayId) {
		return workCalendarMapper.selectByPrimaryKey(dayId);
	}

	@Override
	public String findCurrrentOrNextWorkDay(Date time) {
		boolean b = isWorkDay(time);
		if (b)
			return new SimpleDateFormat("yyyyMMdd").format(time);
		return findNextWorkDay(time);
	}

	@Override
	public String getMonthFirstWorkDay(int month) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYearMonth(month, accountDate);
		return workCalendarMapper.getMonthFirstWorkDay(dayId);
	}

	@Override
	public String getMonthSecondWorkDay(int month) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYearMonth(month, accountDate);
		return workCalendarMapper.getMonthSecondWorkDay(dayId);
	}

	@Override
	public String getMonthLastWorkDay(int month) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYearMonth(month, accountDate);
		return workCalendarMapper.getMonthLastWorkDay(dayId);
	}

	@Override
	public String getMonthSecondLastWorkDay(int month) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYearMonth(month, accountDate);
		return workCalendarMapper.getMonthSecondLastWorkDay(dayId);
	}

	@Override
	public String getYearFirstWorkDay(int year) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYear(year, accountDate);
		return workCalendarMapper.getYearFirstWorkDay(dayId);
	}

	@Override
	public String getYearSecondWorkDay(int year) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYear(year, accountDate);
		return workCalendarMapper.getYearSecondWorkDay(dayId);
	}

	@Override
	public String getYearLastWorkDay(int year) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYear(year, accountDate);
		return workCalendarMapper.getYearLastWorkDay(dayId);
	}

	@Override
	public String getYearSecondLastWorkDay(int year) {
		// 账务日期
		String accountDate = actaccountDateService.getAccountDate();
		String dayId = DateUtils.getCurrYear(year, accountDate);
		return workCalendarMapper.getYearSecondLastWorkDay(dayId);
	}

}
