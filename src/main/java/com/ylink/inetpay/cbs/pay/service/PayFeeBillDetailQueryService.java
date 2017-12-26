package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDetailDto;


public interface PayFeeBillDetailQueryService {

	public PageData<PayFeeBillDetailDto> listPage(PageData<PayFeeBillDetailDto> pageData, PayFeeBillDetailDto queryParam);
	
}
