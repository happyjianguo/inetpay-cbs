package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.act.service.ActaccountDateService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.chl.service.NoticeServiceImpl;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.clear.app.ClearChannelCheckAppService;

/**
 * 下载和解析资金渠道流水
 * @author LS
 *
 */
public class DownLoadChannelBookSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(DownLoadChannelBookSchedulerServiceImpl.class);
	
	@Autowired
	ClearChannelCheckAppService clearChannelCheckAppService;
	
	@Autowired
	ActaccountDateService actaccountDateService;
	@Autowired
	private NoticeServiceImpl noticeService;
	
	@Override
	public void execute() {
		try {
			clearChannelCheckAppService.downLoadAndParseBill(actaccountDateService.getActAccountDateDto().getCurAccountDate(),EAutoManual.AUTO);
		} catch (Exception e) {
			_log.error("下载和解析资金渠道流水失败：" +ExceptionProcUtil.getExceptionDesc(e));
			noticeService.sendErrorMsg(EBisExceptionLogNlevel.ERROR	, EBisExceptionLogType.BASE_SERVICE, "公共基础定时任务调用清结算下载解析渠道流水异常", "");
		}
	}

}
