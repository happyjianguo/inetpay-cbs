package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.clear.app.ClearChannelCheckAppService;

/**
 * 渠道对账
 * 
 * @author LS
 *
 */
public class ChannelCheckSchedulerServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory
			.getLogger(ChannelCheckSchedulerServiceImpl.class);

	@Autowired
	ClearChannelCheckAppService clearChannelCheckAppService;

	@Autowired
	ActaccountDateService actaccountDateService;

	@Override
	public void execute() {
		try {
			clearChannelCheckAppService.check(actaccountDateService
					.getActAccountDateDto().getCurAccountDate());
		} catch (Exception e) {
			_log.error("渠道对账失败：" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}

}
