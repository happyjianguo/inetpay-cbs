package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayRedService;
import com.ylink.inetpay.common.project.pay.app.PayRedAppService;
import com.ylink.inetpay.common.project.pay.dto.PayRedPDto;
@Service("payRedAppService")
public class PayRedAppServiceImpl implements PayRedAppService {
	@Autowired
	private PayRedService payRedService;
	@Override
	public PageData<PayRedPDto> findAll(PageData<PayRedPDto> pageData,
			PayRedPDto queryparam) {
		return payRedService.findAll(pageData, queryparam);
	}

	@Override
	public PayRedPDto details(String id) {
		return payRedService.details(id);
	}

}
