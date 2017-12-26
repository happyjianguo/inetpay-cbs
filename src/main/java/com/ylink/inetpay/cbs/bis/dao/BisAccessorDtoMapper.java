package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccessorDto;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("bisAccessorDtoMapper")
public interface BisAccessorDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisAccessorDto record);

    int insertSelective(BisAccessorDto record);

    BisAccessorDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisAccessorDto record);

    int updateByPrimaryKey(BisAccessorDto record);

	List<BisAccessorDto> list(BisAccessorDto queryParam);
	
	long getAccessorQuence();

	List<BisAccessorDto> queryCustId();
}