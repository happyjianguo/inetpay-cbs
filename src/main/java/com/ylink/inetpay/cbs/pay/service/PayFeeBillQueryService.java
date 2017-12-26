package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDto;



public interface PayFeeBillQueryService {

	/**
	 * 分页查询手续费信息
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<PayFeeBillDto> listPage(PageData<PayFeeBillDto> pageData, PayFeeBillDto queryParam);
	
	PayFeeBillDto selectedById(String busiId);
}
