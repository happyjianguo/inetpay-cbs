package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

public interface PayRefundService {
	/**
	 * 查询退款订单数据
	 * 
	 * @param pageDate
	 * @param PayRefundDto
	 * @return
	 */
	PageData<PayRefundDto> queryAllData(PageData<PayRefundDto> pageDate,
			PayRefundDto payRefundDto);

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
