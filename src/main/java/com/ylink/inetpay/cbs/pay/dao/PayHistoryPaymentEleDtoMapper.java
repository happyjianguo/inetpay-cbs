package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryPaymentEleDto;
@MybatisMapper("payHistoryPaymentEleDtoMapper")
public interface PayHistoryPaymentEleDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryPaymentEleDto record);

    int insertSelective(PayHistoryPaymentEleDto record);

    PayHistoryPaymentEleDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryPaymentEleDto record);

    int updateByPrimaryKey(PayHistoryPaymentEleDto record);
}