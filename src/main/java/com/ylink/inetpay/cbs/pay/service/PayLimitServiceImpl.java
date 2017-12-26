package com.ylink.inetpay.cbs.pay.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.pay.dao.PayLimitDtoMapper;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.EPayLimitBusinessType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.pay.app.PayLimitAppService;
import com.ylink.inetpay.common.project.pay.dto.PayLimitDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;
@Service("payLimitService")
public class PayLimitServiceImpl implements PayLimitService {
	@Autowired
	private PayLimitDtoMapper payLimitDtoMapper;
	@Autowired
	private PayLimitAppService paySystemPayLimitAppService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public List<PayLimitDto> getPayLimits(String custId) {
		return payLimitDtoMapper.getPayLimits(custId);
	}
	@Override
	public void updatePayList(List<PayLimitDto> payLimits){
		//调用支付系统的接口
		try {
		paySystemPayLimitAppService.updatePayLimit(payLimits);
		} catch (PayCheckedException e) {
			_log.error("修改支付限额："+e.getMessage());
			throw new CbsUncheckedException(e.getCode(), e.getMessage());
		}catch (Exception e) {
			_log.error("修改支付限额：调用支付系统超时:{}",ExceptionProcUtil.getExceptionDesc(e));
			throw new CbsUncheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(), "调用支付系统超时");
		}
	}
	@Override
	public PayLimitDto findByCustIdAndBusiType(String custId,EPayLimitBusinessType busiType) {
		return payLimitDtoMapper.findByCustIdAndBusiType( custId, busiType);
	}
}
