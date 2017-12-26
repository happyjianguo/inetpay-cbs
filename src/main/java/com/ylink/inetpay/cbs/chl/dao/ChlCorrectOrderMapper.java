package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.ChlCorrectOrderDto;
@MybatisMapper("chlCorrectOrderMapper")
public interface ChlCorrectOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChlCorrectOrderDto record);

    int insertSelective(ChlCorrectOrderDto record);

    ChlCorrectOrderDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChlCorrectOrderDto record);

    int updateByPrimaryKey(ChlCorrectOrderDto record);
    
    List<ChlCorrectOrderDto> findAll(ChlCorrectOrderDto queryParam);
}