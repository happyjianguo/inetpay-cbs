package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.pay.app.PayFeeSummaryDtoAppService;


/**
 * 手续费汇总
 * @author pst09
 *
 */
public class PayFeeSummarySchedulerServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(PayFeeSummarySchedulerServiceImpl.class);
	
	@Autowired
	private PayFeeSummaryDtoAppService payFeeSummaryDtoAppService;

	@Override
	public void execute() {
		try {
			payFeeSummaryDtoAppService.feeSummary();
		} catch (Exception e) {
			_log.error("代付手续费生效操作失败", e);
		}
	}

}
