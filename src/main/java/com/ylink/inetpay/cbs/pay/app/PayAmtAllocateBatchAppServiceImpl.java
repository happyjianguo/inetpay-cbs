package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayAmtAllocateBatchService;
import com.ylink.inetpay.common.project.pay.app.PayAmtAllocateBatchAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;
@Service("payAmtAllocateBatchAppService")
public class PayAmtAllocateBatchAppServiceImpl implements PayAmtAllocateBatchAppService {
	@Autowired
	private PayAmtAllocateBatchService payAmtAllocateBatchService;
	@Override
	public PageData<PayAmtAllocateBatchDto> listAll(
			PageData<PayAmtAllocateBatchDto> pageData, PayAmtAllocateBatchDto queryparam) {
		return payAmtAllocateBatchService.findAll(pageData, queryparam);
	}


}
