package com.ylink.inetpay.cbs.mrs.dao;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAssignShieNoSubDto;

@MybatisMapper("mrsAssignShieNoSubDtoMapper")
public interface MrsAssignShieNoSubDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(MrsAssignShieNoSubDto record);

	int insertSelective(MrsAssignShieNoSubDto record);

	MrsAssignShieNoSubDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(MrsAssignShieNoSubDto record);

	int updateByPrimaryKey(MrsAssignShieNoSubDto record);

	/**
	 * 根据关联表主键；来源系统查询
	 * 
	 * @param refId
	 * @param source
	 * @return
	 */
	MrsAssignShieNoSubDto selectByRefIdAndSource(@Param("refId") String refId, @Param("source") String source);

}