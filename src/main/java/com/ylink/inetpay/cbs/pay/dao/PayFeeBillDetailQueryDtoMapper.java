package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDetailDto;


@MybatisMapper("payFeeBillDetailQueryDtoMapper")
public interface PayFeeBillDetailQueryDtoMapper {

	  /**
     * 根据查询条件查询手续费明细
     * @param queryParam
     * @return
     */
    List<PayFeeBillDetailDto> list(PayFeeBillDetailDto queryParam);
}
