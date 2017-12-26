package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.ESchedJobQueueStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedPlanDetailDto;

@MybatisMapper("bisSchedPlanDetailDtoMapper")
public interface BisSchedPlanDetailDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisSchedPlanDetailDto record);

    int insertSelective(BisSchedPlanDetailDto record);

    BisSchedPlanDetailDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisSchedPlanDetailDto record);

    int updateByPrimaryKey(BisSchedPlanDetailDto record);
    
    /**
	 * 获取任务计划详情并锁住该记录
	 * 
	 * @description
	 * @param id
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
    BisSchedPlanDetailDto getWithLock(String id);
    
    /**
	 * 批量保存任务计划详情
	 * 
	 * @description
	 * @param jpdList
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	int saveList(@Param("jpdList") List<BisSchedPlanDetailDto> jpdList);
	
	/**
	 * 获取所有任务计划详情
	 * 
	 * @description
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	List<BisSchedPlanDetailDto> listAll();

	/**
	 * 获取任务计划详情列表
	 * 
	 * @description
	 * @param jobPlanId
	 *            任务计划ID
	 * @return
	 * @author LS
	 * @date 2013-9-4
	 */
	List<BisSchedPlanDetailDto> list(String jobPlanId);

	/**
	 * 获取依赖未成功的任务计划详情的任务详情列表
	 * 
	 * @description
	 * @param jobDetailId
	 *            被依赖的，未执行成功的任务详情ID
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	List<BisSchedPlanDetailDto> listRelyOnUnsuccessful(String jobDetailId);

	/**
	 * 更新执行时间
	 * 
	 * @description
	 * @param id
	 * @param fireTime
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	int updateFireTimeAfterInvoke(@Param("id") String id, @Param("fireTime") Date fireTime);

	/**
	 * 定时任务调用完成更新状态 
	 * 
	 * @description
	 * @param id
	 * @param newValue
	 * @return
	 * @author LS
	 * @date 2013-9-3
	 */
	int updateStatusAfterInvoke(@Param("id") String id,@Param("newValue") ESchedJobQueueStatus newValue);
	
	/**
	 * 统计未成功的计划明细
	 * 
	 * @param planId
	 * @return
	 */
	long countUnSuccessPlanDetails(String planId);
	
	/**
	 * 统计未成功的计划明细
	 * 
	 * @param planId
	 * @return
	 */
	long countUnSuccessPlanDetailsByDetailIds(@Param("planDetailIdList")List<String> planDetailIdList);
}