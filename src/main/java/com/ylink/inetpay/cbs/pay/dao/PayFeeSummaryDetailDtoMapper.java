package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDetailDto;


@MybatisMapper("payFeeSummaryDetailDtoMapper")
public interface PayFeeSummaryDetailDtoMapper {

	/**
     * 根据查询条件查询手续费报表明细
     * @param queryParam
     * @return
     */
    List<PayFeeSummaryDetailDto> list(PayFeeSummaryDetailDto queryParam);
}
