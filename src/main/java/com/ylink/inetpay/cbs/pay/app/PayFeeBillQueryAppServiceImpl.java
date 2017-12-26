package com.ylink.inetpay.cbs.pay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayFeeBillQueryService;
import com.ylink.inetpay.common.project.cbs.app.PayFeeBillQueryAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDto;

@Service("payFeeBillQueryAppService")
public class PayFeeBillQueryAppServiceImpl implements PayFeeBillQueryAppService {

	private static Logger log = LoggerFactory.getLogger(PayFeeBillQueryAppServiceImpl.class);
	
	@Autowired
	private PayFeeBillQueryService payFeeBillQueryService;
	
	@Override
	public PageData<PayFeeBillDto> listPage(PageData<PayFeeBillDto> pageData, PayFeeBillDto queryParam) {
		log.debug("PayFeeBillQueryServiceAppImpl.listPage run....");
		return payFeeBillQueryService.listPage(pageData, queryParam);
	}

	@Override
	public PayFeeBillDto selectByBusyId(String busiId) {
		// TODO Auto-generated method stub
		return payFeeBillQueryService.selectedById(busiId);
	}
	

}
