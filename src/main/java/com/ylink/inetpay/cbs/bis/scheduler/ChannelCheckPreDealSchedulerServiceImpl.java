package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.chl.service.NoticeServiceImpl;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.project.channel.app.CheckFileTaskAppService;

/**
 * 资金渠道对账预处理
 * @author LS
 *
 */
public class ChannelCheckPreDealSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(ChannelCheckPreDealSchedulerServiceImpl.class);
	
	@Autowired
	CheckFileTaskAppService checkFileTaskAppService;
	@Autowired
	private NoticeServiceImpl noticeService;
	@Autowired
	protected ThreadPoolTaskExecutor taskExecutor;
	
	@Override
	public void execute() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					checkFileTaskAppService.greatUploadFileTask();
				} catch (Exception ex) {
					_log.error("获取对账信息生成对账文件并上传失败：{}", ex);
					noticeService.sendErrorMsg(EBisExceptionLogNlevel.ERROR	, EBisExceptionLogType.BASE_SERVICE, "公共基础定时任务调用渠道对账预处理异常", "");
				}
			}
		});
	}

}
