package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.eu.util.tools.DateUtil;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.channel.app.ChlAccountChangeNotifyAppService;

/**
 * 动账通知查询（当日）
 * 
 * @author lhui
 *
 */
public class ChlAccountChangeNotifyDayServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(ChlAccountChangeNotifyDayServiceImpl.class);

	@Autowired
	ChlAccountChangeNotifyAppService chlAccountChangeNotifyAppService;

	@Override
	public void execute() {
		_log.info("开始进行动账通知查询（当日）.");
		String startDate = DateUtil.getNowYearMonthDay$();
		String endDate = startDate;
		chlAccountChangeNotifyAppService.accChgNotifyProcess(startDate, endDate);
		_log.info("动账通知查询结束（当日）.");
	}

}
