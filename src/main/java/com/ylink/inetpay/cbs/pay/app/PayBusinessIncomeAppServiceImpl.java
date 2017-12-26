package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayBusinessIncomeService;
import com.ylink.inetpay.common.project.pay.app.PayBusinessIncomeAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;
@Service("payBusinessIncomeAppService")
public class PayBusinessIncomeAppServiceImpl implements PayBusinessIncomeAppService {
	 @Autowired
	 private PayBusinessIncomeService payBusinessIncomeService;

	@Override
	public PageData<PayFeeSummaryDto> queryAll(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam) {
		return payBusinessIncomeService.queryAll(pageData,queryParam);
	}

	@Override
	public PayFeeSummaryDto findSumPayAmount(PayFeeSummaryDto queryParam) {
		 
		return payBusinessIncomeService.findSumPayAmount(queryParam);
	}

	@Override
	public List<PayFeeSummaryDto> findAll(PayFeeSummaryDto queryParam) {
	 
		return payBusinessIncomeService.findAll(queryParam);
	}

}
