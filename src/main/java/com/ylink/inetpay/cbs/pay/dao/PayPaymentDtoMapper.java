package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayPaymentDto;
@MybatisMapper("payPaymentDtoMapper")
public interface PayPaymentDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayPaymentDto record);

    int insertSelective(PayPaymentDto record);

    PayPaymentDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayPaymentDto record);

    int updateByPrimaryKey(PayPaymentDto record);
    /**
     * 根据参数查询所有订单数据
     * @param PayPaymentDto
     * @return
     */
    List<PayPaymentDto> queryAllData(PayPaymentDto payPaymentDto);
    /**
     * 根据参数查询所有订单数据
     * @param PayPaymentDto
     * @return
     */
    List<PayPaymentDto> queryAuditAllData(PayPaymentDto payPaymentDto);
    /**
     * 根据平台业务订单号查询
     * @param busiId
     * @return
     */
    PayPaymentDto selectByBusiId(String busiId);
    /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayPaymentDto payPaymentDto);
    /**
     * 根据auditId查询
     * @param auditId
     * @return
     */
    PayPaymentDto selectByAuditDataId(@Param("auditId") String auditId);
}