package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryBookDto;
@MybatisMapper("payHistoryBookDtoMapper")
public interface PayHistoryBookDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryBookDto record);

    int insertSelective(PayHistoryBookDto record);

    PayHistoryBookDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryBookDto record);

    int updateByPrimaryKey(PayHistoryBookDto record);
}