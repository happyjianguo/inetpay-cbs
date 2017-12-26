package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayRechargeDto;
@MybatisMapper("payRechargeDtoMapper")
public interface PayRechargeDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayRechargeDto record);

    int insertSelective(PayRechargeDto record);

    PayRechargeDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayRechargeDto record);

    int updateByPrimaryKey(PayRechargeDto record);
    /**
     * 根据参数查询所有充值订单数据
     * @param payRechargeDto
     * @return
     */
    List<PayRechargeDto> queryAllData(PayRechargeDto payRechargeDto);
    /**
     * 根据平台业务订单号查询
     * @param busiId
     * @return
     */
    PayRechargeDto selectByBusiId(String busiId);
    /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayRechargeDto payRechargeDto);
}