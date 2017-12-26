package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.pay.app.PayPaymentAppService;

/**
 * 支付成功结果通知
 * @author LS
 *
 */
public class PaySuccessNoticeSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(PaySuccessNoticeSchedulerServiceImpl.class);
	
	@Autowired
	PayPaymentAppService payPaymentAppService;
	
	@Override
	public void execute() {
		try {
			payPaymentAppService.noticeMerchent();
		} catch (Exception e) {
			_log.error("支付成功结果通知失败：" +ExceptionProcUtil.getExceptionDesc(e));
		}
	}

}
