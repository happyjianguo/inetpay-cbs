package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryRechargeDto;
@MybatisMapper("payHistoryRechargeDtoMapper")
public interface PayHistoryRechargeDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryRechargeDto record);

    int insertSelective(PayHistoryRechargeDto record);

    PayHistoryRechargeDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryRechargeDto record);

    int updateByPrimaryKey(PayHistoryRechargeDto record);
}