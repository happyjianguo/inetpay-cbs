package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisSchedJobIntervalDto;

@MybatisMapper("bisSchedJobIntervalDtoMapper")
public interface BisSchedJobIntervalDtoMapper {
   
	int deleteByPrimaryKey(String triggerName);

    int insert(BisSchedJobIntervalDto record);

    int insertSelective(BisSchedJobIntervalDto record);

    BisSchedJobIntervalDto selectByPrimaryKey(String triggerName);

    int updateByPrimaryKeySelective(BisSchedJobIntervalDto record);

    int updateByPrimaryKey(BisSchedJobIntervalDto record);
    
    List<BisSchedJobIntervalDto> listAll();
}