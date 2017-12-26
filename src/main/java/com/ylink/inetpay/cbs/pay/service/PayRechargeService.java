package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayRechargeDto;

public interface PayRechargeService {
	/**
	 * 查询充值订单数据
	 * 
	 * @param pageDate
	 * @param payRechargeDto
	 * @return
	 */
	PageData<PayRechargeDto> queryAllData(PageData<PayRechargeDto> pageDate,
			PayRechargeDto payRechargeDto);

	/**
	 * 根据平台业务订单号查询
	 * 
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
