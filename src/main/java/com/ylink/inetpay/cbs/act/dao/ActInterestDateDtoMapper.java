package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActInterestDateDto;

@MybatisMapper("actInterestDateDtoMapper")
public interface ActInterestDateDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActInterestDateDto record);

    int insertSelective(ActInterestDateDto record);

    ActInterestDateDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActInterestDateDto record);

    int updateByPrimaryKey(ActInterestDateDto record);
    
    List<ActInterestDateDto> list(ActInterestDateDto queryParam);
}