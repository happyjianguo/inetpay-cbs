package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.cbs.app.CbsPayFeeConfigAppService;
/**
 * 代付手续费生效
 * @author pst09
 *
 */
public class PayFeeConfigSchedulerServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(PayFeeConfigSchedulerServiceImpl.class);
	
	@Autowired
	private CbsPayFeeConfigAppService cbsPayFeeConfigAppService;

	@Override
	public void execute() {
		try {
			cbsPayFeeConfigAppService.payFeeConfigEffect();
		} catch (Exception e) {
			_log.error("代付手续费生效操作失败：{}" +e);
		}
	}

}
