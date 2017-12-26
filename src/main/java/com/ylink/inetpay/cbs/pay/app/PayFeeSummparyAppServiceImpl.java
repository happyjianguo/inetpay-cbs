package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.pay.service.PayFeeSummparyService;
import com.ylink.inetpay.common.project.cbs.app.PayFeeSummparyAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

@Service("payFeeSummparyAppService")
public class PayFeeSummparyAppServiceImpl implements PayFeeSummparyAppService {
	@Autowired
	private PayFeeSummparyService payFeeSummparyService;
	@Override
	public PageData<PayFeeSummaryDto> pageList(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam) {
		return payFeeSummparyService.pageList(pageData,queryParam);
	}
	@Override
	public PayFeeSummaryDto detail(String id) {
		 
		return payFeeSummparyService.detail(id);
	} 
	 
}
