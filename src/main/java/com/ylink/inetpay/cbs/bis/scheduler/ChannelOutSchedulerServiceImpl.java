package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.constant.EChlFrontSessionCode;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.channel.app.ChlFrontSessionAppService;

/**
 * 渠道签退
 * 
 * @author yuqingjun
 *
 */
public class ChannelOutSchedulerServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(ChannelOutSchedulerServiceImpl.class);

	@Autowired
	ChlFrontSessionAppService chlFrontSessionAppService;

	@Override
	public void execute() {
		try {
			_log.info("Cbs进行签退处理.");
			chlFrontSessionAppService.loginOrOut(EChlFrontSessionCode._LOGOUT);
		} catch (Exception e) {
			_log.error("渠道签退失败：" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}

}
