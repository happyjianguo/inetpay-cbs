package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisMovingActMatchingAuditDto;

@MybatisMapper("bisMovingActMatchingAuditDtoMapper")
public interface BisMovingActMatchingAuditDtoMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(BisMovingActMatchingAuditDto record);

    int insertSelective(BisMovingActMatchingAuditDto record);

    BisMovingActMatchingAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisMovingActMatchingAuditDto record);

    int updateByPrimaryKey(BisMovingActMatchingAuditDto record);
    
	List<BisMovingActMatchingAuditDto> queryAll(BisMovingActMatchingAuditDto queryParam);

	List<BisMovingActMatchingAuditDto> findAllByBusIdAndStatus(String id);
}