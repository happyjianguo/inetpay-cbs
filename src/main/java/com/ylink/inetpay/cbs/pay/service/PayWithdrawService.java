package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;

public interface PayWithdrawService {
	/**
	 * 查询提现订单数据
	 * 
	 * @param pageDate
	 * @param PayWithdrawDto
	 * @return
	 */
	PageData<PayWithdrawDto> queryAllData(PageData<PayWithdrawDto> pageDate,
			PayWithdrawDto payWithdrawDto);

	/**
	 * 根据平台业务订单号查询
	 * 
	 * @param busiId
	 * @return
	 */
	PayWithdrawDto selectByBusiId(String busiId);
	 /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayWithdrawDto payWithdrawDto);
}
