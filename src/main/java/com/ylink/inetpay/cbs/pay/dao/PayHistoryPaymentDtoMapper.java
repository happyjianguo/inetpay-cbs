package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryPaymentDto;
@MybatisMapper("payHistoryPaymentDtoMapper")
public interface PayHistoryPaymentDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryPaymentDto record);

    int insertSelective(PayHistoryPaymentDto record);

    PayHistoryPaymentDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryPaymentDto record);

    int updateByPrimaryKey(PayHistoryPaymentDto record);
}