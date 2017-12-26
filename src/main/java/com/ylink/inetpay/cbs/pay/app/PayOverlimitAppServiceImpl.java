package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.pay.service.PayOverlimitService;
import com.ylink.inetpay.common.project.cbs.app.PayOverlimitAppService;
import com.ylink.inetpay.common.project.pay.dto.PayOverlimitDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("payOverlimitAppService")
public class PayOverlimitAppServiceImpl implements PayOverlimitAppService {
	@Autowired
	private PayOverlimitService payOverlimitService;
	@Override
	public PageData<PayOverlimitDto> singleOverLimitList(
			PageData<PayOverlimitDto> pageDate, PayOverlimitDto payOverlimitDto)
			throws CbsCheckedException {
		return payOverlimitService.singleOverLimitList(pageDate, payOverlimitDto);
	}
	public PageData<PayOverlimitDto> totalOverLimitList(
			PageData<PayOverlimitDto> pageDate, PayOverlimitDto payOverlimitDto)
			throws CbsCheckedException {
		return payOverlimitService.totalOverLimitList(pageDate, payOverlimitDto);
	}
	@Override
	public PayOverlimitDto details(String id) throws CbsCheckedException {
		return payOverlimitService.details(id);
	}

}
