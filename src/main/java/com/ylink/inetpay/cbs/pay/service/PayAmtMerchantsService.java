package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;

/**
 * 批次商户对外支付列表
 * @author yc
 *
 */

public interface PayAmtMerchantsService {

	/**
	 * 分页查询批次商户对外支付列表
	 * queryParam
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	PageData<PayAmtAllocateBatchDto> queryAllDate(PageData<PayAmtAllocateBatchDto> pageData,PayAmtAllocateBatchDto queryParam);

	 
}
