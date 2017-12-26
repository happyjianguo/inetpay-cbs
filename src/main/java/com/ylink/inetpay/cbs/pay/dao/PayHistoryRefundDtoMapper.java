package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryRefundDto;
@MybatisMapper("payHistoryRefundDtoMapper")
public interface PayHistoryRefundDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryRefundDto record);

    int insertSelective(PayHistoryRefundDto record);

    PayHistoryRefundDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryRefundDto record);

    int updateByPrimaryKey(PayHistoryRefundDto record);
}