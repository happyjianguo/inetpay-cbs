package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayTransferDto;

public interface PayTransferService {
	/**
	 * 查询转账订单数据
	 * 
	 * @param pageDate
	 * @param PayTransferDto
	 * @return
	 */
	PageData<PayTransferDto> queryAllData(PageData<PayTransferDto> pageDate,
			PayTransferDto payTransferDto);

	/**
	 * 根据平台业务订单号查询
	 * 
	 * @param busiId
	 * @return
	 */
	PayTransferDto selectByBusiId(String busiId);
	 /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayTransferDto payTransferDto);
}
