package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.channel.app.ChlAccountChangeNotifyAppService;

/**
 * 动账通知支付系统
 * 
 * @author lhui
 *
 */
public class ChlAccountChangeNotifyPayServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(ChlAccountChangeNotifyPayServiceImpl.class);

	@Autowired
	ChlAccountChangeNotifyAppService chlAccountChangeNotifyAppService;

	@Override
	public void execute() {
		_log.info("开始进行动账通知支付系统.");
		chlAccountChangeNotifyAppService.doNotifyPaySystem();
		_log.info("动账通知支付系统结束.");
	}

}
