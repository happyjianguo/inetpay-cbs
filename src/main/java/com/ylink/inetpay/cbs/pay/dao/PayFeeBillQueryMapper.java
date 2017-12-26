package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDto;


@MybatisMapper("payFeeBillQueryMapper")
public interface PayFeeBillQueryMapper {

	  /**
     * 根据查询条件查询手续费
     * @param queryParam
     * @return
     */
    List<PayFeeBillDto> list(PayFeeBillDto queryParam);
    
    PayFeeBillDto selectedById(String busyId);
}
