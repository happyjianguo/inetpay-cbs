package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.WorkDayTypeEnum;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.WorkCalendarDto;


@MybatisMapper("workCalendarDtoMapper")
public interface WorkCalendarDtoMapper {
	int deleteByPrimaryKey(String dayId);

	int insert(WorkCalendarDto record);

	int insertSelective(WorkCalendarDto record);

	WorkCalendarDto selectByPrimaryKey(String dayId);

	int updateByPrimaryKeySelective(WorkCalendarDto record);

	int updateByPrimaryKey(WorkCalendarDto record);

	/**
	 * 根据传入工作日返回下一工作日.
	 * 
	 * @param time
	 * @return
	 */
	String findNextWorkDay(String time);

	/**
	 * 列出分页数据
	 * 
	 * @description
	 * @param dayId
	 * @param workDayType
	 * @return
	 * @author LS
	 * @date 2013-10-17
	 */
	List<WorkCalendarDto> list(@Param("dayId") String dayId,
			@Param("workDayType") WorkDayTypeEnum workDayType);
	 /**
     * 获取指定月第一个工作日
     * @return
     */
    String getMonthFirstWorkDay(String dayId);

    /**
     * 获取指定月第二个工作日
     * @return
     */
    String getMonthSecondWorkDay(String dayId);

    /**
     * 获取指定月最后一个工作日
     * @return
     */
    String getMonthLastWorkDay(String dayId);

    /**
     * 获取指定月倒数第二个工作日
     * @return
     */
    String getMonthSecondLastWorkDay(String dayId);

    /**
     * 获取指定年第一个工作日
     * @return
     */
    String getYearFirstWorkDay(String dayId);

    /**
     * 获取指定年第二个工作日
     * @return
     */
    String getYearSecondWorkDay(String dayId);

    /**
     * 获取指定年最后一个工作日
     * @return
     */
    String getYearLastWorkDay(String dayId);

    /**
     * 获取指定年倒数第二个工作日
     * @return
     */
    String getYearSecondLastWorkDay(String dayId);

}