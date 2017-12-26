package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.clear.app.ClearChannelCheckAppService;

/**
 * 检查超期未对账的资金渠道流水和支付抽取流水
 * @author LS
 *
 */
public class CheckChannelUncheckSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(CheckChannelUncheckSchedulerServiceImpl.class);
	
	@Autowired
	ClearChannelCheckAppService clearChannelCheckAppService;

	@Override
	public void execute() {
		try {
			clearChannelCheckAppService.checkUnProcessRecord();
		} catch (Exception e) {
			_log.error("检查超期未对账资金渠道流水和支付流水失败：" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}

}
