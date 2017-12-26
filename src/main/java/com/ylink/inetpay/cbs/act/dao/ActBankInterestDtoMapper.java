package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActBankInterestDto;

@MybatisMapper("actBankInterestDtoMapper")
public interface ActBankInterestDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActBankInterestDto record);

    int insertSelective(ActBankInterestDto record);

    ActBankInterestDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActBankInterestDto record);

    int updateByPrimaryKey(ActBankInterestDto record);
    
    List<ActBankInterestDto> list(ActBankInterestDto record);
}