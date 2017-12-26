package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryWithdrawDto;
@MybatisMapper("payHistoryWithdrawDtoMapper")
public interface PayHistoryWithdrawDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryWithdrawDto record);

    int insertSelective(PayHistoryWithdrawDto record);

    PayHistoryWithdrawDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryWithdrawDto record);

    int updateByPrimaryKey(PayHistoryWithdrawDto record);
}