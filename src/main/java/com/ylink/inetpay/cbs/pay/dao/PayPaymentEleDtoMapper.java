package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayPaymentEleDto;
@MybatisMapper("payPaymentEleDtoMapper")
public interface PayPaymentEleDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayPaymentEleDto record);

    int insertSelective(PayPaymentEleDto record);

    PayPaymentEleDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayPaymentEleDto record);

    int updateByPrimaryKey(PayPaymentEleDto record);
    /**
     * 根据业务订单号获取个性化要素集合
     * @param busiId
     * @return
     */
	List<PayPaymentEleDto> getAttachs(String busiId);
}