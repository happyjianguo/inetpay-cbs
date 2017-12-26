package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.channel.app.ChlAccountChangeNotifyAppService;

/**
 * 动账补发文件下载
 * 
 * @author lhui
 *
 */
public class ChlAccountChangeNotifyDownloadServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(ChlAccountChangeNotifyDownloadServiceImpl.class);

	@Autowired
	ChlAccountChangeNotifyAppService chlAccountChangeNotifyAppService;

	@Override
	public void execute() {
		_log.info("动账补发文件下载开始.");
		chlAccountChangeNotifyAppService.accChgFileDownload();
		_log.info("动账补发文件下载结束.");
	}

}
