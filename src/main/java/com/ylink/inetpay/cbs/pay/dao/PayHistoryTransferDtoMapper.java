package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayHistoryTransferDto;
@MybatisMapper("payHistoryTransferDtoMapper")
public interface PayHistoryTransferDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayHistoryTransferDto record);

    int insertSelective(PayHistoryTransferDto record);

    PayHistoryTransferDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayHistoryTransferDto record);

    int updateByPrimaryKey(PayHistoryTransferDto record);
}