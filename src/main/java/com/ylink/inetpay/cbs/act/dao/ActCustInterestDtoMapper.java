package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActCustInterestDto;

@MybatisMapper("actCustInterestDtoMapper")
public interface ActCustInterestDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActCustInterestDto record);

    int insertSelective(ActCustInterestDto record);

    ActCustInterestDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActCustInterestDto record);

    int updateByPrimaryKey(ActCustInterestDto record);
    
    List<ActCustInterestDto> list(ActCustInterestDto record);
    
    long countList(ActCustInterestDto queryParam);
}