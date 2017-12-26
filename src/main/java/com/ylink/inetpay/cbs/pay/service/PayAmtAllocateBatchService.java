package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;

/**
 * 
 * @author pst07
 *
 */
public interface PayAmtAllocateBatchService {
	/**
	 * @param pageData
	 * @param queryparam
	 * @return
	 */
	public PageData<PayAmtAllocateBatchDto> findAll(PageData<PayAmtAllocateBatchDto> pageData,PayAmtAllocateBatchDto queryparam);
	
}
