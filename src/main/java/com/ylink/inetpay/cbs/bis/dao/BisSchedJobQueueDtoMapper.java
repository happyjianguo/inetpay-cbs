package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.ESchedPlanType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobQueueDto;

@MybatisMapper("bisSchedJobQueueDtoMapper")
public interface BisSchedJobQueueDtoMapper {
    int deleteByPrimaryKey(String id);
    int deleteByRefId(String refId);

    int insert(BisSchedJobQueueDto record);

    int insertSelective(BisSchedJobQueueDto record);

    BisSchedJobQueueDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisSchedJobQueueDto record);

    int updateByPrimaryKey(BisSchedJobQueueDto record);
    
    /**
	 * 获取任务队列列表
	 * 
	 * @description
	 * @param jobTypeId
	 * @return
	 * @author LS
	 * @date 2013-9-2
	 */
	List<BisSchedJobQueueDto> list(@Param("planType")ESchedPlanType planType);
	
	/**
	 * 
	 * @param jobTypeId
	 * @return
	 */
	List<BisSchedJobQueueDto> listWithLock(@Param("planType")ESchedPlanType planType);
	
	/**
	 * 获取任务队列
	 * 
	 * @description
	 * @param jobType
	 *            任务类型
	 * @param refId
	 *            关联计划详情ID
	 * @return
	 * @author LS
	 * @date 2013-9-2
	 */
	BisSchedJobQueueDto getByPlanTypeRefId(@Param("planType") ESchedPlanType planType,
			@Param("refId") String refId);

	/**
	 * 批量保存任务队列
	 * 
	 * @description
	 * @param records
	 * @return
	 * @author LS
	 * @date 2013-9-2
	 */
	int saveList(@Param("jobQueueList") List<BisSchedJobQueueDto> records);
}