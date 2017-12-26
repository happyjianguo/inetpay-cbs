package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDto;

@MybatisMapper("bisSchedPlanDtoMapper")
public interface BisSchedPlanDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisSchedPlanDto record);

    int insertSelective(BisSchedPlanDto record);

    BisSchedPlanDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisSchedPlanDto record);

    int updateByPrimaryKey(BisSchedPlanDto record);
    
   	/**
   	 * 获取分页任务计划列表
   	 * 
   	 * @description
   	 * @param beginIndex
   	 * @param endIndex
   	 * @param jobPlanId
   	 * @param fireStartTime
   	 * @param fireEndTime
   	 * @return
   	 * @author LS
   	 * @date 2013-9-4
   	 */
   	List<BisSchedPlanDto> listJobPlan(@Param("planId") String planId,
   			@Param("accountDate") String accountDate);
}