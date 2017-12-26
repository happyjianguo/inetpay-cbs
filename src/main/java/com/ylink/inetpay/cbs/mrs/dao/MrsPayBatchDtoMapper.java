package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDto;

@MybatisMapper("mrsPayBatchDtoMapper")
public interface MrsPayBatchDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(MrsPayBatchDto record);

	int insertSelective(MrsPayBatchDto record);

	MrsPayBatchDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(MrsPayBatchDto record);

	int updateByPrimaryKey(MrsPayBatchDto record);

	/**
	 * 根据参数查询所有订单数据
	 * 
	 * @return
	 */
	List<MrsPayBatchDto> queryAllData(MrsPayBatchDto mrsPayBatchDto);
	/**
	 * 根据参数查询所有订单数据
	 * 
	 * @return
	 */
	List<MrsPayBatchDto> queryAllDataAudit(MrsPayBatchDto mrsPayBatchDto);
}