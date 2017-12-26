package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

@MybatisMapper("payRefundDtoMapper")
public interface PayRefundDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(PayRefundDto record);

	int insertSelective(PayRefundDto record);

	PayRefundDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(PayRefundDto record);

	int updateByPrimaryKey(PayRefundDto record);

	/**
	 * 根据参数查询所有订单数据
	 * 
	 * @param PayRefundDto
	 * @return
	 */
	List<PayRefundDto> queryAllData(PayRefundDto payRefundDto);

	/**
	 * 根据平台业务订单号查询
	 * 
	 * @param busiId
	 * @return
	 */
	PayRefundDto selectByBusiId(String busiId);
	 /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayRefundDto payRefundDto);
}