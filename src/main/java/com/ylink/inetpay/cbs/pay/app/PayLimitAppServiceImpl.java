package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.pay.service.PayLimitService;
import com.ylink.inetpay.common.core.constant.EPayLimitBusinessType;
import com.ylink.inetpay.common.project.cbs.app.PayLimitAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayLimitDto;
@Service("payLimitAppService")
public class PayLimitAppServiceImpl implements PayLimitAppService{
	@Autowired
	private PayLimitService payLimitService;
	@Override
	public List<PayLimitDto> getPayLimits(String custId)
			throws CbsCheckedException {
		return payLimitService.getPayLimits(custId);
	}
	@Override
	public void updatePayList(List<PayLimitDto> payLimits) throws CbsCheckedException {
		payLimitService.updatePayList(payLimits);		
	}
	@Override
	public PayLimitDto findByCustIdAndBusiType(String custId,EPayLimitBusinessType busiType)
	{
		return payLimitService.findByCustIdAndBusiType(custId, busiType);
	}
}
