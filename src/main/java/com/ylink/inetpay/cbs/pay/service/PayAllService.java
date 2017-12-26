package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayAllDto;

public interface PayAllService {
	public PageData<PayAllDto> queryDate(
			PageData<PayAllDto> pageData, PayAllDto queryparam);
}
