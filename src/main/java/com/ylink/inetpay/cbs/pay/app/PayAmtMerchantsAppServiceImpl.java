package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayAmtMerchantsService;
import com.ylink.inetpay.common.project.pay.app.PayAmtMerchantsAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;

@Service("payAmtMerchantsAppService")
public class PayAmtMerchantsAppServiceImpl implements  PayAmtMerchantsAppService {
	 @Autowired
	 private PayAmtMerchantsService payAmtMerchantsService;

	@Override
	public PageData<PayAmtAllocateBatchDto> queryAllDate(PageData<PayAmtAllocateBatchDto> pageData,PayAmtAllocateBatchDto queryParam) {
	 
		return payAmtMerchantsService.queryAllDate(pageData,queryParam);
	}
	 
	 
}
