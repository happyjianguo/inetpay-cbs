package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsBankFeeRepDto;
@MybatisMapper("clsBankFeeRepDtoMapper")
public interface ClsBankFeeRepDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ClsBankFeeRepDto record);

    int insertSelective(ClsBankFeeRepDto record);

    ClsBankFeeRepDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ClsBankFeeRepDto record);

    int updateByPrimaryKey(ClsBankFeeRepDto record);
    
	public List<ClsBankFeeRepDto> queryBankFee(ClsBankFeeRepDto report);
}