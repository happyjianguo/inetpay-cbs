package com.ylink.inetpay.cbs.pay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayFeeBillDetailQueryService;
import com.ylink.inetpay.common.project.cbs.app.PayFeeBillDetailQueryAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDetailDto;

@Service("payFeeBillDetailQueryAppService")
public class PayFeeBillDetailQueryAppServiceImpl implements PayFeeBillDetailQueryAppService {

	private static Logger log = LoggerFactory.getLogger(PayFeeBillDetailQueryAppServiceImpl.class);
	
	@Autowired
	private PayFeeBillDetailQueryService payFeeBillDetailQueryService;
	
	@Override
	public PageData<PayFeeBillDetailDto> listPage(PageData<PayFeeBillDetailDto> pageData,
			PayFeeBillDetailDto queryParam) {
		log.debug("PayFeeBillDetailQueryAppServiceImpl.listPage run....");
		return payFeeBillDetailQueryService.listPage(pageData, queryParam);
	}

}
